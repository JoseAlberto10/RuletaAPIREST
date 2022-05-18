package com.ibm.academia.restapi.excepciones;

public class NotFoundException extends RuntimeException
{
	public NotFoundException(String message)
	{
		super(message);
	}
	
	private static final long serialVersionUID = -450382092202398191L;
}
