package edushare.serveredushare.DTO;

import java.util.List;

public class FileData {
    List<FileDTO> listaFiles;  // Lista file da inviare al frontend
	String message;

	public FileData(List<FileDTO> listaCorsi, String message) {
		this.listaFiles = listaCorsi;
		this.message = message;
	}

	public List<FileDTO> getListaCorsi() {
		return listaFiles;
	}

	public String getMessage() {
		return message;
	}
}