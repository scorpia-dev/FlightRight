package flightright;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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


	//@Test
	public void deleteMemberNegativeIdTest() throws Exception {
		mvc.perform(delete("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
		.andDo(print()).andExpect(content().string(containsString("not valid due to validation error: deleteMember.id: must be greater than 0")));
	}
	
	//@Test
	public void deleteMemberInvalidIdTest() throws Exception {
		mvc.perform(delete("/members/{id}", 3L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
		.andDo(print()).andExpect(content().string(containsString("not valid due to validation error: the member with id 3 was not found")));
	}
	
	@Test
	public void deleteMemberInvalidTypeTest() throws Exception {
		String invalidType = "abc";
		
		mvc.perform(delete("/members/{id}", invalidType).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
		.andDo(print()).andExpect(content().string(containsString("")));
	}
	
	//@Test
	public void updateMemberInvalidIdTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(put("/members/{id}",3L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(content().string(containsString("not valid due to validation error: the member with id 3 was not found")));
	}
	
	//@Test
	public void updateMemberNegativeIdTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		String json = objectMapper.writeValueAsString(member);

		mvc.perform(put("/members/{id}",-1L).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isInternalServerError()).andDo(print())
		.andExpect(content().string(containsString("not valid due to validation error: updateMember.id: must be greater than 0")));
	}
	
	//@Test
	public void updateMemberInvalidRequestBodyTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		Member savedMember = memberService.createMember(member);
		Long id = savedMember.getId();
		
		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
		.andDo(print()).andExpect(content().string(containsString("not valid due to validation error: Required request body is missing: public flightright.model.Member flightright.controller.MemberController.updateMember(java.lang.Long,flightright.model.Member")));
	}
	

/*	
 * 
 * 	@PutMapping("/members/{id}")
	public Member updateMember(@PathVariable @Positive Long id, @RequestBody @Valid Member member) {
		return memberService.updateMember(member, id);
	}
	@Test
	public void deleteMemberNegativeIdTest() throws Exception {
		mvc.perform(delete("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
		.andDo(print()).andExpect(content().string(containsString("not valid due to validation error: deleteMember.id: must be greater than 0")));
	}
	
	@Test
	public void deleteMemberNegativeIdTest() throws Exception {
		mvc.perform(delete("/members/{id}", -1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
		.andDo(print()).andExpect(content().string(containsString("not valid due to validation error: deleteMember.id: must be greater than 0")));
	}*/
			
}
