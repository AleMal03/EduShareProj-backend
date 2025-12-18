package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idFile")
	private Long id;

	@Column(name = "nomeFile", nullable = false)
	private String nome;

	@Column(name = "pathFile", nullable = false)
	private String path;

	@Column(name = "iconaFile")
	private String icona;

	@ManyToOne
	private Course course;

	public File(){}

	public File(String nome, String path, String icona, Course course) {
		this.nome = nome;
		this.path = path;
		this.icona = icona == null ? "defaultFileIcon.png" :  icona;
		this.course = course;
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
	public Course getCourse() {
		return course;
	}
}
