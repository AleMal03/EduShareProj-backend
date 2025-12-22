package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.UserDto;
import edushare.serveredushare.DTO.UserDtoMapper;
import edushare.serveredushare.services.UserService;
import edushare.serveredushare.session.SessionData;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


/// Controller delle route di modifica dati utente
@RestController
@RequestMapping("/modify_data")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")      // Per cross-origin (solo fase di dev)
public class ModificaDatiUserController {
	private final UserService userService;

	ModificaDatiUserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/email")
	public ResponseEntity<SessionData> changeEmail(HttpSession session, @RequestBody Map<String, String> reqBody){
		String newEmail = reqBody.get("data");
		UserDto user = (UserDto) session.getAttribute("user");

		// Controllo di sicurezza: se la sessione è scaduta o non esiste
		if (user == null) {
			return ResponseEntity.status(401).body(new SessionData(null, "Sessione scaduta."));
		}

		try{
			boolean success = userService.changeUserEmail(user.getUsername(), newEmail);
			if (success) {
				// 1. RECUPERO l'utente aggiornato dal DB per avere il DTO fresco
				UserDto updatedUser = UserDtoMapper.map(userService.getUserByUsername(user.getUsername()));

				// 2. AGGIORNO la sessione
				session.setAttribute("user", updatedUser);

				// 3. RESTITUISCO i dati aggiornati al frontend
				return ResponseEntity.ok(new SessionData(updatedUser, "Email aggiornata con successo."));
			}
			else {
				// Se ritorna false senza lanciare eccezioni (es. parametri nulli)
				return ResponseEntity.badRequest().body(new SessionData(user, "Dati non validi."));
			}
		} catch (IllegalArgumentException | IllegalStateException e) {
			// CATCH 1: Errori di logica previsti (es. email duplicata, formato errato) -> 422
			return ResponseEntity.unprocessableContent().body(new SessionData(user, e.getMessage()));
		} catch (Exception e) {
			// CATCH 2: Errori imprevisti (es. DB down) -> 500
			return ResponseEntity.internalServerError()
					.body(new SessionData(user, "Errore interno del server."));
		}
	}

	@PostMapping("/fotoProfilo")
	public ResponseEntity<SessionData> changeFotoProfilo(HttpSession session, @RequestBody Map<String, String> reqBody){
		String newFoto = reqBody.get("data");
		UserDto user = (UserDto) session.getAttribute("user");

		// Controllo di sicurezza: se la sessione è scaduta o non esiste
		if (user == null) {
			return ResponseEntity.status(401).body(new SessionData(null, "Sessione scaduta."));
		}

		if(userService.changeUserImage(user.getUsername(), newFoto)) {
			UserDto updatedUser = UserDtoMapper.map(userService.getUserByUsername(user.getUsername()));
			session.setAttribute("user", updatedUser);
			return ResponseEntity.ok(new SessionData(updatedUser, "Immagine profilo aggiornata con successo."));
		}

		return ResponseEntity.internalServerError().body(new SessionData(user, "Errore interno del server."));
	}

	@PostMapping("/aboutMe")
	public ResponseEntity<SessionData> changeDescription(HttpSession session, @RequestBody Map<String, String> reqBody){
		String newDescription = reqBody.get("data");
		UserDto user = (UserDto) session.getAttribute("user");

		// Controllo di sicurezza: se la sessione è scaduta o non esiste
		if (user == null) {
			return ResponseEntity.status(401).body(new SessionData(null, "Sessione scaduta."));
		}

		if(userService.changeTeacherDescription(user.getUsername(), newDescription)) {
			UserDto updatedUser = UserDtoMapper.map(userService.getUserByUsername(user.getUsername()));
			session.setAttribute("user", updatedUser);
			return ResponseEntity.ok(new SessionData(updatedUser, "Descrizione aggiornata con successo."));
		}

		return ResponseEntity.internalServerError().body(new SessionData(user, "Errore interno del server."));
	}

	@PostMapping("/lingueParlate")
	public ResponseEntity<SessionData> changeLingueParlate(HttpSession session, @RequestBody Map<String, Set<String>> reqBody) {
		Set<String> newLingue = reqBody.get("data");
		UserDto user = (UserDto) session.getAttribute("user");

		// Controllo di sicurezza: se la sessione è scaduta o non esiste
		if (user == null) {
			return ResponseEntity.status(401).body(new SessionData(null, "Sessione scaduta."));
		}

		if(userService.changeUserLingueParlate(user.getUsername(), newLingue)) {
			UserDto updatedUser = UserDtoMapper.map(userService.getUserByUsername(user.getUsername()));
			session.setAttribute("user", updatedUser);
			return ResponseEntity.ok(new SessionData(updatedUser, "Lingue aggiornate con successo."));
		}

		return ResponseEntity.internalServerError().body(new SessionData(user, "Errore interno del server."));
	}

	@PostMapping("/titoliStudio")
	public ResponseEntity<SessionData> changeTitoliStudio(HttpSession session, @RequestBody Map<String, Set<String>> reqBody) {
		Set<String> newTitoli = reqBody.get("data");
		UserDto user = (UserDto) session.getAttribute("user");

		// Controllo di sicurezza: se la sessione è scaduta o non esiste
		if (user == null) {
			return ResponseEntity.status(401).body(new SessionData(null, "Sessione scaduta."));
		}

		if(userService.changeTeacherTitoliStudio(user.getUsername(), newTitoli)) {
			UserDto updatedUser = UserDtoMapper.map(userService.getUserByUsername(user.getUsername()));
			session.setAttribute("user", updatedUser);
			return ResponseEntity.ok(new SessionData(updatedUser, "Titoli aggiornati con successo."));
		}

		return ResponseEntity.internalServerError().body(new SessionData(user, "Errore interno del server."));
	}
}
