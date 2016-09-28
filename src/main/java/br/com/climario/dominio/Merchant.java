
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class Merchant {

    private String apiKey;
    private String apiLogin;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * 
     * @param apiKey
     *     The apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 
     * @return
     *     The apiLogin
     */
    public String getApiLogin() {
        return apiLogin;
    }

    /**
     * 
     * @param apiLogin
     *     The apiLogin
     */
    public void setApiLogin(String apiLogin) {
        this.apiLogin = apiLogin;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
