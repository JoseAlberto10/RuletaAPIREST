package com.ibm.academia.restapi.modelos.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString

@Table(name = "ruletas",schema = "ruletas")
public class Ruleta implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ruleta_abierta")
    private Boolean estaAbierta;

    @Column(name = "fecha_alta")
    private Date fechaAlta;

    @Column(name = "fecha_modificacion")
    private Date fechaModificacion; 

    @ToString.Exclude
    @OneToMany(mappedBy  = "ruleta", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "ruleta"})
    private List<Apuesta> apuestas;
    
    
	public Ruleta(Integer id, Boolean estaAbierta, List<Apuesta> apuestas)
	{
		this.id = id;
		this.estaAbierta = estaAbierta;
		this.apuestas = apuestas;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ruleta other = (Ruleta) obj;
		return Objects.equals(id, other.id);
	}

	@PrePersist
	public void antesPersistir() 
	{
		this.fechaAlta = new Date();
	}

	@PreUpdate
	public void antesActualizar() 
	{
		this.fechaModificacion = new Date();
	}

	private static final long serialVersionUID = 1877095949253514432L;
}
