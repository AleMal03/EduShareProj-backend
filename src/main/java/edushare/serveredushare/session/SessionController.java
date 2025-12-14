package edushare.serveredushare.session;

import edushare.serveredushare.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.Map;


/// Controller delle route di sessione (login - logout)
@RestController
@RequestMapping("/session")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")      // Per cross-origin (solo fase di dev)
public class SessionController {
	private final UserService userService;

	SessionController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<SessionData> create(HttpSession session, @RequestBody Map<String, String> credentials, OutputStream outputStream) {
		String username = credentials.get("username");
		String password = credentials.get("password");
		String existingUser = (String) session.getAttribute("username");

		if (username == null || password == null) {
			if (existingUser == null) {
				return ResponseEntity.ok(new SessionData("","Utente non autenticato."));
			}
			return ResponseEntity.ok(new SessionData(existingUser, "Utente già autenticato."));
		}
		if (existingUser != null) {
			if (username.equals(existingUser)) {
				return ResponseEntity.ok(new SessionData(username,"Utente già autenticato."));
			}
			return ResponseEntity.badRequest().body(new SessionData("","Un altro utente è già autenticato."));
		}

		boolean auth = userService.checkCredentials(username, password);

		if (auth) {
			session.setAttribute("username", username);
			return ResponseEntity.ok(new SessionData(username, "Log in successful."));
		}
		return ResponseEntity.status(401).body(
				new SessionData("", "Credenziali non valide."));
	}

	@GetMapping("/logout")
	public ResponseEntity<SessionData> invalidate(HttpSession session) {
		String existingUser = (String) session.getAttribute("username");

		if (existingUser == null) {
			return ResponseEntity.ok(new SessionData("","No user to log out."));
		}

		session.invalidate();
		return ResponseEntity.ok(new SessionData("","Log out successful."));
	}
}
