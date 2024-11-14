package com.example.gestionstationskii.entities;

import java.io.Serializable;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
<<<<<<< HEAD
public class  Course implements Serializable {
=======
public class Course implements Serializable {
>>>>>>> gestion_piste

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numCourse;
	int level;
	@Enumerated(EnumType.STRING)
	TypeCourse typeCourse;
	@Enumerated(EnumType.STRING)
	Support support;
	Float price;
	int timeSlot;

	@JsonIgnore
	@OneToMany(mappedBy= "course")
<<<<<<< HEAD
	 private  Set<Registration> registrations;
=======
	private Set<Registration> registrations;
>>>>>>> gestion_piste

}
