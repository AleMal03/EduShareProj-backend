package edushare.serveredushare.DTO;

import java.util.List;

public class FileData {
    List<FileDTO> listaFiles;  // Lista file da inviare al frontend
	String message;

	public FileData(List<FileDTO> listaFiles, String message) {
		this.listaFiles = listaFiles;
		this.message = message;
	}

	public List<FileDTO> getListaFiles() {
		return listaFiles;
	}

	public String getMessage() {
		return message;
	}
}