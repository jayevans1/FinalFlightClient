package com.cooksys.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.cooksys.bean.LoginBean;

public class LoggedUserFilter extends GenericFilterBean {
	@Autowired
	LoginBean loginBean;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse)res;
		HttpServletRequest request = (HttpServletRequest) req;
		
		if (loginBean.isLogin()) {
			response.sendRedirect("/FinalFlightClient/secure/SearchPage.xhtml");
			chain.doFilter(request, response);
		}else {
			chain.doFilter(request, response);
		}
	}
}
