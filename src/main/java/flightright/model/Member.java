package flightright.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import flightright.controller.OnCreate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = "[a-zA-Z]+", message = "a name can only contain letters")
	@Size(min = 1, max = 30)
	@NotEmpty (groups = OnCreate.class, message = "First name can not be empty" )
	String firstName;

	@Pattern(regexp = "[a-zA-Z]+", message = "a name can only contain letters")
	@Size(min = 1, max = 30)
	@NotEmpty (groups = OnCreate.class, message = "Last name can not be empty")
	String lastName;

	@Past
	@JsonFormat(pattern = "yyyy-mm-dd")
	@NotNull (groups = OnCreate.class, message = "Date of birth can not be empty")
	private Date dateOfBirth;

	@Pattern(regexp = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$", message = "Invalid UK postcode")
	@NotEmpty (groups = OnCreate.class, message = "Postal code can not be empty")
	private String postalCode;

	public Member (String firstName, String lastName, Date dateOfBirth, String postalCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.postalCode = postalCode;
	}
	
}
