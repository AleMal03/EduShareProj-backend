package edushare.serveredushare.DTO;

import java.io.Serializable;

import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.persistence.Course.Difficolta;
public class CourseDTO implements Serializable {	

	private Long id;
	private String nome;
	private String materia;
	private double prezzo;
	private Difficolta difficolta;
	private String icona;
	private double mediaRecensioni;
	private String owner;

	public CourseDTO(){}

	public CourseDTO(Long id, String nome, String materia, double prezzo, Difficolta difficolta, String icona, double mediaRecensioni, String owner){
		this.id = id;
		this.nome = nome;
		this.materia = materia;
		this.prezzo = prezzo;
		this.difficolta = difficolta;
		this.icona = icona;
		this.mediaRecensioni = mediaRecensioni;
		this.owner = owner;
	}

	// --- GETTER ---
	public Long getId() { return id; }
	public String getNome() { return nome; }
	public String getMateria() { return materia; }
	public double getPrezzo() { return prezzo; }
	public Difficolta getDifficolta() { return difficolta; }
	public String getIcona() { return icona; }
	public double getMediaRecensioni(){return mediaRecensioni;}
	public String getOwner() {return owner;}

	// --- MAPPER STATICO ---
	public static CourseDTO mapCourseToCourseDTO(Course course) {
		if (course == null) return null;

		return new CourseDTO(
				course.getId(),
				course.getNome(),
				course.getMateria(),
				course.getPrezzo(),
				course.getDifficolta(),
				course.getIcona(),
				course.getMediaRecensioni(),
				course.getOwner().getUsername()
		);
	}
}