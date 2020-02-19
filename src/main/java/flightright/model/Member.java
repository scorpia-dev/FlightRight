package flightright.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Member {

	@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
/*	@Valid
	@NotEmpty*/
	
	@Column
	String firstName;
/*
	@NotEmpty
*/
	//String lastName;
/*
	@NotNull
	 @Past
	 @JsonFormat(pattern="yyyy-mm-dd")
	private Date dateOfBirth;

	private String PostalCode;*/

}
