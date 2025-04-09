import { getDemandProductByNameAPI } from "@renderer/api/products";
import TestButton from "@renderer/components/common/senior/TestButton";
import { AIContext } from "@renderer/contexts/AIContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useAudioToggle } from "@renderer/hooks/ai/useAudioToggle";
import { useQuery } from "@tanstack/react-query";
import { useContext, useEffect, useState } from "react";
import { motion } from "framer-motion";
import ProductSheet from "@renderer/components/common/senior/ProductSheet";
import { useNavigate } from "react-router-dom";
import { ProductContext } from "@renderer/contexts/ProductContext";

// Define the expected product data structure with optional fields
interface ProductData {
	bankId: number;
	bankLogoUrl: string;
	bankName: string | null;
	interestRate: number;
	maxSubscriptionBalance: number;
	minSubscriptionBalance: number;
	productDescription: string | null;
	productId: number;
	productName: string | null;
	productTypeName: string | null;
	rateDescription: string | null;
	subscriptionPeriod: number;
}

export default function SeniorDepositProductRecommendationPage(): JSX.Element {
	const { recommended_account, reason, start, stop } = useAudioToggle();
	const [isListening, setIsListening] = useState(false);
	const [recommended, setRecommended] = useState(false);
	const [showSpecSheet, setShowSpecSheet] = useState(false); // State to toggle SpecSheet
	const { setAudioStop, construction } = useContext(AIContext);
	const { setInterestRate, setMaxAmount, setProductName, setMinAmount, setAccountProductId } =
		useContext(ProductContext);
	const navigate = useNavigate();

	const { data, isError, refetch } = useQuery<ProductData>({
		queryKey: ["products", "deposit", recommended_account],
		queryFn: async () => {
			const response = await getDemandProductByNameAPI({ name: recommended_account ?? "" });
			return {
				bankId: response.bankId,
				bankLogoUrl: response.bankLogoUrl,
				bankName: response.bankName ?? null,
				interestRate: response.interestRate,
				maxSubscriptionBalance: response.maxSubscriptionBalance,
				minSubscriptionBalance: response.minSubscriptionBalance,
				productDescription: response.productDescription ?? null,
				productName: response.productName ?? null,
				productTypeName: response.productTypeName ?? null,
				rateDescription: response.rateDescription ?? null,
				subscriptionPeriod: response.subscriptionPeriod,
				productId: response.productId ?? 0
			};
		},
		enabled: false
	});

	useEffect(() => {
		if (data) {
			console.log("추천된 상품:", data);
		}
		if (isError) {
			console.warn("상품 추천 API 호출 실패");
		}
	}, [data, isError]);

	useActionPlay({
		dialogue: "어떤 예금 상품 가입하길 원하시나요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			setIsListening(true);
			setAudioStop(true);
		}
	});

	useActionPlay({
		dialogue: "원하시는 조건 말씀하시고 다 말씀하셨으면 아래 확인 버튼을 눌러주세요!",
		shouldActivate: isListening,
		avatarState: "focusBottom",
		animationDelay: 2500,
		onComplete: () => {
			setIsListening(false);
			window.mainAPI.send("set-sub-mode", "voice-manage");
			start();
		}
	});

	useActionPlay({
		dialogue: `${recommended_account} 추천드립니다!`,
		shouldActivate: !!recommended_account && !!reason,
		avatarState: "idle",
		onComplete: () => {
			setRecommended(true);
		}
	});

	useActionPlay({
		dialogue:
			"확인해보시고 해당 예금 상품에 가입하고 싶으시거나 다른 예금 상품 알아보고 싶으시면 편하게 말씀해주세요!",
		shouldActivate: showSpecSheet,
		avatarState: "idle"
	});

	useEffect(() => {
		if (recommended_account) {
			refetch();
		}
	}, [recommended_account, refetch]);

	// Handle confirm and restart from sub window
	useEffect(() => {
		const handleConfirm = (): void => {
			stop(true);
			setAudioStop(false);
			window.mainAPI.send("set-sub-mode", "default");
		};

		const handleRestart = (): void => {
			stop(false);
			start();
		};

		const unsubscribeConfirm = (): void => window.mainAPI.onCallConfirm(handleConfirm);
		const unsubscribeRestart = (): void => window.mainAPI.onCallCancel(handleRestart);

		return (): void => {
			unsubscribeConfirm?.();
			unsubscribeRestart?.();
		};
	}, [start, stop, setAudioStop]);

	useEffect(() => {
		console.log(recommended, reason, showSpecSheet, construction);
		if (recommended && reason && showSpecSheet && construction === "긍정") {
			setProductName(data?.productName ?? "");
			setInterestRate(data?.interestRate ?? 0);
			setMinAmount(data?.minSubscriptionBalance ?? 0);
			setMaxAmount(data?.maxSubscriptionBalance ?? 0);
			setAccountProductId(data?.productId ?? 0);
			navigate("/senior/depositproducts/option");
		} else if (recommended && reason && showSpecSheet && construction === "부정") {
			setShowSpecSheet(false);
			setRecommended(false);
			setIsListening(true);
		}
	}, [
		construction,
		reason,
		recommended,
		showSpecSheet,
		data,
		navigate,
		setProductName,
		setInterestRate,
		setMinAmount,
		setMaxAmount,
		setAccountProductId
	]);

	// Define sections for SpecSheet with fallback values
	const productSheetSections = data
		? [
				{ label: "상품명", value: data.productName ?? "" },
				{ label: "설명", value: data.productDescription ?? "" },
				{ label: "이자율", value: `${data.interestRate ?? 0}%` },
				{
					label: "최소 가입 금액",
					value: data.minSubscriptionBalance ?? 0,
					formatType: "amount" as const
				},
				{ label: "가입 기간", value: `${data.subscriptionPeriod ?? 0}개월` }
			]
		: [];
	console.log("데이터 뻐커" + data);
	return (
		<>
			{recommended && reason && !showSpecSheet && (
				<motion.div
					initial={{ opacity: 0, y: 20 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 0.5 }}
					className="bg-white p-5 flex flex-col gap-6 mr-24 rounded-3xl absolute right-0 top-[200px] transform -translate-y-1/2"
				>
					<div className="flex flex-col gap-6 justify-center items-start">
						<span className="text-blue text-3xl font-bold">추천 이유</span>
						<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[450px]">
							<span>{reason}</span>
						</div>
					</div>
					<div className="flex justify-center items-center">
						<button
							type="button"
							className="p-6 bg-blue rounded-xl w-full"
							onClick={() => setShowSpecSheet(true)} // Show SpecSheet on click
						>
							<span className="text-3xl text-white">자세히 보기</span>
						</button>
					</div>
				</motion.div>
			)}

			{recommended && data && showSpecSheet && (
				<div className="fixed top-64 w-full">
					<ProductSheet
						sections={productSheetSections}
						title="추천 상품 명세표"
						buttonText1="이거로 할게요"
						buttonText2="다른 상품 추천받을래요"
						width={600}
					/>
				</div>
			)}

			<TestButton
				prevRoute="/senior/depositproducts/check"
				nextRoute="/senior/depositproducts/option"
			/>
		</>
	);
}
