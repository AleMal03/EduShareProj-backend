package edushare.serveredushare.persistence;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/// Entità User con informazioni del profilo utente
@Entity
@Table(name = "utenti")
public class User {
	public enum Role {TEACHER, STUDENT}

	@Id
	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "cognome", nullable = false)
	private String cognome;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "eta")
	private int eta;

	@Column(name = "nazionalita")
	private String nazionalita;

	@Column(nullable = false)
	@ElementCollection(fetch = FetchType.EAGER)     // Campo con molteplicità 1...N
	private Set<String> lingueParlate;

	@Column(name = "ruolo", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role ruolo;

	public User(){
		lingueParlate = new HashSet<>();    // Init per evitare NullPointerException
	}

	public User(String username, String password, String nome, String cognome, String email, int eta, String nazionalita, Set<String> lingueParlate, Role ruolo) {
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.eta = eta;
		this.nazionalita = nazionalita;
		this.lingueParlate = new HashSet<>(lingueParlate);
		this.ruolo = ruolo;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getEmail() {
		return email;
	}

	public int getEta() {
		return eta;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public Set<String> getLingueParlate() {
		return lingueParlate;
	}

	public Role getRole() {
		return ruolo;
	}
}
