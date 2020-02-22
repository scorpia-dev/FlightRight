package flightright;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import flightright.controller.MemberController;
import flightright.model.Member;
import flightright.service.MemberService;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MemberControllerTest {

	private Validator validator;

	@BeforeEach
	public void setUp() {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Autowired
	private MockMvc mvc;

	@Autowired
	MemberService memberService;

	@Autowired
	ObjectMapper objectMapper;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

	@Test
	public void createMemberTest() throws Exception {

		Member member = new Member();
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setDateOfBirth(dob);
		member.setPostalCode("NR14 7TP");

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR14 7TP"));
	}

	@Test
	public void futureDobTest() throws Exception {

		Member member = new Member();
		String sDate = "2025-09-21";
		Date dob = formatter.parse(sDate);
		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setDateOfBirth(dob);
		member.setPostalCode("NR14 7TP");

		Set<ConstraintViolation<Member>> violations = validator.validate(member);
		assertEquals("must be a past date", violations.iterator().next().getMessage());
	}
	
	@Test
	public void invaidFirstNameTest() throws Exception {
		Member member = new Member();
		member.setFirstName("123223+!");

		Set<ConstraintViolation<Member>> violations1 = validator.validate(member);
		assertEquals("Name can only be letters", violations1.iterator().next().getMessage());
	
	
	}
	
}
