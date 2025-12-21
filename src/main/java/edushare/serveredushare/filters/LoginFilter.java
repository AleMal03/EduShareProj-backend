package edushare.serveredushare.filters;

import edushare.serveredushare.DTO.UserDto;
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
	private final SecurityPathConfig securityPathConfig;

	// Spring inietta automaticamente il componente qui
	public LoginFilter(SecurityPathConfig securityPathConfig) {
		this.securityPathConfig = securityPathConfig;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		UserDto user = (UserDto) session.getAttribute("user");

		// Se l'utente non è autenticato, ma la route è pubblica, mando comunque avanti la richiesta
		if (securityPathConfig.isPublic(request.getServletPath())) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Se l'utente è autenticato in questa sessione, mando avanti la richiesta
		if (user != null) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Errore: utente non riconosciuto (non loggato)
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
