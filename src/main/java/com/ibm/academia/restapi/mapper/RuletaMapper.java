package com.ibm.academia.restapi.mapper;


import com.ibm.academia.restapi.modelos.dto.RuletaDTO;
import com.ibm.academia.restapi.modelos.entidades.Ruleta;

public class RuletaMapper
{
	public static RuletaDTO mapRuleta(Ruleta ruleta)    //metodo
	{
		RuletaDTO ruletaDto = new RuletaDTO();    //instanciar objeto RuletaDTO
		
		ruletaDto.setId(ruleta.getId());
		ruletaDto.setEstaAbierta(ruleta.getEstaAbierta());
		return ruletaDto;
	}
}
