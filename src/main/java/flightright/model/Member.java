package flightright.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = "[a-zA-Z]+", message = "a name can only contain letters")
	@Size(min = 1, max = 30)
	String firstName;

	@Pattern(regexp = "[a-zA-Z]+", message = "a name can only contain letters")
	@Size(min = 1, max = 30)
	String lastName;

	
	@Past
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date dateOfBirth;

	@Pattern(regexp = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$", message = "Invalid UK postcode")
	private String postalCode;

	public Member (String firstName, String lastName, Date dateOfBirth, String postalCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.postalCode = postalCode;
	}
	
}
