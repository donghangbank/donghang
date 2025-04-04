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
	}
};

export default SERVICE_CONFIG;
