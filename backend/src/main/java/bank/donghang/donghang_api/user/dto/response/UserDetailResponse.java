package bank.donghang.donghang_api.user.dto.response;

import bank.donghang.donghang_api.user.domain.User;
import bank.donghang.donghang_api.user.domain.enums.UserStatus;

public record UserDetailResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        UserStatus userStatus
) {
    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUserStatus()
        );
    }
}
