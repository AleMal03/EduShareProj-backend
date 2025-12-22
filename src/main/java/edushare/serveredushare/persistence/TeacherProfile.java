package edushare.serveredushare.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Professori")
public class TeacherProfile {
	@Id
	private String username;

	@OneToOne
	@MapsId     // Indica che la PK di TeacherProfile dev'essere la stessa di User
	private User user;

	@Column(name = "AboutMe", nullable = false)
	private String aboutMe;

	@Column(name = "Titoli Di Studio", nullable = false)
	@ElementCollection(fetch = FetchType.EAGER)     // Campo con molteplicità 1...N
	private Set<String> titoliStudio;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Course> mieiCorsi;

	public TeacherProfile() {
		titoliStudio = new HashSet<String>();
		mieiCorsi = new LinkedList<Course>();
	}

	public TeacherProfile(User user, String aboutMe, Set<String> titoliStudio) throws IllegalArgumentException {
		if(user == null)
			throw new IllegalArgumentException("Teacher profile must have non null User attached");

		this.user = user;
		this.aboutMe = aboutMe;
		this.titoliStudio = titoliStudio;
	}

	// ---- GETTERS ----

	public String getUsername() {
		return username;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public Set<String> getTitoliStudio() {
		return titoliStudio;
	}

	@JsonIgnore
	public List<Course> getMieiCorsi() {
		return mieiCorsi;
	}

	// ---- SETTERS ----

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public void setTitoliStudio(Set<String> titoliStudio) {
		this.titoliStudio = titoliStudio;
	}


	// ---- METODI ----

	public void addCourse(Course course) {
		mieiCorsi.add(course);
	}
}
