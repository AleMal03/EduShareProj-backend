package edushare.serveredushare.session;

import edushare.serveredushare.DTO.UserDto;

/// Struttura di supporto per response da mandare al client
public class SessionData {
	private UserDto cookieId;    // Username
	private String message;     // Messaggio di risposta

	SessionData(UserDto cookieId, String retrievedValue) {
		this.cookieId = cookieId;
		this.message = retrievedValue;
	}

	public UserDto getCookieId() {return cookieId;}

	public String getMessage() {return message;}
}

