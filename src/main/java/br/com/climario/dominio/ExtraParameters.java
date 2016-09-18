
package br.com.climario.dominio;

import java.util.HashMap;
import java.util.Map;

public class ExtraParameters {

    private Integer iNSTALLMENTSNUMBER;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The iNSTALLMENTSNUMBER
     */
    public Integer getINSTALLMENTSNUMBER() {
        return iNSTALLMENTSNUMBER;
    }

    /**
     * 
     * @param iNSTALLMENTSNUMBER
     *     The INSTALLMENTS_NUMBER
     */
    public void setINSTALLMENTSNUMBER(Integer iNSTALLMENTSNUMBER) {
        this.iNSTALLMENTSNUMBER = iNSTALLMENTSNUMBER;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
