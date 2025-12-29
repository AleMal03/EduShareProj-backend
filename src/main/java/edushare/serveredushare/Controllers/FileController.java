package edushare.serveredushare.controllers;

import edushare.serveredushare.DTO.FileDTO;
import edushare.serveredushare.DTO.FileData;
import edushare.serveredushare.persistence.File;
import edushare.serveredushare.services.FileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FileController {

    private FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

    /**
	 * Restituisce tutti i file di un corso presenti nel DB
	 */
	// Esempio "http://localhost:5173/files?idCorso=1"
	@GetMapping("")
	public ResponseEntity<FileData> allCourses(HttpSession session, @RequestParam (required = true) Long idCorso){

		List<File> listaFiles = fileService.getAllFilesByCourseId(idCorso);

		return ResponseEntity.ok(new FileData(listaFiles.stream()
				.map(FileDTO::mapFileToFileDTO).toList(), "Files del corso: ' " + idCorso + " '"));
	}
}
