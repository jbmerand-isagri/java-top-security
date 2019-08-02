package dev.controllers.auth;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.domains.User;
import dev.services.LoginService;
import dev.services.ServicesFactory;
import dev.utils.HmacUtils;

@WebServlet("/login")
public class LoginCrl extends HttpServlet {

	private LoginService loginService = ServicesFactory.LOGIN_SERVICE;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		String password = req.getParameter("pass");

		Optional<User> userOpt = loginService.connect(login, password);

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			String message = user.getFirstname() + ";" + user.getLastname() + ";" + user.getLogin() + ";"
					+ user.getAdmin();
			String signature = HmacUtils.CreateSignature("secretSecret", message);

			System.out.println("signature : " + signature);

			String value = Base64.getEncoder().encodeToString((message + "|" + signature).getBytes());

			Cookie cookie = new Cookie("user", value);
			cookie.setHttpOnly(true);
			resp.addCookie(cookie);
			req.getSession().setAttribute("connectedUser", user);
			resp.sendRedirect(req.getContextPath() + "/users/list");
		} else {
			req.setAttribute("errors", "les informations fournies sont incorrectes");
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
		}
	}
}
