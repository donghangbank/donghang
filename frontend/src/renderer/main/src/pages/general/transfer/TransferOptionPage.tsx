import card from "@renderer/assets/images/card.png";
import bankbook from "@renderer/assets/images/bankbook.png";
import numberpad from "@renderer/assets/images/numberpad.png";
import Option from "@renderer/components/common/general/Option";

export const TransferOptionPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "카드", link: "/general/transfer/card/input", imageUrl: card },
		{ prompt: "통장", link: "/general/transfer/card/input", imageUrl: bankbook },
		{ prompt: "계좌번호", link: "/general/transfer/account/input", imageUrl: numberpad }
	];
	return <Option prompts={prompts} title="무엇을 가져오셨나요?" />;
};

export default TransferOptionPage;
