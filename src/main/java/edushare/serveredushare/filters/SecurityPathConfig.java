package edushare.serveredushare.filters;

import org.springframework.stereotype.Component;
import java.util.List;

/// Componente da iniettare nei filtri per la gestione dei path pubblici
@Component
public class SecurityPathConfig {

	// Unica lista centralizzata dei path pubblici (PREFIXES)
	private static final List<String> PUBLIC_PREFIXES = List.of(
			"/session"
	);

	// EXACT MATCH (Pubblici solo se coincidono esattamente, es. /corsi ma NON /corsi/seguiti)
	private static final List<String> PUBLIC_EXACT_MATCHES = List.of(
			"/corsi",
			"/corsi/materie",
			"/corsi/difficolta",
			"/corsi/maxCosto"
	);

	private static final List<String> MODIFY_TEACHER_FIELDS_PATHS = List.of(
			"/modify_data/aboutMe",
			"/modify_data/titoliStudio"

	);

	/**
	 * Controlla se il path richiesto è pubblico.
	 */
	public boolean isPublic(String requestPath) {
		// Ritorna true se il path inizia con una delle stringhe nella whitelist
		return PUBLIC_PREFIXES.stream().anyMatch(requestPath::startsWith) || PUBLIC_EXACT_MATCHES.stream().anyMatch(requestPath::equals);
	}

	/**
	 * Controlla se il path richiesto è riservato agli insegnanti
	 */
	public boolean isModifyTeacherFields(String requestPath) {
		return MODIFY_TEACHER_FIELDS_PATHS.stream().anyMatch(requestPath::startsWith);
	}
}