package edushare.serveredushare.services;

import edushare.serveredushare.persistence.*;
import jakarta.transaction.Transactional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@DependsOn("userService")
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TeacherProfileRepository tpRepository;
    private final FollowedCourseRepository followedCourseRepository;
    private final RecensioneRepository recensioniRepository;

    public CourseService(CourseRepository courseRepository, TeacherProfileRepository tpRepository, UserRepository userRepository, FollowedCourseRepository followedCourseRepository, RecensioneRepository recensioneRepository) {
        this.courseRepository = courseRepository;
        this.tpRepository = tpRepository;
        this.userRepository = userRepository;
        this.followedCourseRepository = followedCourseRepository;
        this.recensioniRepository = recensioneRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(2)
    public void init() {
        try {
            // Creazione corsi 
            creaNuovoCorso("Tecnologie Web", "Informatica", 0, Course.Difficolta.FACILE, "TW.png", "Prof1", null);
            creaNuovoCorso("DataBase", "Informatica", 0, Course.Difficolta.FACILE, "DB.png", "Prof1", null);
            creaNuovoCorso("Matematica discreta", "Matematica", 20, Course.Difficolta.FACILE, "MD.png", "Prof1", null);
            creaNuovoCorso("Analisi I", "Matematica", 35, Course.Difficolta.MEDIA, "Analisi.png", "Prof1", null);
            creaNuovoCorso("Fisica Nucleare", "Fisica", 20, Course.Difficolta.FACILE, "Nucleare.png", "Prof1", null);
            creaNuovoCorso("Elettrodinamica", "Fisica", 45, Course.Difficolta.DIFFICILE, "Elettrodinamica.png", "Prof1", null);
            creaNuovoCorso("Magnetismo", "Fisica", 15, Course.Difficolta.MEDIA, "Magnetismo.png", "Prof1", null);


            creaNuovoCorso("Sistemi Operativi", "Informatica", 15, Course.Difficolta.DIFFICILE, "SO.png", "Chi123", null);
            creaNuovoCorso("Prog3", "Informatica", 0, Course.Difficolta.FACILE, "Prog3.png", "Chi123", null);
            creaNuovoCorso("Caduta dell'Impero Romano", "Storia", 0, Course.Difficolta.DIFFICILE, "CIR.png", "Chi123", null);
            creaNuovoCorso("Scoperta dell'America", "Storia", 10, Course.Difficolta.FACILE, "America.png", "Chi123", null);
            creaNuovoCorso("WW1", "Storia", 35, Course.Difficolta.MEDIA, "ww1.png", "Chi123", null);
            creaNuovoCorso("Esistenza", "Filosofia", 35, Course.Difficolta.DIFFICILE, "Esistenza.png", "Chi123", null);
            creaNuovoCorso("Libero Arbitrio", "Filosofia", 35, Course.Difficolta.MEDIA, "LA.png", "Chi123", null);
            creaNuovoCorso("Identità", "Filosofia", 35, Course.Difficolta.DIFFICILE, "Identita.png", "Chi123", null);




            // ISCRIZIONE DELLO STUDENTE 

            // SimoStr segue corso 1, 4, 5
            iscriviStudente("SimoStr", 1L);
            iscriviStudente("SimoStr", 4L);
            iscriviStudente("SimoStr", 5L);
            iscriviStudente("SimoStr", 8L);
            iscriviStudente("SimoStr", 9L);
            iscriviStudente("SimoStr", 10L);
            iscriviStudente("SimoStr", 13L);
            iscriviStudente("SimoStr", 14L);
            iscriviStudente("SimoStr", 15L);

            // Chi123 segue corso 1, 2, 3
            iscriviStudente("Chi123", 1L);
            iscriviStudente("Chi123", 2L);
            iscriviStudente("Chi123", 3L);
            iscriviStudente("Chi123", 4L);
            iscriviStudente("Chi123", 7L);

            // AleMa segue corso 1, 3
            iscriviStudente("AleMa", 1L);
            iscriviStudente("AleMa", 3L);
            iscriviStudente("AleMa", 10L);
            iscriviStudente("AleMa", 12L);
            iscriviStudente("AleMa", 13L);



            // RECENSIONI DEI CORSI
            
            aggiungiRecensione(2, null, 1L);
            aggiungiRecensione(4, "Prova", 1L);
            aggiungiRecensione(5, "Buono", 1L);
            aggiungiRecensione(1, "Funziona", 1L);
            aggiungiRecensione(3, null, 1L);
            aggiungiRecensione(2, null, 1L);

            aggiungiRecensione(3, null, 2L);
            aggiungiRecensione(5, null, 2L);
            aggiungiRecensione(1, null, 2L);
            aggiungiRecensione(2, null, 2L);
            aggiungiRecensione(2, null, 2L);

            aggiungiRecensione(5, null, 3L);
            aggiungiRecensione(4, "Prova", 3L);
            aggiungiRecensione(5, "Buono", 3L);
            aggiungiRecensione(5, "Funziona", 3L);
            aggiungiRecensione(3, null, 3L);
            aggiungiRecensione(4, null, 3L);

            aggiungiRecensione(3, null, 4L);
            aggiungiRecensione(1, null, 4L);
            aggiungiRecensione(1, null, 4L);
            aggiungiRecensione(2, null, 4L);
            aggiungiRecensione(2, null, 4L);

			aggiungiRecensione(5, null, 5L);
            aggiungiRecensione(5, null, 5L);
            aggiungiRecensione(4, null, 5L);

			aggiungiRecensione(3, null, 6L);
            aggiungiRecensione(3, null, 6L);
            aggiungiRecensione(4, null, 6L);

			aggiungiRecensione(5, null, 7L);
            aggiungiRecensione(5, null, 7L);

            aggiungiRecensione(1, null, 9L);
            aggiungiRecensione(1, null, 9L);
            aggiungiRecensione(1, null, 9L);
            aggiungiRecensione(3, null, 9L);

            aggiungiRecensione(5, null, 10L);
            aggiungiRecensione(4, null, 10L);

            aggiungiRecensione(5, null, 11L);

            aggiungiRecensione(5, null, 14L);
            aggiungiRecensione(5, null, 14L);
            aggiungiRecensione(3, null, 14L);
            aggiungiRecensione(4, null, 14L);
            aggiungiRecensione(4, null, 14L);

            aggiungiRecensione(3, null, 15L);
            aggiungiRecensione(3, null, 15L);






        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void aggiungiRecensione(int voto, String descrizione, Long corsoId) {
        Course course = courseRepository.findById(corsoId).orElse(null);

		if(course == null)
			return;
		
        Recensione recensione = new Recensione(voto, descrizione, course);
        recensioniRepository.save(recensione);

        float nuova_media = getAverageReviews(corsoId);
        aggiornaMediaRecensioni(course.getId(), nuova_media);
    }
        
    

	@Transactional
    public void iscriviStudente(String username, Long corsoId) {
        User studente = userRepository.findByUsername(username); // Assumi che restituisca User o gestisci null
        Course course = courseRepository.findById(corsoId).orElse(null);

        if (studente != null && course != null) {
            // Controlliamo se esiste già per non creare duplicati al riavvio
            if (!followedCourseRepository.existsByUser_UsernameAndCorso_Id(username, corsoId)) {
                FollowedCourse iscrizione = new FollowedCourse(studente, course);
                followedCourseRepository.save(iscrizione);
            }
        }
    }

    @Transactional
    public void creaNuovoCorso(String nome, String materia, double prezzo, Course.Difficolta difficolta, String icona,
                               String ownerId, List<File> risorse) throws IllegalArgumentException{
        Optional<TeacherProfile> opOwner = tpRepository.findById(ownerId);
        TeacherProfile owner;

        if(opOwner.isPresent())
            owner = opOwner.get();
        else
            throw new IllegalArgumentException("Teacher " + ownerId + " not found in creaNuovoCorso");
        
        Course newCourse = new Course(nome, materia, prezzo, difficolta, icona, owner);

		if(risorse != null)
			risorse.forEach((risorsa) -> newCourse.addRisorsa(risorsa));


        courseRepository.save(newCourse);
        owner.addCourse(newCourse);
    }

    @Transactional
    public void rimuoviCorso(Long idCorso, String ownerId) {
        Course corso = courseRepository.findById(idCorso)
                .orElseThrow(() -> new IllegalArgumentException("Corso non trovato con ID: " + idCorso));

        // 1. CONTROLLO SICUREZZA
        String proprietarioReale = corso.getOwner().getUsername();
        if (!proprietarioReale.equals(ownerId)) {
            throw new IllegalStateException("OPERAZIONE NEGATA");
        }

        // 2. CANCELLA LE RECENSIONI DEL CORSO
        recensioniRepository.deleteByCorso_Id(corso.getId());

        // 3. RIMOZIONE PROFESSORE
        TeacherProfile teacher = corso.getOwner();
        if(teacher != null){
            teacher.getMieiCorsi().remove(corso);
        }

        courseRepository.delete(corso);
    }

    public List<Course> getFilteredCoursesByUsername(String nomeCorso, String owner,String materia, Course.Difficolta difficolta) {
        nomeCorso = cleanParamString(nomeCorso);

		return courseRepository.searchCourses(nomeCorso, owner.toLowerCase(), materia, difficolta, null, null, null);
    }

    public List<Course> getCoursesByUsername(String username) {
        return courseRepository.findByOwner_Username(username);
    }

    public List<Course> getFollowedCoursesByUsername(String studentUsername, String nomeCorso, String owner, String materia, Course.Difficolta difficolta) {
        
        // 1. Recupero le iscrizioni dello studente
        List<FollowedCourse> iscrizioni = followedCourseRepository.findByUser_Username(studentUsername);

        // 2. Estraggo i corsi dalle iscrizioni
        List<Course> corsiSeguiti = iscrizioni.stream().map(FollowedCourse::getCorso).collect(Collectors.toList());

        // 3. Applico i filtri in memoria
        String finalNome = (nomeCorso != null) ? nomeCorso.toLowerCase() : null;
        String finalOwner = (owner != null) ? owner.toLowerCase() : null;
        String finalMateria = materia; 

        return corsiSeguiti.stream()
                .filter(c -> finalNome == null || c.getNome().toLowerCase().contains(finalNome))
                .filter(c -> finalOwner == null || c.getOwner().getUsername().toLowerCase().contains(finalOwner))
                .filter(c -> finalMateria == null || c.getMateria().equalsIgnoreCase(finalMateria))
                .filter(c -> difficolta == null || c.getDifficolta() == difficolta)
                .collect(Collectors.toList());
    }

	@Transactional
    public List<Course> getAllCourses(){return courseRepository.findAll();}

	@Transactional
    public List<Course> getFilteredCourses(String nomeCorso, String owner, String materia, Course.Difficolta difficolta,
                                           Double prezzo, Short rating){

        nomeCorso = cleanParamString(nomeCorso);
        owner = cleanParamString(owner);

        return courseRepository.searchCourses(nomeCorso, owner, materia, difficolta, prezzo, rating, null);
    }

    private String cleanParamString(String s){
        if (s != null && !s.isBlank()) {
            s = "%" + s.toLowerCase() + "%";
        } else {
            s = null;
        }
        return s;
    }

	@Transactional
    public Set<String> getMaterie(){
        List<Course> corsi = getAllCourses();
        Set<String> materie = new HashSet<>();

        for(Course c : corsi){
            materie.add(c.getMateria());
        }
        return materie;
    }

	@Transactional
    public Double getMaxCosto(){
        List<Course> corsi = courseRepository.findAll();

        double maxCosto = 0;
        for(Course c : corsi){
            double costo = c.getPrezzo();
            if(costo > maxCosto){
                maxCosto = costo;
            }
        }
        return maxCosto;
    }

    @Transactional
    public float getAverageReviews(Long id){

        List<Recensione> recensioni = recensioniRepository.findByCorso_Id(id);
        
        float averageReviews = 0;
        int count_reviews = 0;
        for(Recensione recensione : recensioni){
            averageReviews += recensione.getVoto();
            count_reviews++;
        }

        float average =  averageReviews / count_reviews;
        // Esempio: 4.56 -> 45.6 -> 46 -> 4.6
        return (float) (Math.round(average * 10.0) / 10.0);
    }


    @Transactional
    public void aggiornaMediaRecensioni(Long corsoId, float nuovaMedia) {
        Course corso = courseRepository.findById(corsoId)
                .orElseThrow(() -> new RuntimeException("Corso non trovato"));

        corso.setMediaRecensioni(nuovaMedia);

        courseRepository.save(corso);
    }


    @Transactional
    public void rimuoviIscrizione(String username, Long corsoId) {
        if (!followedCourseRepository.existsByUser_UsernameAndCorso_Id(username, corsoId)) {
            throw new IllegalStateException("Iscrizione non trovata");
        }
        followedCourseRepository.deleteByUser_UsernameAndCorso_Id(username, corsoId);
    }

	@Transactional
	public List<FollowedCourse> getAllFollowedCourses(){return followedCourseRepository.findAll();}

}