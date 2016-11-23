package com.cooksys.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator{
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		//Grabs password input for id=password
		String pass = value.toString();

		//Grabs confirm password input for id=confirmPassword
		UIInput uiInputConfirmPassword = (UIInput) component.getAttributes().get("confirmPassword");
		String confirmPassword = uiInputConfirmPassword.getSubmittedValue().toString();
		
		// Let required="true" do its job.
		if (pass == null || pass.isEmpty() || confirmPassword == null
				|| confirmPassword.isEmpty()) {
			return;
		}

		if (!pass.equals(confirmPassword)) {
			uiInputConfirmPassword.setValid(false);
			throw new ValidatorException(new FacesMessage(
					"Password must match confirm password."));
		}

	}

}
