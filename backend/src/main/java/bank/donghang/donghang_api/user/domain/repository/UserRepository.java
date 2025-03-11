package bank.donghang.donghang_api.user.domain.repository;

import bank.donghang.donghang_api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
