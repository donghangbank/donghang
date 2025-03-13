package bank.donghang.donghang_api.cardproduct.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum CardDuration {
    MONTH(30),
    HALF(180),
    YEAR(365);

    private long key;
}
