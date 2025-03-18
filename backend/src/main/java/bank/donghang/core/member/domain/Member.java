package bank.donghang.core.member.domain;

import java.time.LocalDateTime;

import bank.donghang.core.common.entity.BaseEntity;
import bank.donghang.core.member.domain.enums.MemberStatus;
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
}
