package br.com.climario.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.dominio.Pedido.Pagagamento;
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
	
	private static final String ERRO_PARAM = "erro";

	private static Logger _logger = LoggerFactory.getLogger(PedidoView.class);

	private static final String BOLETO_METHOD = "BOLETO";
	
	private static final String ID = "transaction";
	
	private static final String NUMERO = "numero";

	private static final long serialVersionUID = -3297581325023937731L;

	private transient IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	private HttpSession session;

	private String numero;
	
	private String telefone;
	
	private String telefoneHolder;

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
	
	private String aniversario;
	
	private String validade;
	
	public String getTelefoneHolder() {
		return telefoneHolder;
	}

	public void setTelefoneHolder(String telefoneHolder) {
		this.telefoneHolder = telefoneHolder;
	}

	public static Logger get_logger() {
		return _logger;
	}

	public static void set_logger(Logger _logger) {
		PedidoView._logger = _logger;
	}

	public IPedidoService getPedidoService() {
		return pedidoService;
	}

	public void setPedidoService(IPedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public DecimalFormat getFormat() {
		return format;
	}

	public void setFormat(DecimalFormat format) {
		this.format = format;
	}

	public String getAniversario() {
		return aniversario;
	}

	public void setAniversario(String aniversario) {
		this.aniversario = aniversario;
	}

	public static String getErroParam() {
		return ERRO_PARAM;
	}

	public static String getBoletoMethod() {
		return BOLETO_METHOD;
	}

	public static String getId() {
		return ID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCards(List<PaymentMethod> cards) {
		this.cards = cards;
	}

	public void setParcelas(List<Installment> parcelas) {
		this.parcelas = parcelas;
	}

	private String cpfCnpjHolder;
	
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
		
        _logger.info(tipo);
		try {
			
			AccountCredentials accountCredentials = getAccountCredencials();
			final String sessionId = SessionService.createSession(accountCredentials);
			_logger.info("Session ID: " + sessionId);
			RequestContext.getCurrentInstance().addCallbackParam("sessionId", sessionId);
			/*RequestContext.getCurrentInstance().addCallbackParam("account", accountCredentials.getEmail());
			RequestContext.getCurrentInstance().addCallbackParam("token", accountCredentials.getToken());*/
			RequestContext.getCurrentInstance().addCallbackParam("valorTotal", format.format(getTotalPedido()));
	        //checkout(null);
			
		} catch (PagSeguroServiceException e) {
			
			e.printStackTrace();
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
	
	public String getCpfCnpjHolder() {
		return cpfCnpjHolder;
	}
	
	public void setCpfCnpjHolder(String cpfCnpjHolder) {
		this.cpfCnpjHolder = cpfCnpjHolder;
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
	
	public String getEnv() {
		//return Util.getString("environment");
		return ServiceLocator.getInstance().getProperty(ServiceLocator.ENV);
	}

	public void consultar(ActionEvent actionEvent) {

		InputText cpfCnpj = (InputText) actionEvent.getComponent().getParent().findComponent("cpfCnpj");
		if (!pedidoService.isPedidoClienteExiste(cpfCnpj.getValue().toString(), numero)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido " + numero + " não encontrado", "Erro!"));
		} else {
			Util.getSession().setAttribute(ID, System.currentTimeMillis());
			Util.getSession().setAttribute(NUMERO, numero);
			Util.redirect(Util.getContextRoot("/pages/resumo.jsf?id=" + Util.getSession().getAttribute(ID).toString()));
		}
	}
	
	public void escolherCartao() {
		
	}
	
	public String getTotalAmount(BigDecimal totalAmount) {
	
		//return DecimalFormat.getCurrencyInstance(new Locale("pt_BR")).format(totalAmount.doubleValue());
		return DecimalFormat.getCurrencyInstance(new Locale("pt", "BR")).format(totalAmount.doubleValue());
	}
	
	public void changeParcela() {
		
		_logger.info(option);
		
		String cardBrand = option.toLowerCase();
		
		try {
			final AccountCredentials accountCredentials = getAccountCredencials();
			Installments installments = InstallmentService.getInstallments(accountCredentials, new BigDecimal(format.format(getTotalPedido())), option.toLowerCase());
			
			parcelas.clear();
			for (Installment installment : installments.get(cardBrand)) {
				_logger.info(installment.toString());
				parcelas.add(installment);
			}
		
		} catch (PagSeguroServiceException e) {

			System.err.println(e.getMessage());
		}
	}
	
	public void execPagamentos() {
	
		FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    processarPagamentos(map);
	}
	
	public void processarPagamentos(Map<String, String> map) {
		
		_logger.info("processarPagamentos: " + tipo);
		JSONObject methods = new JSONObject(map.get("paymentMethods"));
	    cards.clear();
	    
	    Iterator<String> payments = methods.getJSONObject(tipo).getJSONObject("options").keys();
	    while (payments.hasNext()) {
	    	
	    	String k = payments.next();
	    	JSONObject obj = methods.getJSONObject(tipo).getJSONObject("options").getJSONObject(k);
	    	
	    	PaymentMethod p = new PaymentMethod(obj.getInt("code"), obj.getString("name"), obj.getString("displayName"), PaymentMethodStatus.valueOf(obj.getString("status")));
	    	if (PaymentMethodStatus.UNAVAILABLE.equals(p.getStatus())) {
				continue;
			}
	    	
	    	cards.add(p);
		}
	    
	    if(BOLETO_METHOD.equals(tipo) && !cards.isEmpty()) {
			option = cards.get(0).getName();
		}
	}
	
	public void exec(ActionEvent event) {
		
		_logger.info("exec: " + tipo);
		_logger.info("event: " + event);
		if(BOLETO_METHOD.equals(tipo)) {
			execBoleto(event);
		}
		else {
			execCartao(event);
		}
		
		Boolean error = Boolean.valueOf(RequestContext.getCurrentInstance().getCallbackParams().get(ERRO_PARAM).toString());
		if(!error) {
			Util.redirect(Util.getContextRoot("/pages/confirmacao.jsf?id=" + Util.getSession().getAttribute(ID).toString()));	
		}
		
	}
	
	public void execBoleto(ActionEvent event) {
		
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    _logger.info("token card: " + map.get("token"));
	    _logger.info("sender hash: " + map.get("senderHash"));
	
	    final BoletoCheckout request = new BoletoCheckout();

        request.setPaymentMode(PaymentMode.DEFAULT);

        request.setReceiverEmail(Util.getString("credential.email"));

        request.setCurrency(Currency.BRL);

        request.setNotificationURL(Util.getContextRoot("/status"));

        request.setReference(pedido.getNumero());
        //System.out.println(pedido.getCliente().getCpfCnpj()+"###############################################111");
        
        /*String tel[] = map.get("telefoneHolder").split(" ");
        tel[0] = tel[0].replace(")", "");
        tel[0] = tel[0].replace("(", "");*/
        
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
			_logger.info(val);
			
			request.addItem(new Item(item.getCodigo(), //
					item.getDescricao(), //
	                item.getQtd(), //
	                new BigDecimal(val)));	 
		}
        
        transation(request, Pagagamento.BOLETO);
	}
	
	public void execCartao(ActionEvent event) {
		
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    _logger.info("token card: " + map.get("token"));
	    _logger.info("sender hash: " + map.get("senderHash"));
	    _logger.info("holder cpf: " + map.get("cpfCnpjHolder"));
	    _logger.info("holder nome: " + map.get("nomeHolder"));
	
	    final CreditCardCheckout request = new CreditCardCheckout();

        request.setPaymentMode(PaymentMode.DEFAULT);

        request.setReceiverEmail(Util.getString("credential.email"));

        request.setCurrency(Currency.BRL);
        
        request.setNotificationURL(Util.getContextRoot("/status"));

        request.setReference(pedido.getNumero());
        //System.out.println(pedido.getCliente().getCpfCnpj()+"###############################################222");
        //System.out.println(pedido.getNumero()+"###############################################222");
        
        String tel[] = map.get("telefoneHolder").split(" ");
        tel[0] = tel[0].replace(")", "");
        tel[0] = tel[0].replace("(", "");
        
        request.setSender(new Sender(pedido.getCliente().getNome(), //
        		pedido.getCliente().getEmail(), //
        		new Phone(tel[0], tel[1].replace("-","")), //
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
			_logger.info(val);
			
			request.addItem(new Item(item.getCodigo(), //
					item.getDescricao(), //
	                item.getQtd(), //
	                new BigDecimal(val)));	 
		}

        request.setCreditCardToken(map.get("token").toString());
        
        Installment installment = parcelas.get(numParcelas - 1);

        request.setInstallment(new br.com.uol.pagseguro.domain.direct.Installment(installment.getQuantity(), new BigDecimal(format.format(installment.getAmount()))));
        //System.out.println(map.get("telefoneHolder")+"###############################################");
       
       
        
        System.out.println(tel[0]);
        System.out.println(tel[1]);
        
        request.setHolder(new Holder(map.get("nomeHolder"), //
        		new Phone(tel[0], tel[1].replace("-","")), //
                new Document(map.get("cpfCnpjHolder").length() == 14 ? DocumentType.CPF : DocumentType.CNPJ, map.get("cpfCnpjHolder")), //
                map.get("aniversario")));

        request.setBillingAddress(new Address("BRA", //
        		pedido.getCliente().getEstado(), //
        		pedido.getCliente().getCidade(), //
        		pedido.getCliente().getBairro(), //
        		pedido.getCliente().getCep(), //
        		pedido.getCliente().getLogradouro(), //
        		pedido.getCliente().getNumero(), //
        		pedido.getCliente().getComplemento()));
        
        
        transation(request, Pagagamento.CARTAO);
	}
	
	public void validar(ActionEvent actionEvent){
		_logger.info("actionEvent", actionEvent);
		RequestContext.getCurrentInstance().addCallbackParam("autorizar", true);
		RequestContext.getCurrentInstance().addCallbackParam("tipo", tipo);
	}
	
	private Transaction transation(Checkout request, Pagagamento pagagamento) {
	
		String codigo = Long.toString(System.currentTimeMillis());
		Transaction transaction = null; 
		try {
			
			_logger.info("----------------------");
			_logger.info("processamento: " + codigo);
			final AccountCredentials accountCredentials = getAccountCredencials();
	
	        transaction = TransactionService.createTransaction(accountCredentials, request);
	        pedido.setCodigoAutorizacao(transaction.getCode());
	        if(Pagagamento.BOLETO.equals(pagagamento)) {
	        	pedidoService.atulizarCodigoTransacao(pedido.getNumero(), pagagamento, transaction.getCode(), transaction.getPaymentLink());
	        }
	        else {
	        	pedidoService.atulizarCodigoTransacao(pedido.getNumero(), pagagamento, transaction.getCode(), null);
	        }
	        
	        RequestContext.getCurrentInstance().addCallbackParam("link", transaction.getPaymentLink());
	
	        if (transaction != null) {
	            _logger.info("Transaction Code - Default Mode: " + transaction.getCode());
	        }
	        RequestContext.getCurrentInstance().addCallbackParam(ERRO_PARAM, false);
		} catch (PagSeguroServiceException e) {
			RequestContext.getCurrentInstance().addCallbackParam(ERRO_PARAM, true);
			RequestContext.getCurrentInstance().addCallbackParam("messagem", e.getMessage());
			
			RequestContext.getCurrentInstance().addCallbackParam("codigo", codigo);
	        System.err.println("codigo ==> " + e.getMessage());
	        
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido não processado!!", "Falha no processamento do pedido.<br/><br/>Código Erro: " + codigo + ".<br/>Informe o código acima ao administrador do sistema.");
	        RequestContext.getCurrentInstance().showMessageInDialog(message);
	    }
		
		_logger.info("");
		_logger.info("");
		return transaction;
	}

	public static AccountCredentials getAccountCredencials() throws PagSeguroServiceException {
		
		final AccountCredentials accountCredentials = PagSeguroConfig.getAccountCredentials();
		accountCredentials.setEmail(Util.getString("credential.email"));
		
		String env = ServiceLocator.getInstance().getProperty(ServiceLocator.ENV);
		
		if ("sandbox".equals(env)) {
			PagSeguroConfig.setSandboxEnvironment();
			accountCredentials.setSandboxToken(Util.getString("credential." + env + ".token"));
		} else {
			PagSeguroConfig.setProductionEnvironment();
			accountCredentials.setProductionToken(Util.getString("credential." + env + ".token"));
		}
		
		return accountCredentials;
	}
	
	public void handleChange(ValueChangeEvent event){
		_logger.info("here " + event.getNewValue());
	}

	public void checkout(ActionEvent actionEvent) {

		_logger.info("actionEvent", actionEvent);

		try {

			final AccountCredentials accountCredentials = getAccountCredencials();
			_logger.info(PagSeguroConfig.getEnvironment());
			_logger.info(PagSeguroConfig.getModuleVersion());

			_logger.info(accountCredentials.getEmail());
			_logger.info(accountCredentials.getToken());
			
			final PaymentMethods paymentMethods = PaymentMethodService.getPaymentMethods(accountCredentials, accountCredentials.getToken());
			
			cards.clear();
			for (PaymentMethod paymentMethod : paymentMethods.getPaymentMethodsByType(BOLETO_METHOD.equals(tipo) ? PaymentMethodType.BOLETO : PaymentMethodType.CREDIT_CARD)) {

				if (PaymentMethodStatus.UNAVAILABLE.equals(paymentMethod.getStatus())) {
					continue;
				}

				cards.add(paymentMethod);
			}
			
			
			if(BOLETO_METHOD.equals(tipo) && !cards.isEmpty()) {
				option = cards.get(0).getName();
			}
			
			

		} catch (PagSeguroServiceException e) {

			e.printStackTrace();
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
		 * _logger.info(response);
		 * 
		 * 
		 * } catch (PagSeguroServiceException e) {
		 * 
		 * System.err.println(e.getMessage()); }
		 * 
		 * _logger.info(checkout);
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
		
		session = Util.getSession();
		if(session.getAttribute(NUMERO) == null) {
			Util.redirect(Util.getContextRoot());
		}
		else {
			try{
				numero = session.getAttribute(NUMERO).toString();
				pedido = pedidoService.recuperarPedido(numero);
				ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
				if(pedido.getCodigoAutorizacao() != null && !context.getRequestServletPath().contains("confirmacao.jsf")) {
					Util.redirect(Util.getContextRoot("/pages/confirmacao.jsf?id=" + Util.getSession().getAttribute(ID).toString()));	
				}
			}
			catch(RuntimeException e) {
			
				_logger.info("Pedido " + numero + " não existe.");
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
		InputText email = (InputText) actionEvent.getComponent().findComponent("email");
		InputText telefone = (InputText) actionEvent.getComponent().findComponent("telefone");
		
		if(!pedidoService.isClienteExiste(cpfCnpj.getValue().toString(), email.getValue().toString())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cliente não cadastrado!", "Erro!"));
		}
		else {
			Cliente c = pedidoService.recuperarCliente(cpfCnpj.getValue().toString());
			
			Object[] args = new Object[]{c.getNome(), c.getCpfCnpj(), telefone.getValue()};
			
			String txtCliente = Util.getString("texto.solicitacao.info", args);
			Util.sendMail(c.getEmail(), "Solicitar Pedido", txtCliente);
			
			String txtClima = Util.getString("texto.solicitacao", args);
			Util.sendMail(Util.getString("email.sender.account"), "Solicitar Pedido", txtClima);
			Util.redirect(Util.getContextRoot());
		}
		
	}
}
