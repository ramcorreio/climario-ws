package br.com.climario.ui;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

@FacesValidator("custom.numberValidator")
public class NumberValidator implements Validator, ClientValidator {

	private Pattern pattern;

	private static final String NUMBER_PATTERN = "\\d+";

	public NumberValidator() {
	        pattern = Pattern.compile(NUMBER_PATTERN);
	    }

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		if (!pattern.matcher(value.toString()).matches()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", value + " não é número."));
		}
	}

	public Map<String, Object> getMetadata() {
		return null;
	}

	public String getValidatorId() {
		return "custom.numberValidator";
	}

}
