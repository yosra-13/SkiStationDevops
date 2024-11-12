package com.example.gestionstationskii.entities;

import com.example.gestionstationskii.entities.Color; // Assuming Color is an Enum
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Piste {
	private Long numPiste;
	private String namePiste;
	private Color color;
	private int length;
	private int slope;
}
