package bank.donghang.core.cardcompany.domain;

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
public class CardCompany extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String logoUrl;

	private CardCompany(
		String name,
		String logoUrl
	) {
		this.name = name;
		this.logoUrl = logoUrl;
	}

	public static CardCompany createCardCompany(
		String name,
		String logoUrl
	) {
		return new CardCompany(
			name,
			logoUrl
		);
	}

	public void updateCardCompany(
		String name,
		String logoUrl
	) {
		this.name = name;
		this.logoUrl = logoUrl;
	}
}
