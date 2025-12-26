package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/// Entità Corso acquistabile dagli utenti e di proprietà di un insegnante
@Entity
@Table(name = "Corsi")
public class Course {
	public enum Difficolta{PRINCIPIANTE, FACILE, MEDIO, DIFFICILE, ESPERTO}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id", nullable = false)
	private Long id;

	@Column(name = "Nome Corso", nullable = false)
	private String nome;

	@Column(name = "Materia Corso", nullable = false)
	private String materia;

	@Column(name = "Prezzo", nullable = false)
	private Double prezzo;

	@Column(name = "difficolta")
	@Enumerated(EnumType.STRING)    // Serve a trattare il valore dell'enum come stringa invece che come int
	private Difficolta difficolta;

	@Column(name = "Icona")
	private String icona;

	@ManyToOne
	private TeacherProfile owner;

	@ManyToMany(mappedBy = "corsiSeguiti")
	private List<User> studentiIscritti;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<File> risorse;

	public Course() {
		studentiIscritti = new ArrayList<>();
	}

	public Course(String nome, String materia, double prezzo, Difficolta difficolta, String icona, TeacherProfile owner, List<File> risorse) {
		this.nome = nome;
		this.materia = materia;
		this.prezzo = prezzo;
		this.difficolta = difficolta;
		this.icona = icona;
		this.owner = owner;

		if(risorse != null)
			this.risorse = List.copyOf(risorse);
		else
			this.risorse = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getMateria() {
		return materia;
	}

	public TeacherProfile getOwner() {
		return owner;
	}

	public List<User> getStudentiIscritti() {
		return studentiIscritti;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public Difficolta getDifficolta() {
		return difficolta;
	}

	public String getIcona() {
		return icona;
	}

	public List<File> getRisorse() {
		return risorse;
	}

	public void addRisorsa(File risorsa){
		risorse.add(risorsa);
	}
}
