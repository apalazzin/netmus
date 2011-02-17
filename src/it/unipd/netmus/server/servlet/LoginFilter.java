package it.unipd.netmus.server.servlet;

import it.unipd.netmus.server.LoginHelper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("Distrutto filtro");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	    
	    System.out.println("Filtraggio in corso");
		
	    try {
	      HttpServletRequest req = (HttpServletRequest) request;
	      
	      if (LoginHelper.isLoggedIn(req)) {
	        System.out.println("Utente loggato: accesso consentito");
	        chain.doFilter(request, response);
	      } else {
	          System.out.println("Utente non loggato: accesso non consentito");
	          HttpServletResponse resp = (HttpServletResponse) response;
	          resp.sendRedirect("/");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	    System.out.println("Creato filtro");
	}

}
