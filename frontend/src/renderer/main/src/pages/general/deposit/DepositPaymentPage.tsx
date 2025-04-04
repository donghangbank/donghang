import cash from "@renderer/assets/images/cash.png";
import check from "@renderer/assets/images/check.png";
import card_plus_check from "@renderer/assets/images/cash_plus_check.png";
import Option from "@renderer/components/common/general/Option";

export const DepositPaymentPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "현금", link: "/general/deposit/cash/input", imageUrl: cash },
		{ prompt: "수표", link: "/general/deposit/cash/input", imageUrl: check },
		{ prompt: "현금+수표", link: "/general/deposit/cash/input", imageUrl: card_plus_check }
	];
	return <Option prompts={prompts} title="무엇으로 돈을 넣으시겠어요?" />;
};

export default DepositPaymentPage;
