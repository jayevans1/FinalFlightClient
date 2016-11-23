package com.cooksys.bean;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.model.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Component
@Scope("request")
public class SignupBean {

	@Autowired
	Credential credential;
	
	@Autowired
	LoginBean loginBean;

	boolean signupValidationComplete = false;
	
	/**
	 * Register a new User 
	 * @return
	 */
	public String signup() {
		User newUser = new User(credential.getUsername(),
				credential.getPassword(), credential.getFirstname(),
				credential.getLastname(), credential.getEmail(),
				credential.getStreet(), credential.getCity(),
				credential.getState());
		
		Client client = Client.create();
		
		WebResource webResource2 = client
				.resource("http://localhost:8080/FinalFlightService/signup");
		ClientResponse response2 = webResource2.accept(MediaType.APPLICATION_XML).post(ClientResponse.class, newUser);
		
		String isSignupSuccess = response2.getEntity(String.class);
		
		if(isSignupSuccess.equals("success")){
			signupValidationComplete = true;
			loginBean.setLoginValidationComplete(false);
			return "login";
		}
		signupValidationComplete = true;
		return "fail";
	}
	
	/**
	 * Takes User back to login page
	 * @return
	 */
	public String goToLoginPage(){
		signupValidationComplete = false;
		loginBean.setLoginValidationComplete(false);
		return "login";
	}

	public boolean isSignupValidationComplete() {
		return signupValidationComplete;
	}

	public void setSignupValidationComplete(boolean signupValidationComplete) {
		this.signupValidationComplete = signupValidationComplete;
	}
}
