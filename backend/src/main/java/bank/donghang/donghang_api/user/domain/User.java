package bank.donghang.donghang_api.user.domain;

import bank.donghang.donghang_api.common.entity.BaseEntity;
import bank.donghang.donghang_api.user.domain.enums.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @Id
    @Column(nullable = false, name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime birthday;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
