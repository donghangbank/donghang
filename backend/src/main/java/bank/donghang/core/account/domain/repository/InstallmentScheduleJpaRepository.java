package bank.donghang.core.account.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.InstallmentSchedule;
import bank.donghang.core.account.domain.enums.InstallmentStatus;

@Repository
public interface InstallmentScheduleJpaRepository extends JpaRepository<InstallmentSchedule, Long> {
	List<InstallmentSchedule> findInstallmentScheduleByInstallmentScheduledDateAndInstallmentStatus(LocalDate today, InstallmentStatus status);
}
