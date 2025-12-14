package edushare.serveredushare.session;

/// Struttura di supporto per response da mandare al client
public class SessionData {
	private String cookieId;    // Username
	private String message;     // Messaggio di risposta

	SessionData(String cookieId, String retrievedValue) {
		this.cookieId = cookieId;
		this.message = retrievedValue;
	}

	public String getCookieId() {
		return cookieId;
	}

	public String getMessage() {
		return message;
	}
}

