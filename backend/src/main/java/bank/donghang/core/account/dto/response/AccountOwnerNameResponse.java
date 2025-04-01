package bank.donghang.core.account.dto.response;


import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record AccountOwnerNameResponse(
        @Mask(type= MaskingType.NAME)
        String ownerName
) {
}
