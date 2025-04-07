import Menu from "@renderer/components/common/general/Menu";

export const GeneralOtherPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "조회", link: "/general/inquiry/option" },
		{ prompt: "상품 가입", link: "/general/products/option" },
		{ prompt: "계좌 개설", link: "/general" },
		{ prompt: "이전", link: "/general" }
	];

	return <Menu prompts={prompts} />;
};
export default GeneralOtherPage;
