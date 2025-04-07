import Menu from "@renderer/components/common/general/Menu";

export const InquiryMainPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "잔액 조회", link: "/general/balance/warning/card" },
		{ prompt: "거래 내역 조회", link: "/general/history/warning/card" }
	];

	return <Menu prompts={prompts} />;
};

export default InquiryMainPage;
