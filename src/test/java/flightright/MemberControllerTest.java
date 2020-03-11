package flightright;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import flightright.model.Member;
import flightright.service.MemberService;

@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	MemberService memberService;

	@Autowired
	ObjectMapper objectMapper;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
	Member member;
	Member member2;
	Member member3;
	File file;
	Date dob;
	Date dob2;
	Date dob3;

	@BeforeEach
	public void setUp() throws Exception {
		String sDate = "1993-09-21";
		String sDate2 = "1963-11-02";
		String sDate3 = "1995-01-27";

		Date dob = formatter.parse(sDate);
		Date dob2 = formatter.parse(sDate2);
		Date dob3 = formatter.parse(sDate3);

		File file = File.createTempFile("image", ".jpg");
		File file2 = File.createTempFile("test2", ".png");
		File file3 = File.createTempFile("test3", ".jpeg");

		member = new Member("Nick", "Prendergast", dob, "NR14 7TP", file);
		member2 = new Member("John", "Cena", dob2, "WA3 8BJ", file2);
		member3 = new Member("Jenny", "Mann", dob3, "IP1 9XJ", file3);

		/*
		 * drivingLog=new DrivingLog(); duration1 = new Duration(1, 30, 45); duration2 =
		 * new Duration(2, 50, 12);
		 * 
		 * drivingRecord1 = new DrivingRecord(230.0, duration1, "This was a long trip");
		 * drivingRecord2 = new DrivingRecord(300.0, duration2,
		 * "This trip is even longer.");
		 * 
		 * drivingLog.addDrivingRecord(drivingRecord1);
		 * drivingLog.addDrivingRecord(drivingRecord2);
		 */

	}

	@Transactional
	@Test
	public void createMemberTest() throws Exception {

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json))

				.andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR14 7TP")).andExpect(
						MockMvcResultMatchers.jsonPath("picture").value(org.hamcrest.Matchers.containsString(".jpg")));

		Member newMember = memberService.getMember(1L);
		assertThat(newMember.getId().equals(1L));
		assertThat(newMember.getFirstName().equals("Nick"));
		assertThat(newMember.getLastName().equals("Prendergast"));
		assertThat(newMember.getDateOfBirth().equals(dob));
		assertThat(newMember.getPostalCode().equals("NR14 7TP"));
		assertThat(newMember.getPicture().equals(file));

	}

	@Transactional
	@Test
	public void deleteMemberTest() throws Exception {

		memberService.createMember(member);

		mvc.perform(delete("/members/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(content().string(containsString("Member with id 1 deleted")));

		assertTrue(memberService.getAllMembers().isEmpty());

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> memberService.getMember(1L));
		assertTrue(thrown.getMessage().contains("the member with id 1 was not found"));
	}

	@Transactional
	@Test
	public void getMemberTest() throws Exception {
		memberService.createMember(member);

		mvc.perform(get("/members/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR14 7TP")).andExpect(
						MockMvcResultMatchers.jsonPath("picture").value(org.hamcrest.Matchers.containsString(".jpg")));

		assertTrue(memberService.getMember(1L) == member);
	}

	@Transactional
	@Test
	public void listExsistingMembersTest() throws Exception {
		memberService.createMember(member);
		memberService.createMember(member2);
		memberService.createMember(member3);

		mvc.perform(get("/members").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].postalCode").value("NR14 7TP"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].picture")
						.value(org.hamcrest.Matchers.containsString(".jpg")))

				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("John"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Cena"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth").value("1963-11-02"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].postalCode").value("WA3 8BJ"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].picture")
						.value(org.hamcrest.Matchers.containsString(".png")))

				.andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].firstName").value("Jenny"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].lastName").value("Mann"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].dateOfBirth").value("1995-01-27"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].postalCode").value("IP1 9XJ"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].picture")
						.value(org.hamcrest.Matchers.containsString(".jpeg")));

		assertTrue(memberService.getAllMembers().size() == 3);
	}

	@Transactional
	@Test
	public void partialUpdateMemberTest() throws Exception {

		memberService.createMember(member);

		Long id = member.getId();

		Member savedMember = new Member();
		savedMember.setFirstName("Tom");

		String json = objectMapper.writeValueAsString(savedMember);

		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Tom"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR14 7TP"))
				.andExpect(MockMvcResultMatchers.jsonPath("picture")
						.value(org.hamcrest.Matchers.containsString(".jpg")));
		
		Member updatedMember = memberService.getMember(1L);
		assertThat(updatedMember.getId().equals(1L));
		assertThat(updatedMember.getFirstName().equals("Tom"));
		assertThat(updatedMember.getLastName().equals("Jones"));
		assertThat(updatedMember.getDateOfBirth().equals(dob));
		assertThat(updatedMember.getPostalCode().equals("NR14 7TP"));
		assertThat(updatedMember.getPicture().equals(file));

	}

	@Transactional
	@Test
	public void updateMemberAllFieldsTest() throws Exception {

		File file2 = File.createTempFile("image", ".png");

		memberService.createMember(member);

		Long id = member.getId();

		Member updatedMember = new Member();
		updatedMember.setFirstName("Tom");
		updatedMember.setLastName("Jones");
		updatedMember.setPostalCode("NR1 1BD");
		updatedMember.setDateOfBirth(dob2);
		updatedMember.setPicture(file2);
		String json = objectMapper.writeValueAsString(updatedMember);

		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Tom"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Jones"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR1 1BD"))
				.andExpect(MockMvcResultMatchers.jsonPath("picture").value(org.hamcrest.Matchers.containsString(".png")));

		Member afterSaveUpdatedMember = memberService.getMember(1L);
		assertThat(afterSaveUpdatedMember.getId().equals(id));
		assertThat(afterSaveUpdatedMember.getFirstName().equals("Tom"));
		assertThat(afterSaveUpdatedMember.getLastName().equals("Jones"));
		assertThat(afterSaveUpdatedMember.getDateOfBirth().equals(dob2));
		assertThat(afterSaveUpdatedMember.getPostalCode().equals("NR11 1BD"));
		assertThat(afterSaveUpdatedMember.getPicture().equals(file2));

	}

}
