package flightright.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
		Optional<Member> member = memberRepository.findById(id);
		if (id < 1) {
			throw new ValidationException("The id can not be less than 1");
		}
		if (!member.isPresent()) {
			throw new NotFoundException("The id: " + id + " does not exist");
		}
		return member;
	}

	public Member updateMember(Member updatedMember, Long id) {

		Member originalMember = memberRepository.getOne(id);

		BeanUtils.copyProperties(originalMember, updatedMember);

		return memberRepository.save(originalMember);
	}

	public String deleteMember(@Valid Long id) {
		Member member = memberRepository.getOne(id);
		memberRepository.delete(member);
		return "member with id " +id+ " deleted";
	}

}
