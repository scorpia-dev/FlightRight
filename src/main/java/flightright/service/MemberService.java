package flightright.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import flightright.model.Member;
import flightright.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	MemberRepository memberRepository;
	
	public Member createMember(@Valid Member member) {

		return memberRepository.save(member);
	}

	public List<Member> getAllOffers() {
		return memberRepository.findAll();
	}
	
}
