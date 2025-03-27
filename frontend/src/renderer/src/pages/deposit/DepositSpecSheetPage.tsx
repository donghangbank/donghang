import ConfirmPanel from "@renderer/components/common/ConfirmPanel";

const DepositSpecSheetPage = (): JSX.Element => {
	const items = [
		{ text: "금액", value: "50,000 원" },
		{ text: "넣은 후 금액", value: "115,884 원" },
		{ text: "수수료", value: "0 원" }
	];

	return <ConfirmPanel title="명세표를 드릴까요?" items={items} link="/final" />;
};

export default DepositSpecSheetPage;
