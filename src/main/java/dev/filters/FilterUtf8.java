package dev.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class FilterUtf8 implements Filter {

	public FilterUtf8() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("passage par le filtre utf8");
		// Respect the client-specified character encoding
		// (see HTTP specification section 3.4.1)
		request.setCharacterEncoding("UTF-8");
		// Set the default response content type and encoding
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}