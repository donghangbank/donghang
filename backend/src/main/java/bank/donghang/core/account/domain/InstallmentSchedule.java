package bank.donghang.core.account.domain;

import java.time.LocalDate;
import java.time.YearMonth;

import bank.donghang.core.account.domain.enums.InstallmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class InstallmentSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, name = "installment_schedule_id")
	private long installmentScheduleId;

	@Column(nullable = false, name = "installment_account_id")
	private long installmentAccountId;

	@Column(nullable = false, name = "withdrawal_account_id")
	private long withdrawalAccountId;

	@Column(nullable = false, name = "installment_amount")
	private long installmentAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "installment_status")
	private InstallmentStatus installmentStatus;

	@Column(nullable = false, name = "initial_installment_schedule_day")
	private int initialInstallmentScheduleDay;

	@Column(nullable = false, name = "installment_scheduled_date")
	private LocalDate installmentScheduledDate;

	@Column(nullable = false, name = "installment_sequence")
	private int installmentSequence;

	public InstallmentSchedule reassignInstallmentSchedule(LocalDate today) {
		LocalDate nextInstallmentScheduleDate = createNextInstallmentScheduleDate();
		setInstallmentStatus(InstallmentStatus.FAILED);
		InstallmentSchedule newInstallmentSchedule = InstallmentSchedule.builder()
			.installmentAccountId(installmentAccountId)
			.withdrawalAccountId(withdrawalAccountId)
			.initialInstallmentScheduleDay(initialInstallmentScheduleDay)
			.installmentScheduledDate(nextInstallmentScheduleDate)
			.installmentAmount(installmentAmount)
			.installmentSequence(this.getInstallmentSequence())
			.build();
		return newInstallmentSchedule;
	}

	public InstallmentSchedule createNextInstallmentScheduleBasedOnInitialDate() {
		LocalDate nextInstallmentScheduleDate = createNextInstallmentScheduleDate();

		return InstallmentSchedule.builder()
			.installmentAccountId(installmentAccountId)
			.withdrawalAccountId(withdrawalAccountId)
			.installmentStatus(InstallmentStatus.SCHEDULED)
			.initialInstallmentScheduleDay(initialInstallmentScheduleDay)
			.installmentScheduledDate(nextInstallmentScheduleDate)
			.installmentAmount(installmentAmount)
			.installmentSequence(installmentSequence + 1)
			.build();
	}

	public LocalDate createNextInstallmentScheduleDate() {

		return LocalDate.now()
			.plusMonths(1)
			.withDayOfMonth(
				Math.min(
					initialInstallmentScheduleDay,
					YearMonth
						.from(LocalDate
							.now()
							.plusMonths(1))
						.lengthOfMonth()));
	}

	private void setInstallmentStatus(InstallmentStatus status) {
		this.installmentStatus = status;
	}
}
