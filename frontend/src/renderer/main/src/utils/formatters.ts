export const formatAccountNumber = (value: string): string => {
	if (value.length <= 3) {
		return value;
	}
	if (value.length <= 6) {
		return `${value.slice(0, 3)}-${value.slice(3)}`;
	}
	return `${value.slice(0, 3)}-${value.slice(3, 6)}-${value.slice(6)}`;
};

export const formatResidentNumber = (value: string): string => {
	if (value.length <= 6) {
		return value;
	}
	const visiblePart = `${value.slice(0, 6)}-${value.slice(6, 7)}`;
	const maskedPart = "*".repeat(value.length - 7);
	return visiblePart + maskedPart;
};

export const formatPassword = (value: string): string => {
	return "*".repeat(value.length);
};

export const formatAmount = (value: string): string => {
	const number = Number.parseInt(value.replace(/,/g, ""), 10);
	return Number.isNaN(number) ? "" : `${number.toLocaleString()} 원`;
};
