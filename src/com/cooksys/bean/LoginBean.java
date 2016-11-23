package com.cooksys.bean;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.model.User;
import com.cooksys.model.UserModel;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Component
@Scope("session")
public class LoginBean {

	@Autowired
	Credential credential;
	
	@Autowired
	SignupBean signupBean;
	
	@Autowired
	SearchBean searchBean;

	Logger log = LoggerFactory.getLogger(LoginBean.class);
	
	private boolean login;
	private boolean loginValidationComplete = false;
	private UserModel user;
	private UserModel editUser;

	/**
	 * Send Web service username and password 
	 * @return
	 */
	public String loginService() {

		Client client = Client.create();

		MultivaluedMap formData = new MultivaluedMapImpl();

		formData.add("username", credential.getUsername());
		formData.add("password", credential.getPassword());

		WebResource webResource2 = client
				.resource("http://localhost:8080/FinalFlightService/login");
		ClientResponse response2 = webResource2.accept(
				MediaType.APPLICATION_XML).post(ClientResponse.class, formData);
		
		if (response2.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response2.getStatus());
		}

		UserModel tempUser = response2.getEntity(UserModel.class);

		if (tempUser.getUsername() != null) 
		{
			login = true;
			user = tempUser;
			editUser = tempUser;
			loginValidationComplete = true;
			return "success";
		} else {
			loginValidationComplete = true;
			return "fail";
		}

	}

	/**
	 * Logs User out
	 * @return
	 */
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
		user = null;
		editUser = null;
		loginValidationComplete = false;
		login = false;
		log.info("TRYING TO LOG OUT");
		return "logout";
	}
	
	/**
	 * Send Web Service The User's edited user information
	 * @return
	 */
	public String editProfile(){
		Client client = Client.create();

		WebResource webResource2 = client
				.resource("http://localhost:8080/FinalFlightService/edit");
		ClientResponse response2 = webResource2.accept(
				MediaType.APPLICATION_XML).post(ClientResponse.class, editUser);

		if (response2.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response2.getStatus());
		}
		
		UserModel tempUser = response2.getEntity(UserModel.class);
		
		if(tempUser == null){
			log.info("edit is null");
			return "fail";
		}
		else if(!tempUser.equals(user)){
			user = tempUser;
			log.info("Edit success");
			return "success";
		}
		log.info("Edit fail");
		return "fail";
	}

	/**
	 * Takes client to signup page
	 * @return
	 */
	public String goToSignupPage() {
		signupBean.signupValidationComplete = false;
		return "signup";
	}
	
	/**
	 * Takes User to Edit Page
	 * @return
	 */
	public String goToEditPage(){
		searchBean.getBookedFlightPaths();
		return "edit";
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public boolean isLoginValidationComplete() {
		return loginValidationComplete;
	}

	public void setLoginValidationComplete(boolean loginValidationComplete) {
		this.loginValidationComplete = loginValidationComplete;
	}

	public UserModel getEditUser() {
		return editUser;
	}

	public void setEditUser(UserModel editUser) {
		this.editUser = editUser;
	}
}
