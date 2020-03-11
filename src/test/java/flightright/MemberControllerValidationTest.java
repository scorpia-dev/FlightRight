package flightright;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import flightright.model.Member;
import flightright.service.MemberService;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MemberControllerValidationTest {

	@Autowired
	MemberService memberService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private MockMvc mvc;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

	@Test
	public void createMemberNoDobTest() throws Exception {
		Member member = new Member();
		File file = File.createTempFile("image", ".jpg");

		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setPostalCode("NR14 7TP");
		member.setPicture(file);

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(containsString(
						"not valid due to validation error: dateOfBirth: Date of birth can not be empty")));
	}

	@Test
	public void createMemberNoLastNameTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member();
		member.setFirstName("Nick");
		member.setDateOfBirth(dob);
		member.setPostalCode("NR14 7TP");
		member.setPicture(file);

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: lastName: Last name can not be empty")));
	}

	@Test
	public void createMemberNoPictureTest() throws Exception {
		Member member = new Member();
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);

		member.setDateOfBirth(dob);
		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setPostalCode("NR14 7TP");

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(containsString("must contain a picture file")));
	}

	@Test
	public void createMemberNoPostCodeTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member();
		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setDateOfBirth(dob);
		member.setPicture(file);
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: postalCode: Postal code can not be empty")));
	}

	@Test
	public void createMemberPictureTypeTest() throws Exception {
		Member member = new Member();
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".exe");

		member.setDateOfBirth(dob);
		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setPostalCode("NR14 7TP");
		member.setPicture(file);

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content()
						.string(containsString("not valid due to validation error: picture: Invalid image file type")));
	}

	@Test
	public void deleteMemberIdDoesNotExistTest() throws Exception {
		mvc.perform(delete("/members/{id}", 3L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(print()).andExpect(content().string(
						containsString("not valid due to validation error: the member with id 3 was not found")));
	}

	@Test
	public void deleteMemberInvalidTypeTest() throws Exception {
		String invalidType = "abc";
		mvc.perform(delete("/members/{id}", invalidType).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print()).andExpect(content().string(
						containsString("not valid due to validation error: id should be of type java.lang.Long")));
	}

	@Test
	public void deleteMemberNegativeIdTest() throws Exception {
		mvc.perform(delete("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: [flightright.controller.MemberController deleteMember.id: must be greater than 0]")));
	}

	@Test
	public void getMemberIdDoesNotExist() throws Exception {
		mvc.perform(get("/members/{id}", 3L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print()).andExpect(content().string(
						containsString("not valid due to validation error: the member with id 3 was not found")));
	}

	@Test
	public void getMemberIdInvalidType() throws Exception {
		mvc.perform(get("/members/{id}", "String").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: id should be of type java.lang.Long")));
	}

	@Test
	public void getMemberNegativeIdTest() throws Exception {
		mvc.perform(
				get("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: [flightright.controller.MemberController getMember.id: must be greater than 0]")));
	}

	@Test
	public void updateMemberEmptyRequestBodyTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP", file);
		Member savedMember = memberService.createMember(member);
		Long id = savedMember.getId();

		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: Required request body is missing: public flightright.model.Member flightright.controller.MemberController.updateMember(java.lang.Long,flightright.model.Member")));
	}

	@Test
	public void updateMemberInvalidIdTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP", file);
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(put("/members/{id}", 3L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isInternalServerError()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: the member with id 3 was not found")));
	}

	@Test
	public void updateMemberInvalidPictureTypeTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP", file);
		Member savedMember = memberService.createMember(member);
		Long id = savedMember.getId();

		Member updatedMember = new Member();
		File file2 = File.createTempFile("image", ".exe");

		updatedMember.setPicture(file2);
		String json = objectMapper.writeValueAsString(updatedMember);
		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content()
						.string(containsString("not valid due to validation error: picture: Invalid image file type")));
	}

	@Test
	public void updateMemberNegativeIdTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		File file = File.createTempFile("image", ".jpg");

		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP", file);
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(put("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isInternalServerError()).andDo(print())
				.andExpect(content().string(containsString(
						"not valid due to validation error: [flightright.controller.MemberController updateMember.id: must be greater than 0]")));
	}

}
