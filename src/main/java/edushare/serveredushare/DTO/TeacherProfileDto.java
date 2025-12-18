package edushare.serveredushare.DTO;

import java.io.Serializable;
import java.util.Set;

public class TeacherProfileDto implements Serializable {
	private String aboutMe;
	private Set<String> titoliStudio;

	public TeacherProfileDto(String aboutMe, Set<String> titoliStudio) {
		this.aboutMe = aboutMe;
		this.titoliStudio = titoliStudio;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public Set<String> getTitoliStudio() {
		return titoliStudio;
	}
}
