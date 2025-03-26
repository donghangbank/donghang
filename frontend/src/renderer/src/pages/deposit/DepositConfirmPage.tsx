import ConfirmPanel from "@renderer/components/common/ConfirmPanel";

const DepositConfirmPage = (): JSX.Element => {
	const items = [
		{ text: "수표", value: "0 원" },
		{ text: "현금", value: "50,000 원" },
		{ text: "총 금액", value: "50,000 원" }
	];

	return <ConfirmPanel title="내용이 맞나요?" items={items} />;
};

export default DepositConfirmPage;
