package bank.donghang.core.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
