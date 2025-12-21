package edushare.serveredushare.DTO;

import edushare.serveredushare.persistence.TeacherProfile;
import edushare.serveredushare.persistence.User;

import java.util.HashSet;

public class UserDtoMapper {
	public static UserDto map(User user){
		TeacherProfileDto tDto = null;

		if(user == null) return null;

		if(user.getRuoli().contains(User.Role.TEACHER) && user.getTeacherProfile() != null){
			TeacherProfile teacherProfile = user.getTeacherProfile();
			tDto = new TeacherProfileDto(teacherProfile.getAboutMe(), new HashSet<>(teacherProfile.getTitoliStudio()));
		}

		return new UserDto(user.getUsername(), user.getEmail(), user.getNome(), user.getCognome(),
				user.getEta(), user.getNazionalita(), new HashSet<>(user.getLingueParlate()),
				new HashSet<>(user.getRuoli()), user.getCredito(), user.getImmagineProfilo(), tDto);
	}
}
