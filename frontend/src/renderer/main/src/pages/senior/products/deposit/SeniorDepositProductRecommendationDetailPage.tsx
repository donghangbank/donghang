import { getDemandProductByNameAPI } from "@renderer/api/products";
import ProductSheet from "@renderer/components/common/senior/ProductSheet";
import { AIContext } from "@renderer/contexts/AIContext";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useQuery } from "@tanstack/react-query";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

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

export default function SeniorDepositProductRecommendationDetailPage(): JSX.Element {
	const navigate = useNavigate();
	const { recommendAccount } = useContext(AIContext);
	const { construction, setConstruction } = useContext(AIContext);
	const [trigger, setTrigger] = useState(false);
	const {
		setProductDescription,
		setProductName,
		setInterestRate,
		setMinAmount,
		setMaxAmount,
		setAccountProductId,
		setPeriod
	} = useContext(ProductContext);

	const { data, refetch } = useQuery<ProductData>({
		queryKey: ["products", "deposit", recommendAccount],
		queryFn: async () => {
			const response = await getDemandProductByNameAPI({ name: recommendAccount ?? "" });
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
		if (!recommendAccount) return;
		refetch();
	}, [recommendAccount, refetch]);

	useEffect(() => {
		if (data) {
			setProductName(data.productName ?? "");
			setProductDescription(data.productDescription ?? "");
			setInterestRate(data.interestRate ?? 0);
			setMinAmount(data.minSubscriptionBalance ?? 0);
			setMaxAmount(data.maxSubscriptionBalance ?? 0);
			setAccountProductId(data.productId ?? 0);
			setPeriod(data.subscriptionPeriod ?? 0);
		}
	}, [
		data,
		navigate,
		setProductName,
		setInterestRate,
		setMinAmount,
		setMaxAmount,
		setAccountProductId,
		setProductDescription,
		setPeriod
	]);

	useActionPlay({
		dialogue: `${recommendAccount} 상품의 세부 정보 입니다! 만드시겠어요?`,
		shouldActivate: !!recommendAccount,
		avatarState: "idle",
		onComplete: () => {
			setConstruction("etc");
			setTrigger(true);
		}
	});

	useEffect(() => {
		if (!trigger) return;
		if (construction === "긍정") {
			navigate("/senior/depositproducts/option");
		} else if (construction === "부정") {
			navigate("/senior/depositproducts/recommendation/feature");
		}
	}, [trigger, construction, navigate]);

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

	return (
		<>
			{recommendAccount && (
				<div className="fixed top-52 w-full">
					<ProductSheet sections={productSheetSections} title="추천 상품 명세표" width={600} />
				</div>
			)}
		</>
	);
}
