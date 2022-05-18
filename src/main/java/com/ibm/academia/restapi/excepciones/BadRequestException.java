package com.ibm.academia.restapi.excepciones;

public class BadRequestException extends RuntimeException    //hereda de clase padre de excepciones
{

	public BadRequestException(String message) 
	{
		super(message);
	}
	
	private static final long serialVersionUID = 1821023831159687789L;
}
