package flightright;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	public void createMemberInvalidRequestBody() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);

		Member member = new Member();
		member.setFirstName("Nick");
		member.setLastName("Prendergast");
		member.setDateOfBirth(dob);
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: postalCode: Postal code can not be empty")));
	}

	@Test
	public void deleteMemberNegativeIdTest() throws Exception {
		mvc.perform(delete("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: [flightright.controller.MemberController deleteMember.id: must be greater than 0]")));
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
	public void updateMemberInvalidIdTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(put("/members/{id}", 3L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isInternalServerError()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: the member with id 3 was not found")));
	}

	@Test
	public void updateMemberNegativeIdTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(put("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isInternalServerError()).andDo(print())
				.andExpect(content().string(containsString(
						"not valid due to validation error: [flightright.controller.MemberController updateMember.id: must be greater than 0]")));
	}

	@Test
	public void updateMemberEmptyRequestBodyTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		Member savedMember = memberService.createMember(member);
		Long id = savedMember.getId();

		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: Required request body is missing: public flightright.model.Member flightright.controller.MemberController.updateMember(java.lang.Long,flightright.model.Member")));
	}

	@Test
	public void getMemberNegativeIdTest() throws Exception {

		mvc.perform(
				get("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: [flightright.controller.MemberController getMember.id: must be greater than 0]")));
	}

	@Test
	public void getMemberIdDoesNotExist() throws Exception {

		mvc.perform(get("/members/{id}", 3L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andDo(print()).andExpect(content().string(
						containsString("not valid due to validation error: the member with id 3 was not found")));
	}

	@Test
	public void getMemberIdInvalidType() throws Exception {

		mvc.perform(get("/members/{id}", "String").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: id should be of type java.lang.Long")));
	}

}
