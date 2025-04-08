import { useEffect, useState } from "react";
import { getDepositProductsAPI } from "@renderer/api/products";
import { formatAmount } from "@renderer/utils/formatters";
import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import type { DepositProduct, getDepositProductsAPIResponse } from "@renderer/types/products";

export const DepositProducts = (): JSX.Element => {
	const [allProducts, setAllProducts] = useState<DepositProduct[]>([]);
	const [currentPage, setCurrentPage] = useState(0);
	const [pageTokens, setPageTokens] = useState<(number | null)[]>([null]);
	const [hasNext, setHasNext] = useState(false);

	const { data, isError, isFetching, isSuccess } = useQuery<getDepositProductsAPIResponse, Error>({
		queryKey: ["products", "deposit", pageTokens[currentPage]],
		queryFn: () => getDepositProductsAPI({ pageToken: pageTokens[currentPage] ?? undefined })
	});

	useEffect(() => {
		return (): void => {
			setAllProducts([]);
			setCurrentPage(0);
			setPageTokens([null]);
			setHasNext(false);
		};
	}, []);

	useEffect(() => {
		if (isSuccess && data) {
			if (currentPage === pageTokens.length - 1) {
				const newProducts = data.data.filter(
					(product) => !allProducts.some((p) => p.accountProductId === product.accountProductId)
				);
				setAllProducts((prev) => [...prev, ...newProducts]);
				setPageTokens((prev) => [...prev, data.pageToken]);
				setHasNext(data.hasNext);
			}
		}
	}, [isSuccess, data, currentPage, pageTokens, allProducts]);

	const handleNext = (): void => {
		if ((currentPage + 1) * 3 >= setAllProducts.length && hasNext) {
			setCurrentPage((prev) => prev + 1);
		} else {
			setCurrentPage((prev) => prev + 1);
		}
	};

	const handlePrevious = (): void => {
		setCurrentPage((prev) => Math.max(prev - 1, 0));
	};

	const visibleDepositProducts = allProducts.slice(currentPage * 4, (currentPage + 1) * 4);

	if (isError) {
		return <div>데이터를 불러오는 데 실패했습니다</div>;
	}

	if (!data) {
		return <div>예금상품이 존재하지 않습니다</div>;
	}
	return (
		<div className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom">
			<span className="text-4xl font-bold text-center">예금 상품 목록</span>

			{data && (
				<div className="flex flex-col gap-4 min-h-[528px]">
					{visibleDepositProducts.map((product) => (
						<Link
							to={`/general/depositproducts/${product.accountProductId}`}
							key={product.accountProductId}
						>
							<div
								key={product.accountProductId}
								className="w-[600px] flex justify-between items-center p-6 bg-cloudyBlue rounded-3xl shadow-custom"
							>
								<div className="flex flex-col gap-2">
									<span className="text-2xl font-bold text-gray-800">
										{product.accountProductName}
									</span>
									<span className="text-lg font-bold text-gray-600">
										{product.subscriptionPeriod} 개월
									</span>
								</div>
								<div className="flex flex-col items-end gap-2">
									<span className="text-3xl font-bold text-blue">
										<span className="text-2xl">연</span> {product.interestRate}%
									</span>
									<span className="text-xl font-bold">
										최소 {formatAmount(String(product.minSubscriptionBalance))}
									</span>
								</div>
							</div>
						</Link>
					))}
				</div>
			)}

			<div className="grid grid-cols-3 justify-center items-center gap-4">
				<button
					type="button"
					className="p-6 bg-gray-900 rounded-xl disabled:bg-gray-400 disabled:cursor-not-allowed flex-1"
					onClick={handlePrevious}
					disabled={currentPage === 0 || isFetching}
				>
					<span className="text-3xl text-white">이전</span>
				</button>
				<button
					type="button"
					className="p-6 bg-gray-900 rounded-xl disabled:bg-gray-400 disabled:cursor-not-allowed flex-1"
					onClick={handleNext}
					disabled={(!hasNext && (currentPage + 1) * 4 >= allProducts.length) || isFetching}
				>
					<span className="text-3xl text-white">다음</span>
				</button>
				<Link to={"/general/final"}>
					<button type="button" className="p-6 bg-red rounded-xl w-full">
						<span className="text-3xl text-white">취소</span>
					</button>
				</Link>
			</div>
		</div>
	);
};

export default DepositProducts;
