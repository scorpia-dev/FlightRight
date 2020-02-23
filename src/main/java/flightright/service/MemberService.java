package flightright.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import flightright.model.Member;
import flightright.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	MemberRepository memberRepository;

	public Member createMember(Member member) {
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

		BeanUtils.copyProperties(updatedMember, originalMember, getNullPropertyNames(updatedMember));
		return memberRepository.save(originalMember);
	}

	private String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public String deleteMember(Long id) {
		memberRepository.delete(memberRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("the member with id " + id.toString() + " was not found")));
		return "Member with id " + id + " deleted";
	}
}
