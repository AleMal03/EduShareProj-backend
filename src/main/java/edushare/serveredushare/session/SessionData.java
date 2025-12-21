package edushare.serveredushare.session;

import edushare.serveredushare.DTO.UserDto;

/// Struttura di supporto per response da mandare al client
public class SessionData {
	private UserDto user;       // UserDTO per il frontend
	private String message;     // Messaggio di risposta

	public SessionData(UserDto user, String retrievedValue) {
		this.user = user;
		this.message = retrievedValue;
	}

	public UserDto getUser() {return user;}

	public String getMessage() {return message;}
}

