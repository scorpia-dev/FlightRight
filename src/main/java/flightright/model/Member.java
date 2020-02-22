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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = "[a-zA-Z]+", message = "Name can only be letters")
	@Size(min = 2, max = 30)
	@NotEmpty
	@Valid
	@Column
	String firstName;

	@Pattern(regexp = "[a-zA-Z]+", message = "Name can only be letters")
	@Size(min = 1, max = 30)
	@Valid
	String lastName;

	@Valid
	@NotNull
	@Past
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date dateOfBirth;

	@Pattern(regexp = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$", message = "Invalid UK postcode")
	@Valid
	@NotNull
	private String postalCode;

	public Member (String firstName, String lastName, Date dateOfBirth, String postalCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.postalCode = postalCode;
	}
	
}
