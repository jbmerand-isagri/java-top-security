package dev.filters;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.utils.HmacUtils;

@WebFilter("/top/*")
public class AuthFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					System.out.println("Vérification du cookie d'authentification...(filter)");
					String value = cookie.getValue();
					byte[] decodedBytes = Base64.getDecoder().decode(value);
					String dataAndSignature = new String(decodedBytes);
					String data = dataAndSignature.split("\\|")[0];
					System.out.println("data pour authentification = " + data);

					String signature = HmacUtils.CreateSignature("secretSecret", data);
					if (signature.equals(dataAndSignature.split("\\|")[1])) {
						System.out.println("... vérification réussie");
						filterChain.doFilter(servletRequest, servletResponse);
					} else {
						System.out.println("Vérification échouée");
						resp.sendRedirect(req.getContextPath() + "/login");
					}
				}
			}
		}
	}

	@Override
	public void destroy() {

	}
}
