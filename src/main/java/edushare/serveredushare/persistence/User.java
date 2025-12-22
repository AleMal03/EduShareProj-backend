package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

/// Entità User con informazioni del profilo utente
@Entity
@Table(name = "Utenti")
public class User {
	public enum Role {TEACHER, STUDENT}

	@Id
	@Column(name = "Username", unique = true, nullable = false)
	private String username;

	@Column(name = "Password", nullable = false)
	private String password;

	@Column(name = "Nome", nullable = false)
	private String nome;

	@Column(name = "Cognome", nullable = false)
	private String cognome;

	@Column(name = "Email", nullable = false)
	private String email;

	@Column(name = "Eta")
	private int eta;

	@Column(name = "Nazionalita")
	private String nazionalita;

	@Column(nullable = false)
	@ElementCollection(fetch = FetchType.EAGER)     // Campo con molteplicità 1...N
	private Set<String> lingueParlate;

	@Column(name = "Ruolo", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<Role> ruoli;

	@Column(name = "Immagine Profilo")
	private String immagineProfilo;

	@Column(name = "Credito")
	private double credito;

	 @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)    // Cascade perché TeacherProfile è subordinato a User
	 private TeacherProfile teacherProfile;

	 @ManyToMany
	 @JoinTable(name = "Corsi Seguiti Utente")
	 private List<Course> corsiSeguiti;

	public User(){
		lingueParlate = new HashSet<>();    // Init per evitare NullPointerException
		corsiSeguiti = new LinkedList<>();
	}

	public User(String username, String password, String nome, String cognome, String email, int eta,
	            String nazionalita, Set<String> lingueParlate, Set<Role> ruoli, String img, double credito){
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.eta = eta;
		this.nazionalita = nazionalita;
		this.lingueParlate = new HashSet<>(lingueParlate);
		this.ruoli = new HashSet<>(ruoli);
		this.immagineProfilo = img == null ? "default_user.png" : img;
		this.credito = credito;
	}

	public String getUsername() {
		return username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getEmail() {return email;}

	public int getEta() {
		return eta;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public Set<String> getLingueParlate() {
		return lingueParlate;
	}

	public Set<Role> getRuoli() {
		return ruoli;
	}

	public String getImmagineProfilo() {
		return immagineProfilo;
	}

	public double getCredito() {
		return credito;
	}

	public TeacherProfile getTeacherProfile(){
		return teacherProfile;
	}

	public void setTeacherProfile(TeacherProfile teacherProfile) throws IllegalStateException{
		if(ruoli.contains(Role.TEACHER))
			this.teacherProfile = teacherProfile;
		else
			throw new IllegalStateException("L'utente " + this.username + " non ha il ruolo TEACHER.");
	}

	public List<Course> getCorsiSeguiti() {
		return corsiSeguiti;
	}

	public void addToCorsiSeguiti(Course course){
		this.corsiSeguiti.add(course);
	}
}
