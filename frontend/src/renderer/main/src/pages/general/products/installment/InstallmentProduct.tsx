import { getInstallmentProductAPI } from "@renderer/api/products";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { formatAmount } from "@renderer/utils/formatters";
import { useQuery } from "@tanstack/react-query";
import { useContext } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { motion } from "framer-motion";

export const InstallmentProduct = (): JSX.Element => {
	const { id } = useParams<{ id: string }>();
	const navigate = useNavigate();
	const { setAccountProductId, setMinAmount, setMaxAmount } = useContext(ProductContext);

	const { data, isSuccess, isError } = useQuery({
		queryKey: ["products", "installment", id],
		queryFn: () => getInstallmentProductAPI({ id: id ?? "" }),
		enabled: !!id
	});

	if (!data || isError) {
		return <div></div>;
	}

	const handleOnClick = (): void => {
		setAccountProductId(Number(id));
		setMinAmount(data.minSubscriptionBalance);
		setMaxAmount(data.maxSubscriptionBalance);
		navigate("/general/installmentproducts/warning/card");
	};

	return (
		<motion.div
			initial={{ opacity: 0, y: 20 }}
			animate={isSuccess ? { opacity: 1, y: 0 } : { opacity: 0 }}
			transition={{ duration: 0.5 }}
			className="flex flex-col gap-6"
		>
			<div className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom">
				<span className="text-5xl font-bold text-center">{data.productName}</span>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">상품 설명</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{data.productDescription}</span>
					</div>
				</div>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">금리</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{data.interestRate} %</span>
					</div>
				</div>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">가입 기간</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{data.subscriptionPeriod} 개월</span>
					</div>
				</div>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">최소 월 납입액</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{formatAmount(String(data.minSubscriptionBalance))}</span>
					</div>
				</div>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">최대 월 납입액</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{formatAmount(String(data.maxSubscriptionBalance))}</span>
					</div>
				</div>
			</div>
			<div className="flex justify-center items-center gap-6">
				<Link to={"/general/installmentproducts/products"} className="w-full">
					<button type="button" className="p-6 bg-red rounded-xl w-full">
						<span className="text-3xl text-white">뒤로가기</span>
					</button>
				</Link>
				<button type="button" className="p-6 bg-blue rounded-xl w-full" onClick={handleOnClick}>
					<span className="text-3xl text-white">가입하기</span>
				</button>
			</div>
		</motion.div>
	);
};

export default InstallmentProduct;
