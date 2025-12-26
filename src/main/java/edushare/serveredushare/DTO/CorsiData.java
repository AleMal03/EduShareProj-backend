package edushare.serveredushare.DTO;

import java.util.List;

public class CorsiData {
	List<CourseDTO> listaCorsi;  // Lista corsi da inviare al frontend
	String message;

	public CorsiData(List<CourseDTO> listaCorsi, String message) {
		this.listaCorsi = listaCorsi;
		this.message = message;
	}

	public List<CourseDTO> getListaCorsi() {
		return listaCorsi;
	}

	public String getMessage() {
		return message;
	}
}
