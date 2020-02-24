package flightright;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import flightright.model.Member;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MemberValidatorTest {

	private Validator validator;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

	@Test
	public void AllImageTypesForPictureTest() throws Exception {
		String sDate = "1995-09-09";
		Date dob = formatter.parse(sDate);
		File filePng = File.createTempFile("test", ".png");
		File fileJpeg = File.createTempFile("test", ".jpeg");
		File fileJpg = File.createTempFile("test", ".jpg");

		Member member1 = new Member("Nick", "Prendergast", dob, "NR14 7TP", filePng);
		Member member2 = new Member("Nick", "Prendergast", dob, "NR14 7TP", fileJpeg);
		Member member3 = new Member("Nick", "Prendergast", dob, "NR14 7TP", fileJpg);

		Set<ConstraintViolation<Member>> violations1 = validator.validate(member1);
		assertTrue(violations1.isEmpty());
		Set<ConstraintViolation<Member>> violations2 = validator.validate(member2);
		assertTrue(violations2.isEmpty());
		Set<ConstraintViolation<Member>> violations3 = validator.validate(member3);
		assertTrue(violations3.isEmpty());
	}

	@Test
	public void firstNameTooLongTest() throws Exception {
		String sDate = "2010-09-09";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		String firstName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		Member member = new Member(firstName, "Prendergast", dob, "NR14 7TP", file);

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("size must be between 1 and 30", violations.iterator().next().getMessage());
	}

	@Test
	public void firstNameTooShortTest() throws Exception {
		Member member = new Member();
		member.setFirstName("");

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertTrue(tooShortViolations(violations));
	}

	@Test
	public void futureDobTest() throws Exception {
		String sDate = "2025-09-09";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP", file);

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("must be a past date", violations.iterator().next().getMessage());
	}

	@Test
	public void invalidPictureTypeTest() throws Exception {
		String sDate = "1995-09-09";
		Date dob = formatter.parse(sDate);
		File invalidFile = File.createTempFile("test", ".exe");

		Member member1 = new Member("Nick", "Prendergast", dob, "NR14 7TP", invalidFile);

		Set<ConstraintViolation<Member>> violations1 = validator.validate(member1);
		assertEquals("Invalid image file type", violations1.iterator().next().getMessage());
	}

	@Test
	public void invalidPostCodeTest() throws Exception {
		String sDate = "1995-09-09";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		String invalidPostCode = "AAAA";
		Member member1 = new Member("Nick", "Prendergast", dob, invalidPostCode, file);

		Set<ConstraintViolation<Member>> violations1 = validator.validate(member1);
		assertEquals("Invalid UK post code", violations1.iterator().next().getMessage());

		String invalidPostCode2 = "1233223232321";
		Member member2 = new Member("Nick", "Prendergast", dob, invalidPostCode2, file);

		Set<ConstraintViolation<Member>> violations2 = validator.validate(member2);
		assertEquals("Invalid UK post code", violations2.iterator().next().getMessage());
	}

	@Test
	public void lastNameTooLongTest() throws Exception {
		Member member = new Member();
		member.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		Set<ConstraintViolation<Member>> violations3 = validator.validate(member);
		assertEquals("size must be between 1 and 30", violations3.iterator().next().getMessage());
	}

	@Test
	public void lastNameTooShortTest() throws Exception {
		Member member = new Member();
		member.setLastName("");

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertTrue(tooShortViolations(violations));
	}

	@Test
	public void nonLettersInFirstNameTest() throws Exception {
		String sDate = "2015-09-09";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member("123223+!", "Prendergast", dob, "NR14 7TP", file);

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("a name can only contain letters", violations.iterator().next().getMessage());
	}

	@Test
	public void nonLettersInLastNameTest() throws Exception {
		String sDate = "2015-09-09";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		String lastName = "123223+!";
		Member member = new Member("Nick", lastName, dob, "NR14 7TP", file);

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("a name can only contain letters", violations.iterator().next().getMessage());
	}

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	private boolean tooShortViolations(Set<ConstraintViolation<Member>> violations) {
		for (ConstraintViolation<Member> v : violations) {
			if (v.getMessage().equals("a name can only contain letters")
					|| v.getMessage().equals("size must be between 1 and 30"))
				;
			{
				return true;
			}
		}
		return false;
	}

}
