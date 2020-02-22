package flightright.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.BeanUtils;
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

	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	public Member getMember(Long id) {

		return memberRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("the member with id " + id.toString() + " was not found"));
	}

	public Member updateMember(Member updatedMember, Long id) {

		Member originalMember = memberRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("the member with id " + id.toString() + " was not found"));

		BeanUtils.copyProperties(originalMember, updatedMember);

		return memberRepository.save(originalMember);
	}

	public String deleteMember(@Valid Long id) {

		memberRepository.delete(memberRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("the member with id " + id.toString() + " was not found")));

		return "Member with id " +id+ " deleted";
	}
}
