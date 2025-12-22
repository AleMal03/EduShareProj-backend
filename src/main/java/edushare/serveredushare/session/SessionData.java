package edushare.serveredushare.session;

import edushare.serveredushare.DTO.UserDTO;

/// Struttura di supporto per response da mandare al client
public class SessionData {
	private UserDTO user;       // UserDTO per il frontend
	private String message;     // Messaggio di risposta

	public SessionData(UserDTO user, String retrievedValue) {
		this.user = user;
		this.message = retrievedValue;
	}

	public UserDTO getUser() {return user;}

	public String getMessage() {return message;}
}

