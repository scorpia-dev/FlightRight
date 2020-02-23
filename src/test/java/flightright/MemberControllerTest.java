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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

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

	@Transactional
	@Test
	public void createMemberTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");

		String json = objectMapper.writeValueAsString(member);

		mvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR14 7TP"));

		Member newMember = memberService.getMember(1L);
		assertThat(newMember.getId().equals(1L));
		assertThat(newMember.getFirstName().equals("Nick"));
		assertThat(newMember.getLastName().equals("Prendergast"));
		assertThat(newMember.getDateOfBirth().equals(dob));
		assertThat(newMember.getPostalCode().equals("NR14 7TP"));
	}

	@Transactional
	@Test
	public void updateMemberTest() throws Exception {
		String sDate = "1993-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Nick", "Prendergast", dob, "NR14 7TP");
		memberService.createMember(member);

		Long id = member.getId();

		Member savedMember = new Member();
		savedMember.setFirstName("Tom");
		savedMember.setLastName("Jones");
		savedMember.setPostalCode("NR1 1BD");
		savedMember.setDateOfBirth(dob);
		String json = objectMapper.writeValueAsString(savedMember);

		mvc.perform(put("/members/{id}", id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Tom"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Jones"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1993-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("NR1 1BD"));

		Member newMember = memberService.getMember(1L);
		assertThat(newMember.getId().equals(1L));
		assertThat(newMember.getFirstName().equals("Tom"));
		assertThat(newMember.getLastName().equals("Jones"));
		assertThat(newMember.getDateOfBirth().equals(dob));
		assertThat(newMember.getPostalCode().equals("NR11 1BD"));
	}

	@Transactional
	@Test
	public void getMemberTest() throws Exception {
		String sDate = "1973-09-21";
		Date dob = formatter.parse(sDate);
		Member member = new Member("Timmy", "Jenkins", dob, "SA14 9AS");
		memberService.createMember(member);

		mvc.perform(get("/members/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Timmy"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Jenkins"))
				.andExpect(MockMvcResultMatchers.jsonPath("dateOfBirth").value("1973-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("postalCode").value("SA14 9AS"));

		assertTrue(memberService.getMember(1L) == member);
	}

	@Transactional
	@Test
	public void listExsistingMembersTest() throws Exception {
		String sDate1 = "1973-09-21";
		String sDate2 = "1963-11-02";
		String sDate3 = "1995-01-27";

		Date dob1 = formatter.parse(sDate1);
		Date dob2 = formatter.parse(sDate2);
		Date dob3 = formatter.parse(sDate3);

		Member member1 = new Member("Timmy", "Jenkins", dob1, "SA14 9AS");
		Member member2 = new Member("John", "Cena", dob2, "WA3 8BJ");
		Member member3 = new Member("Jenny", "Mann", dob3, "IP1 9XJ");

		memberService.createMember(member1);
		memberService.createMember(member2);
		memberService.createMember(member3);

		mvc.perform(get("/members").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Timmy"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Jenkins"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value("1973-09-21"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].postalCode").value("SA14 9AS"))

				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("John"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Cena"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfBirth").value("1963-11-02"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].postalCode").value("WA3 8BJ"))

				.andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].firstName").value("Jenny"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].lastName").value("Mann"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].dateOfBirth").value("1995-01-27"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].postalCode").value("IP1 9XJ"));

		assertTrue(memberService.getAllMembers().size() == 3);
	}

	@Transactional
	@Test
	public void deleteMemberTest() throws Exception {
		String sDate = "1957-12-25";
		Date dob = formatter.parse(sDate);
		Member member = new Member("James", "Roberts", dob, "NR14 7TP");
		memberService.createMember(member);

		mvc.perform(delete("/members/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(content().string(containsString("Member with id 1 deleted")));

		assertTrue(memberService.getAllMembers().isEmpty());

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> memberService.getMember(1L));
		assertTrue(thrown.getMessage().contains("the member with id 1 was not found"));
	}

}
