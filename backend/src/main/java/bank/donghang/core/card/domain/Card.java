package bank.donghang.core.card.domain;

import java.time.LocalDateTime;

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
public class Card extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "card_product_id")
	private Long cardProductId;

	@Column(nullable = false, name = "owner_id")
	private Long ownerId;

	@Column(nullable = false, name = "account_id")
	private Long accountId;

	@Column(nullable = false, name = "card_number", length = 16, unique = true)
	private String cardNumber;

	@Column(name = "description")
	private String description;

	@Column(name = "expiry_date")
	private LocalDateTime expiryDate;

	@Column(name = "cvc", length = 3)
	private String cvc;

	@Column(nullable = false, name = "password", length = 4)
	private String password;
}
