import Menu from "@renderer/components/common/general/Menu";

export const GeneralOtherPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "조회", link: "/general/inquiry/option" },
		{ prompt: "예금 가입", link: "/general" },
		{ prompt: "적금 가입", link: "/general" },
		{ prompt: "이전", link: "/general" }
	];

	return <Menu prompts={prompts} />;
};
export default GeneralOtherPage;
