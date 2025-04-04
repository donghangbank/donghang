package bank.donghang.core.common.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bank.donghang.core.common.enums.MaskingType;

public class MaskingUtil {

	public static String maskingOf(MaskingType type, String value) {
		return switch (type) {
			case NAME -> nameMaskOf(value);
			case PHONE_NUMBER -> phoneNumberMaskOf(value);
			case EMAIL -> emailMaskOf(value);
			case ACCOUNT_NUMBER -> accountNumberMaskOf(value);
		};
	}

	private static String nameMaskOf(String value) {
		if (value == null || value.length() < 2) {
			return value; // 마스킹 불가능
		}
		if (value.length() == 2) {
			// 두 글자면 두 번째 글자만 마스킹
			return value.charAt(0) + "*";
		}
		// 세 글자 이상이면 중간 문자들 마스킹
		String regex = "(?<=.{1})(.*)(?=.$)";
		String maskedValue = value.replaceFirst(
			regex,
			"*".repeat(value.length() - 2)
		);
		return maskedValue;
	}

	private static String phoneNumberMaskOf(String value) {
		String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
		Matcher matcher = Pattern.compile(regex).matcher(value);
		if (matcher.find()) {
			String maskedValue = matcher.group(0).replaceAll(matcher.group(2), "****");
			return maskedValue;
		}
		return value;
	}

	private static String emailMaskOf(String value) {
		return value.replaceAll("(?<=.{3}).(?=[^@]*?@)", "*");
	}

	private static String accountNumberMaskOf(String accountNumber) {
		String regex = "(^[0-9]+)$";

		Matcher matcher = Pattern.compile(regex).matcher(accountNumber);
		if (matcher.find()) {
			int length = accountNumber.length();
			if (length > 5) {
				char[] target = new char[5];
				Arrays.fill(target, '*');

				return accountNumber.replace(accountNumber,
					accountNumber.substring(0, length - 5) + String.valueOf(target));
			}
		}
		return accountNumber;
	}
}
