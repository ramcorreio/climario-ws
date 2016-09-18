
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class Transaction {

    private Order order;
    private CreditCard creditCard;
    private ExtraParameters extraParameters;
    private String type;
    private String paymentMethod;
    private String paymentCountry;
    private String ipAddress;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 
     * @return
     *     The creditCard
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * 
     * @param creditCard
     *     The creditCard
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * 
     * @return
     *     The extraParameters
     */
    public ExtraParameters getExtraParameters() {
        return extraParameters;
    }

    /**
     * 
     * @param extraParameters
     *     The extraParameters
     */
    public void setExtraParameters(ExtraParameters extraParameters) {
        this.extraParameters = extraParameters;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 
     * @param paymentMethod
     *     The paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 
     * @return
     *     The paymentCountry
     */
    public String getPaymentCountry() {
        return paymentCountry;
    }

    /**
     * 
     * @param paymentCountry
     *     The paymentCountry
     */
    public void setPaymentCountry(String paymentCountry) {
        this.paymentCountry = paymentCountry;
    }

    /**
     * 
     * @return
     *     The ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 
     * @param ipAddress
     *     The ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
