package flightright.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import flightright.model.Member;
import flightright.service.MemberService;

@RestController
@Validated
public class MemberController implements OnCreate {

	@Autowired
	MemberService memberService;

	@Override
	@PostMapping("/members")
	public Member createMember(@Validated({ OnCreate.class }) @RequestBody Member member) {
		return memberService.createMember(member);
	}

	@DeleteMapping("/members/{id}")
	public String deleteMember(@PathVariable @Positive Long id) {
		return memberService.deleteMember(id);
	}

	@GetMapping("/members/{id}")
	public Member getMember(@PathVariable @Positive Long id) {
		return memberService.getMember(id);
	}

	@GetMapping("/members")
	public List<Member> listExistingMembers() {
		return memberService.getAllMembers();
	}

	@PutMapping("/members/{id}")
	public Member updateMember(@PathVariable @Positive Long id, @RequestBody @Valid Member member) {
		return memberService.updateMember(member, id);
	}

}
