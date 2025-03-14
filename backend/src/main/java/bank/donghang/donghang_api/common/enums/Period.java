package bank.donghang.donghang_api.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum Period {
    MONTH(30),
    QUARTER(90),
    YEAR(365);

    private long key;
}
