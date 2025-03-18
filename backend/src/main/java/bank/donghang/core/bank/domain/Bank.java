package bank.donghang.core.bank.domain;

import bank.donghang.core.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bank extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String logoUrl;

	private Bank(
		String name,
		String logoUrl
	) {
		this.name = name;
		this.logoUrl = logoUrl;
	}

	public static Bank updateBank(
		String name,
		String logoUrl
	) {
		return new Bank(
			name,
			logoUrl
		);
	}

	public static Bank createBank(
		String name,
		String logoUrl
	) {
		return new Bank(
			name,
			logoUrl
		);
	}
}
