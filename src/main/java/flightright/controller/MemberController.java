package flightright.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import flightright.model.Member;
import flightright.service.MemberService;

@RestController
@Validated
public class MemberController {

	@Autowired
	MemberService memberService;

	@PostMapping("/members")
	public Member createMember(@RequestBody @Valid Member member) {
		return memberService.createMember(member);
	}

	@PutMapping("/members/{id}")
	public Member updateMember(@PathVariable @Positive Long id, @RequestBody @Valid Member member) {
		return memberService.updateMember(member, id);
	}

	@GetMapping("/members/{id}")
	public Member getMember(@PathVariable @Positive Long id) {
		return memberService.getMember(id);
	}

	@GetMapping("/members")
	public List<Member> listExistingMembers() {
		return memberService.getAllMembers();
	}
	
	@DeleteMapping("/members/{id}")
	public String deleteMember(@PathVariable @Positive Long id) {
		return memberService.deleteMember(id);
	}
	
	  @ExceptionHandler(ConstraintViolationException.class)
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
	    return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	  }

}
