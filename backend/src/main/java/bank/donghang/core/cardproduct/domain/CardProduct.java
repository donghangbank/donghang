package bank.donghang.core.cardproduct.domain;

import bank.donghang.core.cardproduct.domain.enums.CardDuration;
import bank.donghang.core.cardproduct.domain.enums.CardProductType;
import bank.donghang.core.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private CardProductType type;

	private String description;

	private String imageUrl;

	private Long cardCompanyId;

	@Enumerated(EnumType.STRING)
	private CardDuration duration;

	private CardProduct(
		String name,
		CardProductType type,
		String description,
		String imageUrl,
		CardDuration duration,
		Long cardCompanyId
	) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.imageUrl = imageUrl;
		this.duration = duration;
		this.cardCompanyId = cardCompanyId;
	}

	public static CardProduct createCardProduct(
		String name,
		CardProductType type,
		String description,
		String imageUrl,
		CardDuration duration,
		Long cardCompanyId
	) {
		return new CardProduct(
			name,
			type,
			description,
			imageUrl,
			duration,
			cardCompanyId
		);
	}

	public void updateCardCompany(
		String name,
		CardProductType type,
		String description,
		String imageUrl,
		CardDuration duration
	) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.imageUrl = imageUrl;
		this.duration = duration;
	}
}
