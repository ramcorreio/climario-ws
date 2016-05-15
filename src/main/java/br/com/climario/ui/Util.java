package br.com.climario.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Util {

	private static transient ResourceBundle bundle = ResourceBundle.getBundle("pagseguro", new UTF8Control());	
	
	public static String criptografarString(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest(input.getBytes("UTF-8"));
		 
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
		  hexString.append(String.format("%02X", 0xFF & b));
		}

		return hexString.toString();
	}

	public static void successMsg(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
	}

	public static void warnMsg(String msg, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, detail));
	}
	
	public static void warnMsg(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
	}
	
	public static void errorMsg(String msg, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, detail));
	}
	
	public static void errorMsg(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
	}
	
	public static void infoMsg(String msg, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, detail));
	}
	
	public static void fatalMsg(String msg, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, detail));
	}
	
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
	
	public static <T> T getSession(String key, Class<T> type) {
		return type.cast(getSession().getAttribute(key));
		//return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
	
	public static <T> HttpSession setSession(String key, T value) {
		HttpSession session = getSession();
		session.setAttribute(key, value);
		return session;
	}
	
	public static String getContextRoot() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		/*String url = request.getHeader("Host").toString() + request.getContextPath().toString();
		if(url.indexOf("10.21.7.9") >= 0){
			return "localhost:8080"+request.getContextPath().toString();
		}*/
		return "http://"+request.getHeader("Host").toString() + request.getContextPath().toString();
	}
	
	public static String getContextRoot(String pathToAdd) {
		return getContextRoot() + pathToAdd;
	}
	
	public static void removeSession(String key) {
		getSession().removeAttribute(key);
	}

	public static String getRequestParam(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
	}
	
	public static Integer getRequestParamAsInteger(String key) {
		Integer rs = null;
		String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
		if(value != null){
			rs = new Integer(value);
		}
		return rs;
	}
	
	public static HttpServletResponse getResponse(){
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	
	public static String[] getRequestParams(String key) {
		HttpServletRequest r = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
		return r.getParameterValues(key);
	}
	
	public static String getMessageBundle(String msgKey, String... params ) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String messageBundleName = facesContext.getApplication().getMessageBundle();
		Locale locale = facesContext.getViewRoot().getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
		String  msgValue = bundle.getString(msgKey);
		MessageFormat messageFormat = new MessageFormat(msgValue);
		ArrayList<String> args = new ArrayList<String>();
		for (String param : params) {
			args.add(param);
		}
	    return messageFormat.format(args.toArray());
	}
	
	public static <T> SelectItem[] createFilterOptions(List<T> list, String key, String value){
		Field keyField;
		Field valueField;
		SelectItem[] options = new SelectItem[list.size() + 1];
		options[0] = new SelectItem("", "Selecione");
		int i = 1;
		try {
			for(T item : list){
				keyField = item.getClass().getDeclaredField(key);
				keyField.setAccessible(true);
				
				valueField = item.getClass().getDeclaredField(value);
				valueField.setAccessible(true);
				
				options[i] = new SelectItem(keyField.get(item).toString(), valueField.get(item).toString());
				i++;
			}
			
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        /*SelectItem[] options = new SelectItem[data.length + 1];  
  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < data.length; i++) {  
            options[i + 1] = new SelectItem(data[i], data[i]);  
		return options;  
        }*/  
		return options;
    }

	public static Timestamp dateToTimestamp(Date launchDate) {
		return new Timestamp(launchDate.getTime());
	}

	public static Object getAttributeParam(String string) {
		return UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get(string);
	}
	
	public static Integer getAttributeParamAsInteger(String string) {
		return (Integer) FacesContext.getCurrentInstance().getAttributes().get(string);
	}

	public static Integer getDaysInMonth(int month, int year) {
		Calendar mycal = new GregorianCalendar(year, month, 1);
		return Integer.valueOf(mycal.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	public static Calendar getDate(int year, int month, int date) {
		Calendar c;
		c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, date);
		c.get(Calendar.DAY_OF_WEEK);
		c.getTime();
		return c;
	}
	
	/*public static String PaddingWithZero(String string, Integer size, int position) {
		String stringAux = new String(string);
		if(position == Util.PADDING_LEFT){
			while(stringAux.length() < size){
				stringAux = "0" + stringAux;
			}
		}
		else if(position == Util.PADDING_RIGHT){
			while(stringAux.length() < size){
				stringAux = stringAux + "0";
			}
		}
		return stringAux;
	}*/
	
	public static String bigDecimalToString(Double amountTemp) {
		return amountTemp.toString().replaceAll("\\.", ",");
	}
	
	public static String bigDecimalToString(BigDecimal amountTemp) {
		return amountTemp.toString().replaceAll("\\.", ",");
	}
	
	public static BigDecimal stringToBigDecimal(String amountTemp) {
		return new BigDecimal(amountTemp.replaceAll("\\.", "X").replaceAll(",", ".").replaceAll("X", ""));
	}
	
	public static Double stringToBigDouble(String amountTemp) {
		return new Double(amountTemp.replaceAll("\\.", "X").replaceAll(",", ".").replaceAll("X", ""));
	}
	
	public static String dateToString(Date date, String format) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(date);
		} else {
			return "";
		}
	}

	public static String secondsToTimeString(Long seconds) {
		String strHour = "00";
		String strMinutes = "00";
		
		Long minutes = seconds/60;
		Long hour = Double.valueOf(minutes/60).longValue();
		strHour = hour.toString();
		if(hour < 10){
			strHour = "0" + hour;
		}
		minutes = Double.valueOf(minutes % 60).longValue();
		strMinutes = minutes.toString();
		if(minutes < 10){
			strMinutes = "0" + minutes;
		}
		return strHour + ":" + strMinutes;
	}
	
	public static void redirectByContext(String redirectTo){
		
		redirect(getContextRoot(redirectTo));
	}

	public static void redirect(String redirectTo){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(redirectTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void invalidateSession() {
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); 
		
	}

	public static String getCurrentPage() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
	}	
	
	public static String getProperties(String key){
		return getProperties().getProperty(key);
	}
	
	public static Properties getProperties(){
		Properties prop = new Properties();
		try {
			prop.load(Util.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prop;
	}
	
	public static String getString(String key) {
		
		return bundle.getString(key);
	}
	
	public static String getString(String key, Object ...args) {
		
		return MessageFormat.format(getString(key), args);
	}
	
	public static void main(String[] args) {
		sendMail("ramcorreio@yahoo.com.br", "Assunto", "CORPO EMAIL", null);
	}
	
	public static Boolean sendMail(String emailTo, String subject, String txt){
    	return sendMail(emailTo, subject, txt, null);
    }
    
    public static Boolean sendMail(String emailTo, String subtitle, String txt, AnexoEmail anexo) {
    	
		Boolean rs = true;
		// Recipient's email ID needs to be mentioned.
		String to = emailTo;

		// Sender's email ID needs to be mentioned
		// String from = "";
		// String from =
		// configService.findValueByKey(Constants.CONFIG_SYSTEM_MAIL);
		final String from = getString("email.sender.account");
		final String fromName = getString("email.sender.label");
		//final String password = Constants.EMAIL_B2B_CONTATO_PASS;
		//final String mailBody = "";

		// Assuming you are sending email from localhost
		// String host = "";
		// String host =
		// configService.findValueByKey(Constants.CONFIG_SMTP_HOST);
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", host);
		
		// Create a default MimeMessage object.
		//MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));
		
		//if(!host.equals("localhost"))
		/*properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");*/

		/*Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});*/
			
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);

		try {
			
			Message message = new MimeMessage(session);
			message.addHeader("Content-Type", "text/html");
			message.addHeader("charset", "utf-8");

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, fromName));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subtitle);

			// Now set the actual message
			// message.setText(txt);
			//message.setContent(mailBody.replaceFirst("\\[=TXT=]", txt), "text/html; charset=utf-8");
			
			if(anexo == null){
				message.setContent(txt, "text/html; charset=utf-8");
			}else{
				Multipart multipart = new MimeMultipart();
			    MimeBodyPart messageBodyPart = new MimeBodyPart();
			    messageBodyPart.setText(txt, "utf-8", "html");
			    multipart.addBodyPart(messageBodyPart);
	
			    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			    ByteArrayDataSource ds = new ByteArrayDataSource(anexo.getArquivo(), anexo.getContentType().getBaseType()); 
			    attachmentBodyPart.setDataHandler(new DataHandler(ds));
			    attachmentBodyPart.setFileName(anexo.getNomeArquivo());
			    multipart.addBodyPart(attachmentBodyPart);
			    message.setContent(multipart);
			}
		    
			//message.setContent(txt, "text/html");

			// Send message
			Transport.send(message);
			System.out.println("envio ok para " + message);
		} catch (MessagingException mex) {
			rs = false;
			System.out.println("erro de envio");
			mex.printStackTrace();
		}
		catch (Exception e) {
			rs = false;
			System.out.println("erro de envio");
			e.printStackTrace();
		}
		return rs;
	}
}
