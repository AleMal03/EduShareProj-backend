package edushare.serveredushare.DTO;

public class CorsiData {
	CoursesListDTO listaCorsi;  // Lista corsi da inviare al frontend
	String message;

	public CorsiData(CoursesListDTO listaCorsi, String message) {
		this.listaCorsi = listaCorsi;
		this.message = message;
	}

	public CoursesListDTO getListaCorsi() {
		return listaCorsi;
	}

	public String getMessage() {
		return message;
	}
}
