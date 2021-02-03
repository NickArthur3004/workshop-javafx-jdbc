package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidadeExcptions extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>();

	public ValidadeExcptions(String msg) {
		super(msg);
	}
	public Map<String, String> getErros(){
		return erros;
	}
	
	public void addErro(String fieldName, String erreMessage) {
		erros.put(fieldName, erreMessage);
	}
}
