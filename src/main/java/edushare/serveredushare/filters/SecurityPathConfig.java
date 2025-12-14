package edushare.serveredushare.filters;

import org.springframework.stereotype.Component;
import java.util.List;

/// Componente da iniettare nei filtri per la gestione dei path pubblici
@Component
public class SecurityPathConfig {

	// Unica lista centralizzata dei path pubblici
	private static final List<String> PUBLIC_PATHS = List.of(
			"/session"
	);

	/**
	 * Controlla se il path richiesto è pubblico.
	 */
	public boolean isPublic(String requestPath) {
		// Ritorna true se il path inizia con una delle stringhe nella whitelist
		return PUBLIC_PATHS.stream().anyMatch(requestPath::startsWith);
	}
}