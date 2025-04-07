import Menu from "@renderer/components/common/general/Menu";

export const GeneralMain = (): JSX.Element => {
	const prompts = [
		{ prompt: "입금", link: "/general/deposit/warning/scam" },
		{ prompt: "출금", link: "/general/withdrawal/warning/scam" },
		{ prompt: "이체", link: "/general/transfer/warning/scam" },
		{ prompt: "기타", link: "/general/others" }
	];

	return <Menu prompts={prompts} />;
};

export default GeneralMain;
