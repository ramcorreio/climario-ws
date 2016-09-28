
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private Integer accountId;
    private String referenceCode;
    private String description;
    private String language;
    private String signature;
    private String notifyUrl;
    private AdditionalValues additionalValues;
    private Buyer buyer;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * 
     * @param accountId
     *     The accountId
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * 
     * @return
     *     The referenceCode
     */
    public String getReferenceCode() {
        return referenceCode;
    }

    /**
     * 
     * @param referenceCode
     *     The referenceCode
     */
    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language
     *     The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 
     * @return
     *     The signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 
     * @param signature
     *     The signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 
     * @return
     *     The notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * 
     * @param notifyUrl
     *     The notifyUrl
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * 
     * @return
     *     The additionalValues
     */
    public AdditionalValues getAdditionalValues() {
        return additionalValues;
    }

    /**
     * 
     * @param additionalValues
     *     The additionalValues
     */
    public void setAdditionalValues(AdditionalValues additionalValues) {
        this.additionalValues = additionalValues;
    }

    /**
     * 
     * @return
     *     The buyer
     */
    public Buyer getBuyer() {
        return buyer;
    }

    /**
     * 
     * @param buyer
     *     The buyer
     */
    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
