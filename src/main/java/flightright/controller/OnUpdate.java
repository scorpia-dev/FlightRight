package flightright.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import flightright.model.Member;

public interface OnUpdate  extends Default {

	
	@PutMapping("/members/{id}")
	public Member updateMember(@PathVariable @Positive Long id, @RequestBody @Valid Member member);	
}
