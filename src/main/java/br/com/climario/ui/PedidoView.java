package br.com.climario.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.ItemPedido;
import br.com.climario.dominio.Pedido;
import br.com.climario.dominio.Pedido.Pagagamento;
//import br.com.climario.dominio.Pedido.PedidoStatus;
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
	
	private final static String USER_AGENT = "Mozilla/5.0";
	
	private static final String ERRO_PARAM = "erro";

	private static Logger _logger = LoggerFactory.getLogger(PedidoView.class);

	private static final String BOLETO_METHOD = "BOLETO";
	
	private static final String ID = "transaction";
	
	private static final String NUMERO = "numero";

	private static final long serialVersionUID = -3297581325023937731L;
	
	public String envioEmail = "marcio@internit.com.br";
	
	//public String envioEmailU = "jonath@internit.com.br";

	private transient IPedidoService pedidoService = ServiceLocator.getInstance().getPedidoService();
	
	private HttpSession session;

	private String numero;
	
	private String telefone;
	
	private String telefoneHolder;
	
	private String dddHolder;

	private Pedido pedido;

	private String option;

	private List<CartoesPagamento> cards = new ArrayList<>();
	
	private List<Parcelas> parcelas = new ArrayList<>();
	
	private Integer numParcelas = 1;
	
	private DecimalFormat format = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	
	private String token;
	
	private String numeroCartao;
	
	private String cvv;
	
	private String nome;
	
	private String aniversario;
	
	private String validade;
	
	public String getDddHolder() {
		return dddHolder;
	}

	public void setDddHolder(String dddHolder) {
		this.dddHolder = dddHolder;
	}

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

	public void setCards(List<CartoesPagamento> cards) {
		this.cards = cards;
	}

	public void setParcelas(List<Parcelas> parcelas) {
		this.parcelas = parcelas;
	}

	private String cpfCnpjHolder;
	
	private String tipo;

	private String[] arrayDescs;
	
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

	public List<CartoesPagamento> getCards() {
		return cards;
	}
	
	public List<Parcelas> getParcelas() {
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
	
	public void changeParcela() 
	{
		double parcela;
		int quantity = 1;
		double totalAmount = getTotalPedido(pedido);
		
		parcelas.clear();
		try {
			for(int i = 1; i<=10; i++)
			{
				parcela = totalAmount/i;
				
				
				DecimalFormat df = new DecimalFormat("###.##");
				df.setRoundingMode(RoundingMode.UP);
				
				Parcelas parc = new Parcelas(df.format(parcela), i, totalAmount);
				parcelas.add(parc);
			}
			
		
		} catch (Exception e) {

			System.err.println(e.getMessage());
		}
		
	}
	
	public void execPagamentos() {
	
		_logger.info("teste: " + tipo);
		FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();	
	    
	    JSONObject token = solicitarToken();		
		JSONObject formaPagamento = solicitarPagamentos(token.getString("token_type"), token.getString("access_token"));
		JSONArray paymentMethods = formaPagamento.getJSONArray("paymentMethods");
		
	    processarPagamentos(paymentMethods);
	}
	//TODO: teste
	public void processarPagamentos(JSONArray paymentMethods) {
		
		_logger.info("processarPagamentos: " + tipo);
		//JSONObject methods = new JSONObject(map.get("paymentMethods"));
	    cards.clear();
	    
	    List<String> arrayDescs = new ArrayList<>();
	    
	    for (int i = 0; i < paymentMethods.length(); ++i) {
		    JSONObject rec = paymentMethods.getJSONObject(i);
		    
		    int id = rec.getInt("id");
		    String description = rec.getString("description");
		    String country = rec.getString("country");
		    Boolean enabled = rec.getBoolean("enabled");
		    //String reason = rec.getString("reason");
		    
		    //PaymentMethod p = new PaymentMethod(obj.getInt("code"), obj.getString("name"), obj.getString("displayName"), PaymentMethodStatus.valueOf(obj.getString("status")));
		    
		    /*System.out.println(arrayDescs);
		    System.out.println(description);
		    System.out.println(arrayDescs.contains(description));*/
		    
		    if(arrayDescs.contains(description) == false && !description.equals("BOLETO_BANCARIO") && !tipo.equals("BOLETO"))
		    {
			    CartoesPagamento c = new CartoesPagamento(id, description, country, enabled);
			    cards.add(c);
			    
		    }else if(description.equals("BOLETO_BANCARIO") && tipo.equals("BOLETO")){
		    	 CartoesPagamento c = new CartoesPagamento(id, description, country, enabled);
				 cards.add(c);
		    }
		    arrayDescs.add(description);
		}
	    System.out.println(cards);
	    /*while (payments.hasNext()) {
	    	
	    	String k = payments.next();
	    	JSONObject obj = methods.getJSONObject(tipo).getJSONObject("options").getJSONObject(k);
	    	
	    	PaymentMethod p = new PaymentMethod(obj.getInt("code"), obj.getString("name"), obj.getString("displayName"), PaymentMethodStatus.valueOf(obj.getString("status")));
	    	if (PaymentMethodStatus.UNAVAILABLE.equals(p.getStatus())) {
				continue;
			}
	    	
	    	cards.add(p);
		}*/
	    
	   /* if(BOLETO_METHOD.equals(tipo) && !cards.isEmpty()) {
			option = cards.get(0).getName();
		}*/
	}
	
	public void exec(ActionEvent event) {
		
		if(BOLETO_METHOD.equals(tipo)) {
			execBoleto(event);
		}
		else {
			execCartao(event);
		}
		
		Boolean error = Boolean.valueOf(RequestContext.getCurrentInstance().getCallbackParams().get(ERRO_PARAM).toString());
		if(!error) {
			
			
			String texto = "O cliente "+pedido.getCliente().getNome()+", iniciou o processo de pagamento para o pedido "+pedido.getNumero()+". Um novo e-mail será enviado após o retorno do pagamento.<br><br>";
				   texto += "Clima Rio<br>";
				   texto += "Sempre a melhor compra.<br/><br/>";
				   texto += "<img src='http://climariopagamentos.com.br/javax.faces.resource/img/clima_logo.jpg.jsf?ln=media'>";
			
			String emailEnvio = "";
			
			if(pedido.getCliente().getEmailRca().isEmpty())
			{
				emailEnvio = envioEmail;
			}else{
				emailEnvio = pedido.getCliente().getEmailRca();
			}
		    
				   
			Util.sendMail(emailEnvio, "Solicitar Pedido", texto);
			
			
			String texto2 = "Prezado(a)  "+pedido.getCliente().getNome()+", <br /><br /> ";			   
			   texto2 += "Seu pedido "+pedido.getNumero()+" está em processo de análise junto a sua operadora de cartão. Você será informado(a), por e-mail, sobre o retorno da operadora referente a conclusão do pagamento.<br /><br />";
			   texto2 += "O processo de compra será concluído somente após este retorno.<br /><br />";
			   texto2 += "Em caso de dúvida ou problema entre em contato com o consultor de vendas que lhe atendeu.<br /><br />";
		
			   texto2 += "Clima Rio<br/>";
			   texto2 += "Sempre a melhor compra.<br/><br/>";
			   texto2 += "<img src='http://climariopagamentos.com.br/javax.faces.resource/img/clima_logo.jpg.jsf?ln=media'>";
	
			   Util.sendMail(pedido.getCliente().getEmail(), "Solicitar Pedido", texto2);
			
			//_logger.info(texto);
			Util.redirect(Util.getContextRoot("/pages/confirmacao.jsf?id=" + Util.getSession().getAttribute(ID).toString()));	
		}
	}
	
	
	public void execBoleto(ActionEvent event) {
		
		JSONObject token = solicitarToken();		
		JSONObject pagamento = efetuarPagamento(token.getString("token_type"), token.getString("access_token"), pedido,"boleto", null);
        
        transation(pagamento, Pagagamento.BOLETO);
	}
	
	public void execCartao(ActionEvent event) {
		
	    FacesContext context = FacesContext.getCurrentInstance();
	    Map<String, String> map = context.getExternalContext().getRequestParameterMap();
	    _logger.info("token card: " + map.get("token"));
	    _logger.info("sender hash: " + map.get("senderHash"));
	    _logger.info("holder cpf: " + map.get("cpfCnpjHolder"));
	    _logger.info("holder nome: " + map.get("nomeHolder"));
	
	    System.out.println(map);
	    
	    JSONObject token = solicitarToken();
	    JSONObject pagamento = efetuarPagamento(token.getString("token_type"), token.getString("access_token"), pedido,"cartao", map);
	    	    
	    transation(pagamento,Pagagamento.CARTAO);
	}
	
	public void validar(ActionEvent actionEvent){
		_logger.info("actionEvent", actionEvent);
		_logger.info("tipo", tipo);
		RequestContext.getCurrentInstance().addCallbackParam("autorizar", true);
		RequestContext.getCurrentInstance().addCallbackParam("tipo", tipo);
		
	}
	
	private void transation(JSONObject pagamento, Pagagamento pagagamento) {
		
		JSONObject token = solicitarToken();
		String codigo = Long.toString(System.currentTimeMillis());
		//TODO: teste	    
		try {
					
					
					JSONObject transaction = pagamento.getJSONObject("transactionResponse");			        
			        pedido.setCodigoAutorizacao(pagamento.getJSONObject("transactionResponse").getString("transactionId"));
			        
			        if(Pagagamento.BOLETO.equals(pagagamento)) {
			        	pedidoService.atulizarCodigoTransacao(pedido.getNumero(), pagagamento, transaction.getString("transactionId"), transaction.getJSONObject("extraParameters").getString("URL_BOLETO_BANCARIO"));
			        }
			        else {
			        	pedidoService.atulizarCodigoTransacao(pedido.getNumero(), pagagamento, transaction.getString("transactionId"), null);
			        }
			        
			        //pedidoService.atulizarStatus(pedido.getNumero(), PedidoStatus.APROVADA)
			        
			        RequestContext.getCurrentInstance().addCallbackParam("link", transaction.getJSONObject("extraParameters").getString("URL_BOLETO_BANCARIO"));
			
			        if (transaction != null) {
			            _logger.info("Transaction Code - Default Mode: " + transaction.getString("transactionId"));
			        }
			        
			        RequestContext.getCurrentInstance().addCallbackParam(ERRO_PARAM, false);
			        
			        
			        
				} catch (Exception e) {
					RequestContext.getCurrentInstance().addCallbackParam(ERRO_PARAM, true);
					RequestContext.getCurrentInstance().addCallbackParam("messagem", e.getMessage());
					
					RequestContext.getCurrentInstance().addCallbackParam("codigo", codigo);
					System.err.println("codigodp erro ==> " + codigo);
			        System.err.println("codigo ==> " + e.getMessage());
			        
			        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido não processado!!", "Falha no processamento do pedido.<br/><br/>Código Erro: " + codigo + ".<br/>Informe o código acima ao administrador do sistema.");
			        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido não processado!!", "Falha no processamento do pedido.<br/><br/>Erro: " + pagamento.get("error"));
			        RequestContext.getCurrentInstance().showMessageInDialog(message);
			    }
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

				//cards.add(paymentMethod);
			}
			
			
			/*if(BOLETO_METHOD.equals(tipo) && !cards.isEmpty()) {
				option = cards.get(0).getName();
			}*/
			
			

		} catch (PagSeguroServiceException e) {

			e.printStackTrace();
		}


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
				
				/*JSONObject token = solicitarToken();		
				JSONObject formaPagamento = solicitarPagamentos(token.getString("token_type"), token.getString("access_token"));
				JSONObject pagamento = efetuarPagamento(token.getString("token_type"), token.getString("access_token"), pedido);*/
				
				if(pedido.getCodigoAutorizacao() != null && !context.getRequestServletPath().contains("confirmacao.jsf")) 
				{
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
			String txtCliente = Util.getString("sendMail", args);
			Util.sendMail(c.getEmail(), "Solicitar Pedido", txtCliente);
			
			String txtClima = Util.getString("texto.solicitacao", args);
			Util.sendMail(Util.getString("email.sender.account"), "Solicitar Pedido", txtClima);
			Util.redirect(Util.getContextRoot());
		}
		
	}
	public static JSONObject efetuarPagamento(String tokenType, String access_token, Pedido pedido, String tipo, Map<String, String> map){
		
		String urlParameters = null;
		JSONObject json = null;
		
		try {		
			 String url = "https://api.payulatam.com/payments-api/4.0/service.cgi";
			 urlParameters = capturarParametros(pedido, tipo, map);
			 String[] headers = {"Content-Type#application/json","Accept#application/json;charset=utf-8"};
			 System.out.println("Paramentro de Envio");
			 System.out.println(urlParameters);
			 StringBuffer pagamento = sendPostGet("POST", url, urlParameters, headers);
			 System.out.println("Retorno do PAYU");
			 System.out.println(pagamento);

			 json = new JSONObject(pagamento.toString());
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
		
	}
	
	public static byte[] gerarHash(String frase, String algoritmo) {
		  try {
		    MessageDigest md = MessageDigest.getInstance(algoritmo);
		    md.update(frase.getBytes());
		    return md.digest();
		  } catch (NoSuchAlgorithmException e) {
		    return null;
		  }
		}
	
	private static String stringHexa(byte[] bytes) 
		{
		   StringBuilder s = new StringBuilder();
		   for (int i = 0; i < bytes.length; i++) {
		       int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
		       int parteBaixa = bytes[i] & 0xf;
		       if (parteAlta == 0) s.append('0');
		       s.append(Integer.toHexString(parteAlta | parteBaixa));
		   }
		   return s.toString();
		}
		
	public static String capturarParametros(Pedido pedido, String tipo, Map<String, String> map){
		
		String referenceCode = pedido.getNumero();
		
		String amount = getTotalPedido(pedido).toString();
		String apiKey = "CCZCKJn3TMUOb9hKJwCwVUVK2E";
		String cpfTeste = "10792984790";
		String merchantBuyerId = "576002";
		String accountId = "578808";
		InetAddress ip = null;
		
		
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String validade=null;
		String cpf = null;
		
		if(tipo.equals("cartao"))
		{
			String val = map.get("validade");
			
			String array[] = new String[3];
			array = val.split("/");
			validade = array[1] +"/"+array[0];			
			cpf = map.get("cpfCnpjHolder").replaceAll("[^0-9]", "");
		} 
		else if (tipo.equals("boleto"))
		{
			cpf = pedido.getCliente().getCpfCnpj();
		}
		
		String ass = stringHexa(gerarHash(apiKey+"~576002~"+referenceCode+"~"+amount+"~BRL", "MD5"));
		
		JSONObject parametros = new JSONObject();		
			parametros.put("language","pt");
			parametros.put("command","SUBMIT_TRANSACTION");
		
			JSONObject merchant = new JSONObject();
				merchant.put("apiKey",apiKey);
				merchant.put("apiLogin","465kWF7zi3qGQNo");
			
			
		  
			JSONObject transaction = new JSONObject();
				JSONObject order = new JSONObject();
					order.put("accountId",accountId);
					order.put("description","Pedido "+pedido.getNumero());
					order.put("referenceCode",referenceCode);					
					order.put("language","pt");
					order.put("signature",ass);
					order.put("notifyUrl","http://climariopagamentos.com.br/status");
					
					JSONObject additionalValues = new JSONObject();
						JSONObject TX_VALUE = new JSONObject();
							TX_VALUE.put("value",amount);
							TX_VALUE.put("currency","BRL");
						
					
					JSONObject buyer = new JSONObject();
						
						buyer.put("emailAddress",pedido.getCliente().getEmail());
						buyer.put("dniNumber",cpf);
						//buyer.put("dniNumber",cpfTeste);						
						
						JSONObject shippingAddress = new JSONObject();
						shippingAddress.put("street1",pedido.getCliente().getLogradouro());
						shippingAddress.put("street2",pedido.getCliente().getNumero());
						shippingAddress.put("city", Normalizer.normalize(pedido.getCliente().getCidade(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
						shippingAddress.put("state",pedido.getCliente().getEstado());
						shippingAddress.put("country","BR");
						shippingAddress.put("postalCode",pedido.getCliente().getCep());
					
					
				transaction.put("order",order);				
				transaction.put("type","AUTHORIZATION_AND_CAPTURE");
				transaction.put("paymentMethod","BOLETO_BANCARIO");
				transaction.put("paymentCountry","BR");				
				transaction.put("ipAddress",ip.getHostAddress());
				
			
			parametros.put("test",false);
			
		if(tipo.equals("boleto"))
		{
			buyer.put("fullName",pedido.getCliente().getNome());
			String data=null;
			Date dataDoUsuario = new Date();

			// Através do Calendar, trabalhamos a data informada e adicionamos 1 dia nela
			Calendar c = Calendar.getInstance();
			c.setTime(dataDoUsuario);
			c.add(Calendar.DATE, +5);

			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			data = format1.format(c.getTime());
			
			
			System.out.println("----------xxx-------");
			System.out.println(data);
			System.out.println("------xxx-----------");
			transaction.put("expirationDate",data);
			
		}
		if(tipo.equals("cartao"))
		{
			buyer.put("fullName",map.get("nome"));
			buyer.put("merchantBuyerId",pedido.getCliente().getId());
			buyer.put("contactPhone",map.get("dddHolder")+map.get("telefoneHolder"));
			shippingAddress.put("phone",map.get("dddHolder")+map.get("telefoneHolder"));	
			
			JSONObject creditCard = new JSONObject();
				creditCard.put("number",map.get("numeroCartao"));
				creditCard.put("securityCode",map.get("cvv"));
				creditCard.put("expirationDate",validade);
				creditCard.put("name",map.get("nome"));				
			transaction.put("creditCard",creditCard);
			
			JSONObject extraParameters = new JSONObject();
				extraParameters.put("INSTALLMENTS_NUMBER",map.get("parcelas_input"));
			transaction.put("extraParameters",extraParameters);
			
			transaction.put("paymentMethod",map.get("basic_input"));
			
			/*try {		
				 String url = "https://www.binlist.net/json/"+map.get("numeroCartao").substring(0,6);
				 String urlParameters = "";
				 String[] headers = {"Content-Type#application/x-www-form-urlencoded"}; 
				 StringBuffer band = sendPostGet("POST", url, urlParameters, headers);

				 JSONObject bandeira = new JSONObject(band.toString());
				 
				 System.out.println("---------------------");
				 System.out.println(bandeira);
				 System.out.println(bandeira.get("brand").toString());
				 System.out.println("---------------------");
				 
				 transaction.put("paymentMethod",bandeira.get("brand").toString());
					
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
			
			
		}
		
		parametros.put("merchant",merchant);
		additionalValues.put("TX_VALUE",TX_VALUE);
		order.put("additionalValues",additionalValues);
		buyer.put("shippingAddress",shippingAddress);				
		order.put("buyer",buyer);
		parametros.put("transaction",transaction);
		return parametros.toString();
	}
	
	public static JSONObject solicitarToken() {

		JSONObject jsonObj = null;
		
		try {		
			 String url = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";
			 String urlParameters = "grant_type=client_credentials&client_id=300046&client_secret=c8d4b7ac61758704f38ed5564d8c0ae0";
			 String[] headers = {"Content-Type#application/x-www-form-urlencoded"}; 
			 
			 StringBuffer token = sendPostGet("POST", url, urlParameters, headers);
			 jsonObj = new JSONObject(token.toString());
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	}
	
	
	public static JSONObject solicitarPagamentos(String tokenType, String access_token) {
		
		JSONObject jsonObj = null;
		
		try {		
			 String url = "https://api.payulatam.com/payments-api/4.0/service.cgi";
			 String urlParameters = "{   'test': false,   'language': 'pt',   'command': 'GET_PAYMENT_METHODS',   'merchant': {      'apiLogin': '465kWF7zi3qGQNo',      'apiKey': 'CCZCKJn3TMUOb9hKJwCwVUVK2E'   }}";
			 String[] headers = {"Content-Type#application/json","Accept#application/json"};
			 StringBuffer metodos = sendPostGet("POST", url, urlParameters, headers);

			 jsonObj = new JSONObject(metodos.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	}

	 // HTTP POST request
	 private static StringBuffer sendPostGet(String method, String url, String urlParameters, String[] headers) throws Exception 
	 {

		  URL obj = new URL(url);
		  HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
		  con.setRequestMethod(method);
		  if (headers.length > 0)
		  {
			  for(int x=0; x < headers.length; x++)
			  {
				  String h[] = headers[x].split("#");
				  con.setRequestProperty(h[0], h[1]);
			  }
		  }
		  JSONObject jsonObj = null;
		  
		  if(method == "POST")
		  {
			  con.setDoOutput(true);
			  DataOutputStream write = new DataOutputStream(con.getOutputStream());
			  BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(write, "UTF-8"));
			  try{
				  jsonObj = new JSONObject(urlParameters);	
				  wr.write(jsonObj.toString());
				  //System.out.println(jsonObj);
			  }catch (Exception e) {
					//e.printStackTrace();
				  wr.write(urlParameters);
				}
			  
			  // Send post request
			 
			  wr.flush();
			  wr.close();
		  }
		  int responseCode = con.getResponseCode();
	
		  BufferedReader in = new BufferedReader(
		          			  new InputStreamReader(con.getInputStream()));
		  String inputLine;
		  StringBuffer response = new StringBuffer();
	
		  while ((inputLine = in.readLine()) != null) 
		  {
		   response.append(inputLine);
		  }
		  in.close();
	
		  return response;

	 }
	 
}
