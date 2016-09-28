
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class AutorizaECapturaPagamentoRequest {

    private String language;
    private String command;
    private Merchant merchant;
    private Transaction transaction;
    private Boolean test;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     *     The command
     */
    public String getCommand() {
        return command;
    }

    /**
     * 
     * @param command
     *     The command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * 
     * @return
     *     The merchant
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * 
     * @param merchant
     *     The merchant
     */
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * 
     * @return
     *     The transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * 
     * @param transaction
     *     The transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * 
     * @return
     *     The test
     */
    public Boolean getTest() {
        return test;
    }

    /**
     * 
     * @param test
     *     The test
     */
    public void setTest(Boolean test) {
        this.test = test;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
