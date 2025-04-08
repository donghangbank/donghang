export function getMaxLength(type?: string): number {
	switch (type) {
		case "account":
			return 12;
		case "resident":
			return 13;
		case "password":
			return 4;
		case "amount":
			return 11;
		default:
			return 2;
	}
}

export function calculateNewValue(prevValue: string, input: string, type?: string): string {
	let newValue = prevValue;

	// (1) 만약 type이 amount이고 현재 값이 "0"이라면, 새로운 숫자로 대체
	if (type === "amount" && prevValue === "0") {
		newValue = input;
	}
	// (2) 길이 제한
	else if (prevValue.length < getMaxLength(type)) {
		newValue = prevValue + input;
	}

	return newValue;
}
