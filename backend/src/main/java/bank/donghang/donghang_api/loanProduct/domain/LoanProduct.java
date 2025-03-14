package bank.donghang.donghang_api.loanProduct.domain;

import bank.donghang.donghang_api.common.entity.BaseEntity;
import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.domain.enums.RepaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Period;

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

    private Period period;

    @Column(nullable = false)
    private LoanType type;

    @Column(nullable = false)
    Integer minLoanBalance;

    @Column(nullable = false)
    Integer maxLoanBalance;

    /* TODO: 이거는 따로 정하는게 좋을듯 */
    @Column(nullable = false)
    Double interestRate;

    String description;

    @Column(nullable = false)
    RepaymentMethod repaymentMethod;

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
