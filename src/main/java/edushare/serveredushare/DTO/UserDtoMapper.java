package edushare.serveredushare.DTO;

import edushare.serveredushare.persistence.TeacherProfile;
import edushare.serveredushare.persistence.User;

public class UserDtoMapper {
	public static UserDto map(User user){
		TeacherProfileDto tDto = null;

		if(user == null) return null;

		if(user.getRuoli().contains(User.Role.TEACHER) && user.getTeacherProfile() != null){
			TeacherProfile teacherProfile = user.getTeacherProfile();
			tDto = new TeacherProfileDto(teacherProfile.getAboutMe(), teacherProfile.getTitoliStudio());
		}

		return new UserDto(user.getUsername(), user.getEmail(), user.getNome(), user.getCognome(),
				user.getEta(), user.getNazionalita(), user.getLingueParlate(), user.getRuoli(), tDto);
	}
}
