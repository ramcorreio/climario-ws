
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class Buyer {

    private String merchantBuyerId;
    private String fullName;
    private String emailAddress;
    private String contactPhone;
    private String dniNumber;
    private String cnpj;
    private ShippingAddress shippingAddress;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The merchantBuyerId
     */
    public String getMerchantBuyerId() {
        return merchantBuyerId;
    }

    /**
     * 
     * @param merchantBuyerId
     *     The merchantBuyerId
     */
    public void setMerchantBuyerId(String merchantBuyerId) {
        this.merchantBuyerId = merchantBuyerId;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * 
     * @param emailAddress
     *     The emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * 
     * @return
     *     The contactPhone
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 
     * @param contactPhone
     *     The contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * 
     * @return
     *     The dniNumber
     */
    public String getDniNumber() {
        return dniNumber;
    }

    /**
     * 
     * @param dniNumber
     *     The dniNumber
     */
    public void setDniNumber(String dniNumber) {
        this.dniNumber = dniNumber;
    }

    /**
     * 
     * @return
     *     The cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * 
     * @param cnpj
     *     The cnpj
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * 
     * @return
     *     The shippingAddress
     */
    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    /**
     * 
     * @param shippingAddress
     *     The shippingAddress
     */
    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
