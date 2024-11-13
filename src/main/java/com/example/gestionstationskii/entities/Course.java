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
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numCourse;
	int level;
	@Enumerated(EnumType.STRING)
	TypeCourse typeCourse=TypeCourse.INDIVIDUAL;
	@Enumerated(EnumType.STRING)
	Support support=Support.SNOWBOARD;
	Float price;
	int timeSlot;

	@JsonIgnore
	@OneToMany(mappedBy= "course")
	 private Set<Registration> registrations;
	@ManyToOne
	@JoinColumn(name = "instructor_id", nullable = true)
	Instructor instructor;


}
