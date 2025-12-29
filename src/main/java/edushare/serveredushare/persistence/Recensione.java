package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Recensioni")
public class Recensione {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Voto", nullable = false)
	private int  voto;

	@Column(name = "Descrizione")
	private String description;

	@ManyToOne
    @JoinColumn(name = "Corso_Id", nullable = false)
    private Course corso;

	public Recensione(){}

	public Recensione(int voto, String description, Course corso) {
		this.voto = voto;
		this.description = description;
		this.corso = corso;
	}

	public Long getId() {
		return id;
	}

	public int getVoto() {
		return voto;
	}

    @JsonIgnore
	public Course getCorso() {
		return corso;
	}

}
