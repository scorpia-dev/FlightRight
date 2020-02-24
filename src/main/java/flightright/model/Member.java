package flightright.model;

import java.io.File;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import flightright.controller.OnCreate;
import flightright.validation.FileValidatorConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = "[a-zA-Z]+", message = "a name can only contain letters")
	@Size(min = 1, max = 30)
	@NotEmpty(groups = OnCreate.class, message = "{firstName.notEmpty}")
	private String firstName;

	@Pattern(regexp = "[a-zA-Z]+", message = "a name can only contain letters")
	@Size(min = 1, max = 30)
	@NotEmpty(groups = OnCreate.class, message = "{lastName.notEmpty}")
	private String lastName;

	@Past
	@JsonFormat(pattern = "yyyy-mm-dd")
	@NotNull(groups = OnCreate.class, message = "{dateOfBirth.notNull}")
	private Date dateOfBirth;

	@Pattern(regexp = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$", message = "Invalid UK post code")
	@NotEmpty(groups = OnCreate.class, message = "{postalCode.notEmpty}")
	private String postalCode;
	
	@NotNull(groups = OnCreate.class, message = "{picture.notEmpty}")
	@FileValidatorConstraint
	private File picture;

	public Member(String firstName, String lastName, Date dateOfBirth, String postalCode, File picture) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.postalCode = postalCode;
		this.picture = picture;
	}

}
