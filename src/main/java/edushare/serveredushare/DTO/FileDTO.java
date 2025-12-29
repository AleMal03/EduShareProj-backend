package edushare.serveredushare.DTO;

import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.persistence.Course.Difficolta;
import edushare.serveredushare.persistence.File;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

public class FileDTO implements Serializable{

	private Long id;
	private String nome;
	private String path;
	private String icona;
    private Long idCorso;

    public FileDTO(){}

    public FileDTO(Long id, String nome, String path, String icona, Long idCorso){
        this.id = id;
        this.nome = nome;
        this.path = path;
        this.icona = icona;
        this.idCorso = idCorso;
    }

    // --- GETTER 
    public Long getId(){ return id; }
    public String getNome(){ return nome; }
    public String getPath(){ return path; }
    public String getIcona(){ return icona; }
    public Long getIdCorso(){ return idCorso; }
    
    // --- MAPPER STATICO ---
	public static FileDTO mapFileToFileDTO(File file) {
		if (file == null) return null;

		return new FileDTO(
				file.getId(),
				file.getNome(),
				file.getPath(),
				file.getIcona(),
				file.getCorso().getId()
		);
	}


}
