package edushare.serveredushare.services;

import edushare.serveredushare.persistence.Course;
import edushare.serveredushare.persistence.CourseRepository;
import edushare.serveredushare.persistence.File;
import edushare.serveredushare.persistence.FileRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@DependsOn("courseService")
public class FileService {
	private final FileRepository fileRepository;
	private final CourseRepository courseRepository;

	public FileService(FileRepository fileRepository, CourseRepository courseRepository) {
		this.fileRepository = fileRepository;
		this.courseRepository = courseRepository;
	}

	@EventListener(ApplicationReadyEvent.class) // Al posto di @PostConstruction per aprire una sessione che permetta di recuperare il corso lazy dal repository
	@Transactional
	@Order(3)
	public void init(){
		try {
			// Video
			creaNuovoFile("Lezione 1", "pathInesistente", "mp4_icon.png", 1L);
			creaNuovoFile("Lezione 1", "pathInesistente", "mp4_icon.png", 2L);
			creaNuovoFile("Lezione 2", "pathInesistente", "mp4_icon.png", 2L);
			creaNuovoFile("Lezione 1", "pathInesistente", "mp4_icon.png", 3L);
			creaNuovoFile("Lezione 2", "pathInesistente", "mp4_icon.png", 3L);
			creaNuovoFile("Lezione 3", "pathInesistente", "mp4_icon.png", 3L);
			creaNuovoFile("Lezione 1", "pathInesistente", "mp4_icon.png", 4L);
			creaNuovoFile("Lezione 1", "pathInesistente", "mp4_icon.png", 5L);
			creaNuovoFile("Lezione 2", "pathInesistente", "mp4_icon.png", 5L);
			creaNuovoFile("Lezione 1", "pathInesistente", "mp4_icon.png", 6L);

			// Pdf
			creaNuovoFile("Appunti 1", "pathInesistente", "pdf_icon.png", 1L);
			creaNuovoFile("Appunti 2", "pathInesistente", "pdf_icon.png", 1L);
			creaNuovoFile("Appunti 1", "pathInesistente", "pdf_icon.png", 2L);
			creaNuovoFile("Appunti 1", "pathInesistente", "pdf_icon.png", 3L);
			creaNuovoFile("Appunti 2", "pathInesistente", "pdf_icon.png", 3L);
			creaNuovoFile("Appunti 1", "pathInesistente", "pdf_icon.png", 4L);
			creaNuovoFile("Appunti 2", "pathInesistente", "pdf_icon.png", 4L);
			creaNuovoFile("Appunti 1", "pathInesistente", "pdf_icon.png", 6L);
			creaNuovoFile("Appunti 2", "pathInesistente", "pdf_icon.png", 6L);
			creaNuovoFile("Appunti 1", "pathInesistente", "pdf_icon.png", 7L);
			creaNuovoFile("Appunti 2", "pathInesistente", "pdf_icon.png", 7L);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Crea un nuovo file e lo aggiunge al repository dei file e alla lista dei file del corso associato
	 */
	@Transactional
	public void creaNuovoFile(String nome, String path, String icona, Long courseId) throws IllegalArgumentException{
		Optional<Course> opCourse = courseRepository.findById(courseId);
		Course course;

		if(opCourse.isPresent()){
			course = opCourse.get();
		}
		else{
			throw new IllegalArgumentException("Course " + courseId + " not found in creaNuovoCorso");
		}

		File newFile = new File(nome, path, icona, course);

		fileRepository.save(newFile);
	}


	@Transactional
	public List<File> getAllFilesByCourseId(Long id){
		return fileRepository.getFilesByCorso_Id(id);
	}
	



}
