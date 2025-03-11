package bank.donghang.donghang_api.user.application;

import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import bank.donghang.donghang_api.user.domain.User;
import bank.donghang.donghang_api.user.domain.repository.UserRepository;
import bank.donghang.donghang_api.user.dto.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDetailResponse findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        return UserDetailResponse.from(user);
    }
}
