package edushare.serveredushare.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/// Filtro per le route di login
@Component
@Order(1)
public class LoginFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse res = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute("username");

		// Le preflight requests passano sempre (per permettere il cross origin)
		if (request.getMethod().equals("OPTIONS")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Se l'utente è autenticato, mando avanti la richiesta
		if (user != null) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Se l'utente non è autenticato, ma sta tentando di autenticarsi, mando avanti la richiesta
		String pathname = request.getServletPath();
		if (pathname.startsWith("/session")) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
