package com.ibm.academia.restapi.modelos.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.academia.restapi.enums.Color;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString

@Table(name = "apuestas",schema = "ruletas")
public class Apuesta implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "apuesta_color")
    @Enumerated(EnumType.STRING)
    private Color apuestaColor;
    
    @DecimalMin(value = "0" ,message = "Numeros disponibles para apostar: del 0 al 36")
    @DecimalMax(value = "36",message = "Numeros disponibles para apostar: del 0 al 36")
    @Column(name = "apuesta_numero")
    private Integer apuestaNumero;

    @DecimalMin(value = "1"    ,message = "Monto permitido para apostar: 1 a 10000 dolares")
    @DecimalMax(value = "10000",message = "Monto permitido para apostar: 1 a 10000 dolares")
    @Column(name = "apuesta_dinero")
    private Integer apuestaDinero;

    @Column(name = "fecha_alta")
    private Date fechaAlta;

    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ruleta_id", foreignKey = @ForeignKey (name = "FK_RULETA_ID"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "apuestas"})
    private Ruleta ruleta;

    
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
		Apuesta other = (Apuesta) obj;
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

	private static final long serialVersionUID = 6089850537685116142L;
}
