package flightright.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import flightright.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
