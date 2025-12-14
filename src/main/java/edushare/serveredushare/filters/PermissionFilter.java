package edushare.serveredushare.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/// Filtro per i permessi in base alla tipologia di utente
@Component
@Order(2)
public class PermissionFilter implements Filter {
	private final SecurityPathConfig securityPathConfig;

	// Spring inietta automaticamente il componente qui
	public PermissionFilter(SecurityPathConfig securityPathConfig) {
		this.securityPathConfig = securityPathConfig;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();

		// Se la route è pubblica, mando avanti la richiesta a prescindere
		if (securityPathConfig.isPublic(request.getServletPath())) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// TODO: Aggiungere condizioni basate sui permessi dell'utente

		// Errore: l'utente loggato non ha i permessi
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
