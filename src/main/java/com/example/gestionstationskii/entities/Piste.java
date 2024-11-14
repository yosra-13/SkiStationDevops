package com.example.gestionstationskii.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

<<<<<<< HEAD
=======

>>>>>>> gestion_piste
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Piste implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numPiste;
	String namePiste;
	@Enumerated(EnumType.STRING)
	Color color;
	int length;
	int slope;

	@ManyToMany(mappedBy= "pistes")
<<<<<<< HEAD
   private 	Set<Skier> skiers;
=======
	private Set<Skier> skiers;
>>>>>>> gestion_piste

}
