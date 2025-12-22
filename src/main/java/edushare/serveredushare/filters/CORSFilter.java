package edushare.serveredushare.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)  // Richiamo il filtro per il cross origin prima di tutti
public class CORSFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// Imposta gli header CORS per permettere al browser di accettare DIRETTAMENTE la richiesta
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
		response.setHeader("Access-Control-Allow-Credentials", "true"); // Necessario se usi i cookie/sessioni
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization");

		// Gestione delle richieste preflight (OPTIONS)
		if (request.getMethod().equals("OPTIONS")) {
			// Se è un controllo preflight, rispondiamo OK e fermiamo la catena qui.
			// Non occorre mandare avanti la richiesta nella filter chain
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			// Se non è OPTIONS, la richiesta prosegue verso LoginFilter (Order 1)
			chain.doFilter(req, res);
		}
	}
}
