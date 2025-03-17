package bank.donghang.donghang_api.accountproduct.domain;

import bank.donghang.donghang_api.accountproduct.domain.enums.AccountProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class AccountProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "account_product_id")
    private Long accountProductId;

    @Column(nullable = false, name = "account_product_name")
    private String accountProductName;

    @Column(nullable = false, name = "account_product_description")
    private String accountProductDescription;

    @Column(nullable = false, name = "bank_id")
    private Long bankId;

    @Column(nullable = false, name = "interest_rate")
    private Double interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "account_product_type")
    private AccountProductType accountProductType;

    @Column(name = "subscription_period")
    private Long subscriptionPeriod;

    @Column(name = "rate_description")
    private String rateDescription;

    @Column(name = "min_subscription_balance")
    private Long minSubscriptionBalance;

    @Column(name = "max_subscription_balance")
    private Long maxSubscriptionBalance;

}
