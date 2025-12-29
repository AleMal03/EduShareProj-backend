package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Files")
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Nome File", nullable = false)
	private String nome;

	@Column(name = "Path File", nullable = false)
	private String path;

	@Column(name = "Icona File")
	private String icona;

	@ManyToOne
    @JoinColumn(name = "Corso_Id", nullable = false)
    private Course corso;

	public File(){}

	public File(String nome, String path, String icona, Course course) {
		this.nome = nome;
		this.path = path;
		this.icona = icona == null ? "default_file_icon.png" :  icona;
		this.corso = course;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getPath() {
		return path;
	}

	public String getIcona() {
		return icona;
	}

	@JsonIgnore
	public Course getCorso() {
		return corso;
	}
}
