package bank.donghang.donghang_api.member.domain;

import bank.donghang.donghang_api.common.entity.BaseEntity;
import bank.donghang.donghang_api.member.domain.enums.MemberStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @Column(nullable = false, name = "member_id")
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
    private MemberStatus memberStatus;

    public static Member of(
            String name,
            String email,
            String phoneNumber,
            LocalDateTime birthday,
            String address,
            String postNumber,
            MemberStatus memberStatus
    ) {
        return new Member(name, email, phoneNumber, birthday, address, postNumber, memberStatus);
    }

    private Member(
            String name,
            String email,
            String phoneNumber,
            LocalDateTime birthday,
            String address,
            String postNumber,
            MemberStatus memberStatus
    ) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.postNumber = postNumber;
        this.memberStatus = memberStatus;
    }
}
