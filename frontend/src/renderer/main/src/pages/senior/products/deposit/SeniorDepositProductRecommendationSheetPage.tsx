import { AIContext } from "@renderer/contexts/AIContext";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import check_deposit from "@renderer/assets/audios/check_deposit.mp3?url";

// Section 인터페이스 정의
interface Section {
	label: string;
	value: string | number | null;
	formatType?: "amount" | "account" | "resident" | "password" | "datetime" | "default";
}

// 포맷터 정의
const formatters: Record<NonNullable<Section["formatType"]>, (value: string) => string> = {
	amount: (value) => `${parseInt(value).toLocaleString()}원`,
	account: (value) => value.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3"),
	resident: (value) => value.replace(/(\d{6})(\d{7})/, "$1-$2"),
	password: (value) => "*".repeat(value.length),
	datetime: (value) => new Date(value).toLocaleString(),
	default: (value) => value
};

export default function SeniorDepositProductProductQuestionPage(): JSX.Element {
	const navigate = useNavigate();
	const { construction, setConstruction } = useContext(AIContext);
	const { productName, productDescription, interestRate, minAmount, period } =
		useContext(ProductContext);
	const [isDataLoaded, setIsDataLoaded] = useState(false);
	const [isVisible, setIsVisible] = useState(false);

	useEffect(() => {
		if (
			productName &&
			productDescription &&
			interestRate !== undefined &&
			minAmount !== undefined &&
			period !== undefined
		) {
			setIsDataLoaded(true);
		}
	}, [productName, productDescription, interestRate, minAmount, period]);

	useActionPlay({
		audioFile: check_deposit,
		dialogue:
			"확인해보시고 해당 예금 상품에 가입하고 싶으시거나 다른 예금 상품 알아보고 싶으시면 편하게 말씀해주세요!",
		shouldActivate: isDataLoaded,
		avatarState: "idle",
		onComplete: () => {
			setConstruction("etc");
			setIsVisible(true);
		}
	});

	// 음성 인식 결과에 따라 페이지 이동
	useEffect(() => {
		if (!isVisible) return; // 명세표가 표시되기 전에는 동작하지 않음
		if (construction === "긍정") {
			navigate("/senior/depositproducts/option");
		} else if (construction === "부정") {
			navigate("/senior/depositproducts/recommendation/feature");
		}
	}, [isVisible, construction, navigate]);

	// 상품 데이터 섹션 정의
	const productSheetSections: Section[] = [
		{ label: "상품명", value: productName ?? "" },
		{ label: "설명", value: productDescription ?? "" },
		{ label: "이자율", value: `${interestRate ?? 0}%` },
		{ label: "최소 가입 금액", value: minAmount ?? 0, formatType: "amount" },
		{ label: "가입 기간", value: `${period ?? 0}개월` }
	];

	return (
		<div className="flex w-full h-full justify-end items-center">
			{isDataLoaded && isVisible && (
				<motion.div
					initial={{ opacity: 0, y: 20 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 0.5 }}
					className="bg-white p-5 flex flex-col gap-6 mr-24 rounded-3xl"
				>
					{productSheetSections.map((section, index) => {
						const raw = section.value?.toString() ?? "";
						const formatter = formatters[section.formatType ?? "default"];
						const formattedValue = formatter(raw);

						return (
							<div key={index} className="flex flex-col gap-6 justify-center items-start">
								<span className="text-blue text-3xl font-bold">{section.label}</span>
								<div
									className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold"
									style={{ width: "400px" }} // 기본 너비 400px 유지
								>
									<span>{formattedValue}</span>
								</div>
							</div>
						);
					})}
				</motion.div>
			)}
		</div>
	);
}
