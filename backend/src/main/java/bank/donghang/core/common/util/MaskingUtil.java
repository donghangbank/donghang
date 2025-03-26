package bank.donghang.core.common.util;

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

	private static String accountNumberMaskOf(String value) {
		return value.replaceAll("(?<=.{3}).(?=[^@]*?@)", "*");
	}
}
