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

export const formatTransactionTime = (transactionTime: string): string => {
	const date = new Date(transactionTime);

	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	const day = date.getDate();

	let hour = date.getHours();
	const minute = `${date.getMinutes()}`.padStart(2, "0");
	const second = `${`0${date.getSeconds()}`.slice(-2)}`;

	const period = hour < 12 ? "오전" : "오후";
	hour = hour % 12 || 12;

	return `${year}년 ${month}월 ${day}일 ${period} ${hour}시 ${minute}분 ${second}초`;
};

export const formatDefault = (value: string): string => {
	return value;
};
