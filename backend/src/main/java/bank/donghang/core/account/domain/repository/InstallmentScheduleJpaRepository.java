package bank.donghang.core.account.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.InstallmentSchedule;

@Repository
public interface InstallmentScheduleJpaRepository extends JpaRepository<InstallmentSchedule, Long> {

}
