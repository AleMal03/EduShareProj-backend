package edushare.serveredushare.filters;

import edushare.serveredushare.DTO.UserDto;
import edushare.serveredushare.persistence.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/// Filtro per i permessi in base alla tipologia di utente (blackList)
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
		UserDto user = (UserDto) session.getAttribute("user");

		// Se la route è pubblica, mando avanti la richiesta a prescindere
		if (securityPathConfig.isPublic(request.getServletPath())) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// Se cerca di modificare dati da insegnante se non è insegnante (blacklisted)
		if(securityPathConfig.isModifyTeacherFields(request.getServletPath()) && !user.getRuoli().contains(User.Role.TEACHER)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);   // Errore: l'utente loggato non ha i permessi
		}

		// Altrimenti vai avanti
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
