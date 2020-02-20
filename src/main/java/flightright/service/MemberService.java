package flightright.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flightright.model.Member;
import flightright.repository.MemberRepository;
import flightright.exceptions.Exception.*;


@Service
public class MemberService {

	@Autowired
	MemberRepository memberRepository;
	
	public Member createMember(@Valid Member member) {

		return memberRepository.save(member);
	}

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public Optional<Member> getMember(@Valid Long id) {
		Optional<Member> member = memberValidation(id);
		return member;
	}
	
	public Optional<Member> memberValidation(Long id) {
		Optional<Member> offer = memberRepository.findById(id);
		if (id < 1) {
			throw new ValidationException("The id can not be less than 1");
		}
		if (!offer.isPresent()) {
			throw new NotFoundException("The id: " + id + " does not exist");
		}
		return offer;
	}
	
}
