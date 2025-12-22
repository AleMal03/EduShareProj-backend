package edushare.serveredushare.DTO;

import java.io.Serializable;
import java.util.Set;

import edushare.serveredushare.persistence.TeacherProfile;
import edushare.serveredushare.persistence.User;
import edushare.serveredushare.persistence.User.Role;

public class UserDTO implements Serializable {
    private String username;
    private String email;
    private String nome;
    private String cognome;
    private int eta;
    private String nazionalita;
    private Set<String> lingueParlate;
    private Set<Role> ruoli;
    private TeacherProfileDTO teacherProfileDTO;

    public UserDTO(String username, String email, String nome, String cognome, int eta, String nazionalita, Set<String> lingueParlate, Set<Role> ruoli, TeacherProfileDTO teacherProfileDto) {
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.nazionalita = nazionalita;
        this.lingueParlate = lingueParlate;
        this.ruoli = ruoli;
        this.teacherProfileDTO = teacherProfileDto;
    }



    // --- GETTERS ---
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public int getEta() { return eta; }
    public String getNazionalita() { return nazionalita; }
    public Set<String> getLingueParlate() { return lingueParlate; }
    public Set<Role> getRuoli() { return ruoli; }
    public TeacherProfileDTO getTeacherProfileDto() { return teacherProfileDTO; }



    // --- MAPPER STATICO ---
    public static UserDTO mapUserToUserDTO(User user){
        TeacherProfileDTO tDTO = null;

        if(user == null) return null;

        // Logica per creare il DTO innestato
        if(user.getRuoli().contains(User.Role.TEACHER) && user.getTeacherProfile() != null){
            TeacherProfile teacherProfile = user.getTeacherProfile();
            // CORREZIONE: Ora possiamo chiamare new TeacherProfileDTO direttamente
            tDTO = new TeacherProfileDTO(teacherProfile.getAboutMe(), teacherProfile.getTitoliStudio());
        }

        return new UserDTO(user.getUsername(), user.getEmail(), user.getNome(), user.getCognome(),
                user.getEta(), user.getNazionalita(), user.getLingueParlate(), user.getRuoli(), tDTO);
    }



    // --- CLASSE INNESTATA - estensione teacher (STATIC) ---
    public static class TeacherProfileDTO implements Serializable {
        private String aboutMe;
        private Set<String> titoliStudio;

        public TeacherProfileDTO(String aboutMe, Set<String> titoliStudio) {
            this.aboutMe = aboutMe;
            this.titoliStudio = titoliStudio;
        }

        public String getAboutMe() { return aboutMe; }
        public Set<String> getTitoliStudio() { return titoliStudio; }
    }
}