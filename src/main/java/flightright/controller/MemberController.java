package flightright.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import flightright.model.Member;
import flightright.service.MemberService;

@RestController
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@PostMapping("/members")
	public Member createMember(@RequestBody Member member){
		return memberService.createMember(member);
	}
	
	@GetMapping("/members")
	public List<Member> getAllOffers() {
		return memberService.getAllOffers();
		
	}
}
