package bank.donghang.donghang_api.member.domain.repository;

import bank.donghang.donghang_api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
