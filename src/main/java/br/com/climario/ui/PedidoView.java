package br.com.climario.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;

import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;
import br.com.climario.service.impl.ServiceLocator;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Document;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.SenderDocument;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.direct.Holder;
import br.com.uol.pagseguro.domain.direct.Installment;
import br.com.uol.pagseguro.domain.direct.checkout.BoletoCheckout;
import br.com.uol.pagseguro.domain.direct.checkout.Checkout;
import br.com.uol.pagseguro.domain.direct.checkout.CreditCardCheckout;
import br.com.uol.pagseguro.domain.paymentmethod.PaymentMethod;
import br.com.uol.pagseguro.domain.paymentmethod.PaymentMethods;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.DocumentType;
import br.com.uol.pagseguro.enums.PaymentMethodStatus;
import br.com.uol.pagseguro.enums.PaymentMethodType;
import br.com.uol.pagseguro.enums.PaymentMode;
import br.com.uol.pagseguro.enums.ShippingType;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.PaymentMethodService;
import br.com.uol.pagseguro.service.SessionService;
import br.com.uol.pagseguro.service.TransactionService;

@ManagedBean
@ViewScoped
public class PedidoView implements Serializable {

	private static final long serialVersionUID = -3297581325023937731L;

	private transient IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();

	private transient ResourceBundle bundle = ResourceBundle.getBundle("pagseguro");

	private String numero;

	private Pedido pedido;

	private String option;

	private List<PaymentMethod> cards = new ArrayList<>();
	
	private DecimalFormat format = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	
	private String token;
	
	private String numeroCartao;
	
	private String cvv;
	
	private String nome;
	
	private String validade;
	
	private String tipo;
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void handleKeyEvent() {
        System.out.println(tipo);
        checkout(null);
    }
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public List<PaymentMethod> getCards() {
		return cards;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public void consultar(ActionEvent actionEvent) {

		InputText cpfCnpj = (InputText) actionEvent.getComponent().getParent().findComponent("cpfCnpj");
		if (!pedidoService.isPedidoClienteExiste(cpfCnpj.getValue().toString(), numero)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido " + numero + " não encontrado", "Erro!"));
		} else {
			Util.redirect(Util.getContextRoot("/pages/resumo.jsf?id=" + numero));
		}
	}
	
	public void exec() {
		
		if("boleto".equals(tipo)) {
			execBoleto();
		}
		else {
			execCartao();
		}
	}
	
	public void execBoleto() {
		
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    System.out.println("token card" + map.get("token"));
	    System.out.println("sender hash" + map.get("senderHash"));
	
	    final BoletoCheckout request = new BoletoCheckout();

        request.setPaymentMode(PaymentMode.DEFAULT);

        request.setReceiverEmail(bundle.getString("credential.email"));

        request.setCurrency(Currency.BRL);

        //request.setNotificationURL("http://www.meusite.com.br/notification");

        request.setReference(pedido.getNumero());

        request.setSender(new Sender(pedido.getCliente().getNome(), //
        		pedido.getCliente().getEmail(), //
        		new Phone("99", "99999999"), //
                new SenderDocument(pedido.getCliente().getCpfCnpj().length() == 11 ? DocumentType.CPF : DocumentType.CNPJ, pedido.getCliente().getCpfCnpj())));

        request.setSenderHash(map.get("senderHash").toString());
        
        request.setShippingAddress(new Address("BRA", //
        		pedido.getCliente().getEstado(), //
        		pedido.getCliente().getCidade(), //
        		pedido.getCliente().getBairro(), //
        		pedido.getCliente().getCep(), //
        		pedido.getCliente().getLogradouro(), //
        		pedido.getCliente().getNumero(), //
        		pedido.getCliente().getComplemento()));
        
        request.setShippingType(ShippingType.SEDEX);

        request.setShippingCost(new BigDecimal(format.format(pedido.getValorFrete())));

        for (ItemPedido item : pedido.getItens()) {
			
			String val = format.format(item.getPrecoUnitario());
			System.out.println(val);
			
			request.addItem(new Item(item.getCodigo(), //
					item.getDescricao(), //
	                item.getQtd(), //
	                new BigDecimal(val)));	 
		}

        /*request.setCreditCardToken(map.get("token").toString());

        request.setInstallment(new Installment(1, new BigDecimal(format.format(getTotalPedido()))));

        request.setHolder(new Holder(pedido.getCliente().getNome(), //
        		new Phone("99", "99999999"), //
                new Document(pedido.getCliente().getCpfCnpj().length() == 11 ? DocumentType.CPF : DocumentType.CNPJ, pedido.getCliente().getCpfCnpj()), //
                "01/01/1900"));

        request.setBillingAddress(new Address("BRA", //
        		pedido.getCliente().getEstado(), //
        		pedido.getCliente().getCidade(), //
        		pedido.getCliente().getBairro(), //
        		pedido.getCliente().getCep(), //
        		pedido.getCliente().getLogradouro(), //
        		pedido.getCliente().getNumero(), //
        		pedido.getCliente().getComplemento()));*/
        
        
        transation(request);
		
	}
	
	public void execCartao() {
		
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    System.out.println("token card" + map.get("token"));
	    System.out.println("sender hash" + map.get("senderHash"));
	
	    final CreditCardCheckout request = new CreditCardCheckout();

        request.setPaymentMode(PaymentMode.DEFAULT);

        request.setReceiverEmail(bundle.getString("credential.email"));

        request.setCurrency(Currency.BRL);

        //request.setNotificationURL("http://www.meusite.com.br/notification");

        request.setReference(pedido.getNumero());

        request.setSender(new Sender(pedido.getCliente().getNome(), //
        		pedido.getCliente().getEmail(), //
        		new Phone("99", "99999999"), //
                new SenderDocument(pedido.getCliente().getCpfCnpj().length() == 11 ? DocumentType.CPF : DocumentType.CNPJ, pedido.getCliente().getCpfCnpj())));

        request.setSenderHash(map.get("senderHash").toString());
        
        request.setShippingAddress(new Address("BRA", //
        		pedido.getCliente().getEstado(), //
        		pedido.getCliente().getCidade(), //
        		pedido.getCliente().getBairro(), //
        		pedido.getCliente().getCep(), //
        		pedido.getCliente().getLogradouro(), //
        		pedido.getCliente().getNumero(), //
        		pedido.getCliente().getComplemento()));
        
        request.setShippingType(ShippingType.SEDEX);

        request.setShippingCost(new BigDecimal(format.format(pedido.getValorFrete())));

        for (ItemPedido item : pedido.getItens()) {
			
			String val = format.format(item.getPrecoUnitario());
			System.out.println(val);
			
			request.addItem(new Item(item.getCodigo(), //
					item.getDescricao(), //
	                item.getQtd(), //
	                new BigDecimal(val)));	 
		}

        request.setCreditCardToken(map.get("token").toString());

        request.setInstallment(new Installment(1, new BigDecimal(format.format(getTotalPedido()))));

        request.setHolder(new Holder(pedido.getCliente().getNome(), //
        		new Phone("99", "99999999"), //
                new Document(pedido.getCliente().getCpfCnpj().length() == 11 ? DocumentType.CPF : DocumentType.CNPJ, pedido.getCliente().getCpfCnpj()), //
                "01/01/1900"));

        request.setBillingAddress(new Address("BRA", //
        		pedido.getCliente().getEstado(), //
        		pedido.getCliente().getCidade(), //
        		pedido.getCliente().getBairro(), //
        		pedido.getCliente().getCep(), //
        		pedido.getCliente().getLogradouro(), //
        		pedido.getCliente().getNumero(), //
        		pedido.getCliente().getComplemento()));
        
        
        transation(request);
	}
	
	public void validar(ActionEvent actionEvent){
		System.out.println(actionEvent);
		RequestContext.getCurrentInstance().addCallbackParam("autorizar", true);
		RequestContext.getCurrentInstance().addCallbackParam("tipo", tipo);
	}
	
	private void transation(Checkout request) {
	
		String codigo = Long.toString(System.currentTimeMillis());
		try {
			
			System.out.println("----------------------");
			System.out.println("processamento: " + codigo);
			final AccountCredentials accountCredentials = getAccountCredencials();
	
	        final Transaction transaction = TransactionService.createTransaction(accountCredentials, request);
	        pedido.setCodigoAutorizacao(transaction.getCode());
	        pedidoService.atulizarCodigoTransacao(pedido.getNumero(), transaction.getCode());
	
	        if (transaction != null) {
	            System.out.println("Transaction Code - Default Mode: " + transaction.getCode());
	            Util.redirect(Util.getContextRoot("/pages/confirmacao.jsf"));
	        }
	        RequestContext.getCurrentInstance().addCallbackParam("erro", false);
		} catch (PagSeguroServiceException e) {
			RequestContext.getCurrentInstance().addCallbackParam("erro", true);
			RequestContext.getCurrentInstance().addCallbackParam("messagem", e.getMessage());
			
			RequestContext.getCurrentInstance().addCallbackParam("codigo", codigo);
	        System.err.println("codigo ==> " + e.getMessage());
	    }
		System.out.println();
		System.out.println();
	}

	private AccountCredentials getAccountCredencials() throws PagSeguroServiceException {
		
		final AccountCredentials accountCredentials = PagSeguroConfig.getAccountCredentials();
		accountCredentials.setEmail(bundle.getString("credential.email"));
		
		if ("sandbox".equals(bundle.getString("environment"))) {
			PagSeguroConfig.setSandboxEnvironment();
			accountCredentials.setSandboxToken(bundle.getString("credential." + bundle.getString("environment") + ".token"));
		} else {
			PagSeguroConfig.setProductionEnvironment();
			accountCredentials.setProductionToken(bundle.getString("credential." + bundle.getString("environment") + ".token"));
		}
		
		return accountCredentials;
	}

	public void checkout(ActionEvent actionEvent) {

		System.out.println(actionEvent);
		// PaymentRequest paymentRequest = new PaymentRequest();

		try {

			if (bundle.getString("environment").equals("sandbox")) {
				PagSeguroConfig.setSandboxEnvironment();
			} else {
				PagSeguroConfig.setProductionEnvironment();
			}

			System.out.println(bundle.getString("environment"));

			final AccountCredentials accountCredentials = getAccountCredencials();

			System.out.println(accountCredentials.getEmail());
			System.out.println(accountCredentials.getToken());
			
			final String sessionId = SessionService.createSession(accountCredentials);
			System.out.println("Session ID: " + sessionId);

			final String publicKey = bundle.getString("credential.public");
			System.out.println("publicKey: " + publicKey);
			
			final PaymentMethods paymentMethods = PaymentMethodService.getPaymentMethods(accountCredentials, publicKey);
			RequestContext.getCurrentInstance().addCallbackParam("sessionId", sessionId);

			cards.clear();
			for (PaymentMethod paymentMethod : paymentMethods.getPaymentMethodsByType("boleto".equals(tipo) ? PaymentMethodType.BOLETO : PaymentMethodType.CREDIT_CARD)) {

				if (PaymentMethodStatus.UNAVAILABLE.equals(paymentMethod.getStatus())) {
					continue;
				}

				cards.add(paymentMethod);
			}

		} catch (PagSeguroServiceException e) {

			System.err.println(e.getMessage());
		}

		/*
		 * Checkout checkout = new Checkout(); for (ItemPedido item :
		 * pedido.getItens()) { checkout.addItem(item.getCodigo(),
		 * item.getDescricao(), item.getQtd(), new
		 * BigDecimal(item.getPrecoUnitario()), 0l, new BigDecimal(0)); }
		 * 
		 * checkout.setShippingAddress( "BRA", // País
		 * pedido.getCliente().getEstado(), // UF
		 * pedido.getCliente().getCidade(), // Cidade
		 * pedido.getCliente().getBairro(), // Bairro
		 * pedido.getCliente().getCep(), // CEP
		 * pedido.getCliente().getLogradouro(), // Logradouro
		 * pedido.getCliente().getNumero(), // Número
		 * pedido.getCliente().getComplemento() // Complemento );
		 * 
		 * checkout.setShippingType(ShippingType.SEDEX);
		 * 
		 * checkout.setShippingCost(new BigDecimal(pedido.getValorFrete()));
		 * 
		 * checkout.setSender( pedido.getCliente().getNome(), // Nome completo
		 * pedido.getCliente().getEmail(), // email "", // DDD "", // Telefone
		 * DocumentType.CPF, // Tipo de documento
		 * pedido.getCliente().getCpfCnpj() // Número do documento );
		 * 
		 * checkout.setCurrency(Currency.BRL);
		 * 
		 * try {
		 * 
		 * boolean onlyCheckoutCode = false; String response =
		 * checkout.register(PagSeguroConfig.getAccountCredentials(),
		 * onlyCheckoutCode);
		 * 
		 * System.out.println(response);
		 * 
		 * 
		 * } catch (PagSeguroServiceException e) {
		 * 
		 * System.err.println(e.getMessage()); }
		 * 
		 * System.out.println(checkout);
		 */
		// checkout.addItem("id", "ddd", 1, new BigDecimal(3), 0l, new
		// BigDecimal(0));

		/*
		 * PaymentRequest p = new PaymentRequest(); p.addItem(, description,
		 * quantity, amount, weight, shippingCost);
		 */
		// Checkout checkout = new Checkout();

	}

	public void init() {
		if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("id")) {
			numero = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			pedido = pedidoService.recuperarPedido(numero);
			
			if(pedido.getCodigoAutorizacao() != null) {
				Util.redirect(Util.getContextRoot("/pages/confirmacao.jsf"));	
			}
		}
		/*else { 
			Util.redirect(Util.getContextRoot("confirmacao.jsf")); 
		}*/
	}

	public class ItemWrap {

		private ItemPedido item;

		public ItemWrap(ItemPedido item) {
			this.item = item;
		}

		public String getCodigo() {
			return item.getCodigo();
		}

		public String getDescricao() {
			return item.getDescricao();
		}

		public Integer getQtd() {
			return item.getQtd();
		}

		public Double getPrecoUnitario() {
			return item.getPrecoUnitario();
		}

		public Double getTotal() {
			return item.getPrecoUnitario() * item.getQtd();
		}

	}

	public List<ItemWrap> getItens() {

		List<ItemWrap> itens = new ArrayList<>();
		for (ItemPedido itemPedido : pedido.getItens()) {
			itens.add(new ItemWrap(itemPedido));
		}
		return itens;
	}

	public Double getTotalPedido() {

		Double sum = 0d;
		for (ItemWrap itemPedido : getItens()) {
			sum += itemPedido.getTotal();
		}
		return sum + pedido.getValorFrete();
	}
}
