
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class AdditionalValues {

    private TXVALUE tXVALUE;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The tXVALUE
     */
    public TXVALUE getTXVALUE() {
        return tXVALUE;
    }

    /**
     * 
     * @param tXVALUE
     *     The TX_VALUE
     */
    public void setTXVALUE(TXVALUE tXVALUE) {
        this.tXVALUE = tXVALUE;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
