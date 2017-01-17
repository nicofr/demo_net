package main.controller.exceptions;


public class BadParameterException extends Exception{
	


	private static final long serialVersionUID = 1L;
	public String name;
	public String hint;
	
	
	public BadParameterException(String name, String hint) {
		this.name = name;
		this.hint = hint;
	}
	
	@Override
	public String toString() {
		return "Parameter "+name+" should be "+hint; 
	}
	
}
