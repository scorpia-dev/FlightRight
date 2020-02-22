package flightright;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void futureDobTest() throws Exception {
		String sDate = "2025-09-09";
		Date dob = formatter.parse(sDate);
		Member member = new Member();
		member.setDateOfBirth(dob);

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("must be a past date", violations.iterator().next().getMessage());
	}
	
	@Test
	public void nonLettersInFirstNameTest() throws Exception {
		Member member = new Member();
		member.setFirstName("123223+!");

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("a name can only contain letters", violations.iterator().next().getMessage());
	}
	
	@Test
	public void firstNameTooLongTest() throws Exception {
		Member member = new Member();
		member.setFirstName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

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
	public void nonLettersInLastNameTest() throws Exception {
		Member member = new Member();
		member.setLastName("123223+!");

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("a name can only contain letters", violations.iterator().next().getMessage());
	}
	
	@Test
	public void lastNameTooLongTest() throws Exception {
		Member member = new Member();
		member.setLastName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("size must be between 1 and 30", violations.iterator().next().getMessage());
	}
	
	@Test
	public void lastNameTooShortTest() throws Exception {
		Member member = new Member();
		member.setLastName("");
	
		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertTrue(tooShortViolations(violations));
	}
	
	private boolean tooShortViolations(Set<ConstraintViolation<Member>> violations) {
		for (ConstraintViolation<Member> v: violations) {
			if (v.getMessage().equals("a name can only contain letters") || 
					v.getMessage().equals("size must be between 1 and 30"));
			{
			return true;
			}
		}
		return false;
	}
	
	@Test
	public void invalidPostCodeTest() throws Exception {
		Member member1 = new Member();
		member1.setPostalCode("aaa");
		Set<ConstraintViolation<Member>> violations1 = validator.validate(member1);
		assertEquals("Invalid UK postcode", violations1.iterator().next().getMessage());
		
		Member member2= new Member();
		member2.setPostalCode("1233223232321");
		Set<ConstraintViolation<Member>> violations2 = validator.validate(member2);
		assertEquals("Invalid UK postcode", violations2.iterator().next().getMessage());
	}

}
