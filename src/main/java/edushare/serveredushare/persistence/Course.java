package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/// Entità Corso acquistabile dagli utenti e di proprietà di un insegnante
@Entity
@Table(name = "Corsi")
public class Course {
    public enum Difficolta{FACILE, MEDIA, DIFFICILE}

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
    @Enumerated(EnumType.STRING)
    private Difficolta difficolta;

    @Column(name = "Icona")
    private String icona;

    @Column(name = "mediaRecensioni")
    private float mediaRecensioni;

    @ManyToOne
    private TeacherProfile owner;

    @OneToMany(mappedBy = "corso", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<File> risorse = new ArrayList<>();

    // Colleghiamo il corso alla tabella delle iscrizioni
    @OneToMany(mappedBy = "corso", cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<FollowedCourse> iscritti = new ArrayList<>();

    public Course() {}

    public Course(String nome, String materia, double prezzo, Difficolta difficolta, String icona, TeacherProfile owner, float mediaRecensioni) {
        this.nome = nome;
        this.materia = materia;
        this.prezzo = prezzo;
        this.difficolta = difficolta;
        this.icona = icona;
        this.owner = owner;
        this.mediaRecensioni = 0F;
    }

    // Getter e Setter standard
    public Long getId() { return id; }
    
    public String getNome() { return nome; }
    
    public String getMateria() { return materia; }
    
    public TeacherProfile getOwner() { return owner; }
    
    public double getPrezzo() { return prezzo; }
    
    public Difficolta getDifficolta() { return difficolta; }
    
    public String getIcona() { return icona; }
    
    public List<File> getRisorse() { return risorse; }
    
    public float getMediaRecensioni() { return mediaRecensioni; }

    public void addRisorsa(File risorsa){
        risorse.add(risorsa);
    }

    // Getter e Setter per la nuova lista
    public List<FollowedCourse> getIscritti() {
        return iscritti;
    }

    public void setIscritti(List<FollowedCourse> iscritti) {
        this.iscritti = iscritti;
    }

    public void setMediaRecensioni(float mediaRecensioni) {
        this.mediaRecensioni = mediaRecensioni;
    }
}