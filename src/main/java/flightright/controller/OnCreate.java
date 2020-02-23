package flightright.controller;

import javax.validation.Valid;
import javax.validation.groups.Default;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import flightright.model.Member;

public interface OnCreate  extends Default {

	
	@PostMapping("/members")
	public Member createMember(@RequestBody @Valid Member member);
	
}
