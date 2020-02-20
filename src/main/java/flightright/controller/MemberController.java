package flightright.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
public class MemberController {

	@Autowired
	MemberService memberService;

	@PostMapping("/members")
	public Member createMember(@RequestBody Member member) {
		return memberService.createMember(member);
	}

	@PutMapping("/members/{id}")
	public Member updateMember(@PathVariable("id") Long id, @RequestBody Member member) {
		return memberService.updateMember(member, id);
	}

	@GetMapping("/members/{id}")
	public Optional<Member> getMember(@Valid @PathVariable Long id) {
		return memberService.getMember(id);
	}

	@GetMapping("/members")
	public List<Member> listExistingMembers() {
		return memberService.getAllMembers();
	}
	
	@DeleteMapping("/members/{id}")
	public String deleteMember(@Valid @PathVariable Long id) {
		return memberService.deleteMember(id);
	}

}
