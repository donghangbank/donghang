package bank.donghang.core.account.domain;

import java.time.LocalDate;

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
	@Column(unique = true, nullable = false, name = "installment_schedul_id")
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

	@Column(nullable = false, name = "installment_scheduled_date")
	private LocalDate installmentScheduledDate;

	@Column(nullable = false, name = "installment_sequence")
	private int installmentSequence;

	public InstallmentSchedule reassignInstallmentSchedule(LocalDate today) {
		setInstallmentStatus(InstallmentStatus.FAILED);
		InstallmentSchedule newInstallmentSchedule = InstallmentSchedule.builder()
			.installmentAccountId(installmentAccountId)
			.withdrawalAccountId(withdrawalAccountId)
			.installmentScheduledDate(today.plusDays(1))
			.installmentAmount(installmentAmount)
			.installmentSequence(this.getInstallmentSequence())
			.build();
		return newInstallmentSchedule;
	}

	private void setInstallmentStatus(InstallmentStatus status) {
		this.installmentStatus = status;
	}
}
