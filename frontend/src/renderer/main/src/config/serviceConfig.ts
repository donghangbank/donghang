interface StepConfig {
	path: string;
	name: string;
}

interface ServiceConfig {
	title: string;
	steps: StepConfig[];
}

const SERVICE_CONFIG: Record<string, ServiceConfig> = {
	transfer: {
		title: "이체",
		steps: [
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "info", name: "이체 정보" },
			{ path: "specsheet", name: "명세표 확인" },
			{ path: "final", name: "이체 완료" }
		]
	},
	deposit: {
		title: "입금",
		steps: [
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "인증 수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "payment", name: "입금 수단 선택" },
			{ path: "cash", name: "현금 투입" },
			{ path: "confirm", name: "내용 확인" },
			{ path: "specsheet", name: "명세표 확인" },
			{ path: "final", name: "입금 완료" }
		]
	},
	withdrawal: {
		title: "출금",
		steps: [
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "인증 수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "amount", name: "금액 입력" },
			{ path: "cash", name: "현금 출금" },
			{ path: "specsheet", name: "명세표 확인" },
			{ path: "final", name: "출금 완료" }
		]
	},
	inquiry: {
		title: "조회",
		steps: [{ path: "option", name: "선택" }]
	},
	balance: {
		title: "잔액 조회",
		steps: [
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "인증 수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "specsheet", name: "잔액 확인" },
			{ path: "final", name: "완료" }
		]
	},
	history: {
		title: "거래 내역 조회",
		steps: [
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "인증 수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "specsheet", name: "거래 내역 확인" },
			{ path: "final", name: "완료" }
		]
	},
	products: {
		title: "상품 가입",
		steps: [{ path: "option", name: "상품 선택" }]
	},
	depositproducts: {
		title: "예금 상품 가입",
		steps: [
			{ path: "products", name: "예금 상품 목록" },
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "인증 수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "amount", name: "예치금 입력" },
			{ path: "specsheet", name: "예금 상품 가입" },
			{ path: "final", name: "완료" }
		]
	},
	installmentproducts: {
		title: "적금 상품 가입",
		steps: [
			{ path: "products", name: "적금 상품 목록" },
			{ path: "warning", name: "금융 사기" },
			{ path: "option", name: "인증 수단 선택" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "amount", name: "월 납입액 입력" },
			{ path: "specsheet", name: "적금 상품 가입" },
			{ path: "final", name: "완료" }
		]
	},
	demandproducts: {
		title: "입출금 계좌 개설",
		steps: [
			{ path: "products", name: "입출금 계좌 목록" },
			{ path: "warning", name: "금융 사기" },
			{ path: "resident", name: "신분증 투입" },
			{ path: "password", name: "비밀번호 입력" },
			{ path: "specsheet", name: "적금 상품 가입" },
			{ path: "final", name: "완료" }
		]
	}
};

export default SERVICE_CONFIG;
