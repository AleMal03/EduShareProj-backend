package edushare.serveredushare.DTO;

import java.io.Serializable;
import java.util.Set;
import edushare.serveredushare.persistence.User.Role;

public class UserDto implements Serializable {
	private String username;
	private String email;
	private String nome;
	private String cognome;
	private int eta;
	private String nazionalita;
	private Set<String> lingueParlate;
	private Set<Role> ruoli;
	private TeacherProfileDto teacherProfileDto;
	private double credito;
	private String fotoProfilo;

	public UserDto(String username, String email, String nome, String cognome, int eta, String nazionalita,
	               Set<String> lingueParlate, Set<Role> ruoli, double credito, String fotoProfilo,
	               TeacherProfileDto teacherProfileDto) {
		this.username = username;
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.eta = eta;
		this.nazionalita = nazionalita;
		this.lingueParlate = lingueParlate;
		this.ruoli = ruoli;
		this.teacherProfileDto = teacherProfileDto;
		this.credito = credito;
		this.fotoProfilo = fotoProfilo;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
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

	public Set<Role> getRuoli() {
		return ruoli;
	}

	public TeacherProfileDto getTeacherProfileDto() {
		return teacherProfileDto;
	}

	public double getCredito() {
		return credito;
	}

	public String getFotoProfilo() {
		return fotoProfilo;
	}
}
