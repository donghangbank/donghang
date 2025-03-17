package bank.donghang.donghang_api.loanproduct.domain;

import bank.donghang.donghang_api.common.entity.BaseEntity;
import bank.donghang.donghang_api.common.enums.Period;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.enums.RepaymentMethod;
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
public class LoanProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bankId;

    @Column(nullable = false)
    private Long ratingId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Period period;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType type;

    @Column(nullable = false)
    private Integer minLoanBalance;

    @Column(nullable = false)
    private Integer maxLoanBalance;

    @Column(nullable = false)
    private Double interestRate;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RepaymentMethod repaymentMethod;

    public void  updateLoanProduct(
            Long ratingId,
            String name,
            Period period,
            Integer minLoanBalance,
            Integer maxLoanBalance,
            Double interestRate,
            String description
    ){
        this.ratingId = ratingId;
        this.name = name;
        this.period = period;
        this.minLoanBalance = minLoanBalance;
        this.maxLoanBalance = maxLoanBalance;
        this.interestRate = interestRate;
        this.description = description;
    }

    public static LoanProduct createLoanProduct(
            Long bankId,
            Long ratingId,
            String name,
            Period period,
            LoanType type,
            Integer minLoanBalance,
            Integer maxLoanBalance,
            Double interestRate,
            String description,
            RepaymentMethod repaymentMethod
    ){
        return new LoanProduct(
                bankId,
                ratingId,
                name,
                period,
                type,
                minLoanBalance,
                maxLoanBalance,
                interestRate,
                description,
                repaymentMethod
        );
    }

    private LoanProduct(
            Long ratingId,
            String name,
            Period period,
            Integer minLoanBalance,
            Integer maxLoanBalance,
            Double interestRate,
            String description
    ) {
        this.ratingId = ratingId;
        this.name = name;
        this.period = period;
        this.minLoanBalance = minLoanBalance;
        this.maxLoanBalance = maxLoanBalance;
        this.interestRate = interestRate;
        this.description = description;
    }

    private LoanProduct(
            Long bankId,
            Long ratingId,
            String name,
            Period period,
            LoanType type,
            Integer minLoanBalance,
            Integer maxLoanBalance,
            Double interestRate,
            String description,
            RepaymentMethod repaymentMethod
    ) {
        this.bankId = bankId;
        this.ratingId = ratingId;
        this.name = name;
        this.period = period;
        this.type = type;
        this.minLoanBalance = minLoanBalance;
        this.maxLoanBalance = maxLoanBalance;
        this.interestRate = interestRate;
        this.description = description;
        this.repaymentMethod = repaymentMethod;
    }
}
