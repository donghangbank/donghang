import Menu from "@renderer/components/common/general/Menu";

export const GeneralProductsPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "예금 가입", link: "/general/depositproducts/products" },
		{ prompt: "적금 가입", link: "/general/installmentproducts/products" }
	];

	return <Menu prompts={prompts} />;
};
export default GeneralProductsPage;
