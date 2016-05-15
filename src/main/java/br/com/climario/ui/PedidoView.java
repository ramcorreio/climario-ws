package br.com.climario.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;

import br.com.climario.dominio.Cliente;
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
import br.com.uol.pagseguro.domain.direct.checkout.BoletoCheckout;
import br.com.uol.pagseguro.domain.direct.checkout.Checkout;
import br.com.uol.pagseguro.domain.direct.checkout.CreditCardCheckout;
import br.com.uol.pagseguro.domain.installment.Installment;
import br.com.uol.pagseguro.domain.installment.Installments;
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
import br.com.uol.pagseguro.service.InstallmentService;
import br.com.uol.pagseguro.service.PaymentMethodService;
import br.com.uol.pagseguro.service.SessionService;
import br.com.uol.pagseguro.service.TransactionService;

@ManagedBean
@ViewScoped
public class PedidoView implements Serializable {

	private static final long serialVersionUID = -3297581325023937731L;

	private transient IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();

	private String numero;
	
	private String telefone;

	private Pedido pedido;

	private String option;

	private List<PaymentMethod> cards = new ArrayList<>();
	
	private List<Installment> parcelas = new ArrayList<>();
	
	private Integer numParcelas = 1;
	
	private DecimalFormat format = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	
	private String token;
	
	private String numeroCartao;
	
	private String cvv;
	
	private String nome;
	
	private String validade;
	
	private String tipo;
	
	public Integer getNumParcelas() {
		return numParcelas;
	}
	
	public void setNumParcelas(Integer numParcelas) {
		this.numParcelas = numParcelas;
	}
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void handleKeyEvent() {
        System.out.println(tipo);
        checkout(null);
        if(!cards.isEmpty()) {
        	option = cards.get(0).getName();
        }
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
	
	public List<Installment> getParcelas() {
		return parcelas;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public boolean isExistePedido() {
		
		return pedido != null; 
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
	
	public void escolherCartao() {
		
	}
	
	public String getTotalAmount(BigDecimal totalAmount) {
	
		//return DecimalFormat.getCurrencyInstance(new Locale("pt_BR")).format(totalAmount.doubleValue());
		return DecimalFormat.getCurrencyInstance().format(totalAmount.doubleValue());
	}
	
	public void changeParcela() {
		
		System.out.println(option);
		
		String cardBrand = option.toLowerCase();
		
		try {
			final AccountCredentials accountCredentials = getAccountCredencials();
			Installments installments = InstallmentService.getInstallments(accountCredentials, new BigDecimal(format.format(getTotalPedido())), option.toLowerCase());
			
			parcelas.clear();
			for (Installment installment : installments.get(cardBrand)) {
				System.out.println(installment);
				parcelas.add(installment);
			}
		
		} catch (PagSeguroServiceException e) {

			System.err.println(e.getMessage());
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

        request.setReceiverEmail(Util.getString("credential.email"));

        request.setCurrency(Currency.BRL);

        request.setNotificationURL(Util.getContextRoot("/status"));

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
        
        transation(request);
		
	}
	
	public void execCartao() {
		
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    System.out.println("token card" + map.get("token"));
	    System.out.println("sender hash" + map.get("senderHash"));
	
	    final CreditCardCheckout request = new CreditCardCheckout();

        request.setPaymentMode(PaymentMode.DEFAULT);

        request.setReceiverEmail(Util.getString("credential.email"));

        request.setCurrency(Currency.BRL);
        
        request.setNotificationURL(Util.getContextRoot("/status"));

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
        
        Installment installment = parcelas.get(numParcelas - 1);

        request.setInstallment(new br.com.uol.pagseguro.domain.direct.Installment(installment.getQuantity(), new BigDecimal(format.format(installment.getAmount()))));

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
	        
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Pedido não processadno", "Falha no processamento do pedido. Erro: " + codigo);
	        RequestContext.getCurrentInstance().showMessageInDialog(message);
	    }
		
		System.out.println();
		System.out.println();
	}

	public static AccountCredentials getAccountCredencials() throws PagSeguroServiceException {
		
		final AccountCredentials accountCredentials = PagSeguroConfig.getAccountCredentials();
		accountCredentials.setEmail(Util.getString("credential.email"));
		
		if ("sandbox".equals(Util.getString("environment"))) {
			PagSeguroConfig.setSandboxEnvironment();
			accountCredentials.setSandboxToken(Util.getString("credential." + Util.getString("environment") + ".token"));
		} else {
			PagSeguroConfig.setProductionEnvironment();
			accountCredentials.setProductionToken(Util.getString("credential." + Util.getString("environment") + ".token"));
		}
		
		return accountCredentials;
	}

	public void checkout(ActionEvent actionEvent) {

		System.out.println(actionEvent);

		try {

			if (Util.getString("environment").equals("sandbox")) {
				PagSeguroConfig.setSandboxEnvironment();
			} else {
				PagSeguroConfig.setProductionEnvironment();
			}

			System.out.println(Util.getString("environment"));

			final AccountCredentials accountCredentials = getAccountCredencials();

			System.out.println(accountCredentials.getEmail());
			System.out.println(accountCredentials.getToken());
			
			final String sessionId = SessionService.createSession(accountCredentials);
			System.out.println("Session ID: " + sessionId);

			final String publicKey = Util.getString("credential.public");
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
			try{
				pedido = pedidoService.recuperarPedido(numero);
				if(pedido.getCodigoAutorizacao() != null) {
					Util.redirect(Util.getContextRoot("/pages/confirmacao.jsf"));	
				}
			}
			catch(RuntimeException e) {
			
				System.out.println("Pedido " + numero + " não existe.");
			}
		}
	}

	public static class ItemWrap {

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

		return getItens(pedido);
	}

	public static List<ItemWrap> getItens(Pedido p) {

		List<ItemWrap> itens = new ArrayList<>();
		for (ItemPedido itemPedido : p.getItens()) {
			itens.add(new ItemWrap(itemPedido));
		}
		return itens;
	}

	public Double getTotalPedido() {

		return getTotalPedido(pedido);
	}
	
	public static Double getTotalPedido(Pedido p) {

		Double sum = 0d;
		for (ItemWrap itemPedido : getItens(p)) {
			sum += itemPedido.getTotal();
		}
		return sum + p.getValorFrete();
	}
	
	public void solicitar(ActionEvent actionEvent) {
		
		InputText cpfCnpj = (InputText) actionEvent.getComponent().findComponent("cpfCnpj");
		InputText nuPedido = (InputText) actionEvent.getComponent().findComponent("nuPedido");
		InputText telefone = (InputText) actionEvent.getComponent().findComponent("telefone");
		
		if(!pedidoService.isPedidoClienteExiste(cpfCnpj.getValue().toString(), nuPedido.getValue().toString())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido não cadastrado!", "Erro!"));
		}
		else {
			Pedido p = pedidoService.recuperarPedido(nuPedido.getValue().toString());
			Cliente c = p.getCliente();
			
			//Cliente c = pedidoService.recuperarCliente(cpfCnpj.getValue().toString());
			Object[] args = new Object[]{c.getNome(), c.getCpfCnpj(), telefone.getValue()};
			
			String txtCliente = Util.getString("texto.solicitacao.info", args);
			Util.sendMail(p.getCliente().getEmail(), "Solicitar Pedido", txtCliente);
			
			String txtClima = Util.getString("texto.solicitacao", args);
			Util.sendMail(Util.getString("email.sender.account"), "Solicitar Pedido", txtClima);
			Util.redirect(Util.getContextRoot());
		}
		
	}
}
