import { getDemandProductAPI } from "@renderer/api/products";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useQuery } from "@tanstack/react-query";
import { useContext } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

export const DemandProduct = (): JSX.Element => {
	const { id } = useParams<{ id: string }>();
	const navigate = useNavigate();
	const { setAccountProductId } = useContext(ProductContext);

	const { data, isError } = useQuery({
		queryKey: ["products", "demand", id],
		queryFn: () => getDemandProductAPI({ id: id ?? "" }),
		enabled: !!id
	});

	if (!data || isError) {
		return <div>데이터를 불러오는 데 실패했습니다</div>;
	}

	const handleOnClick = (): void => {
		setAccountProductId(Number(id));
		navigate("/general/demandproducts/warning/residentwarning");
	};

	return (
		<div className="flex flex-col gap-6">
			<div className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom">
				<span className="text-5xl font-bold text-center">{data.productName}</span>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">상품 설명</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{data.productDescription}</span>
					</div>
				</div>
				<div className="flex justify-between gap-20 items-center">
					<span className="text-blue text-3xl font-bold">연 금리</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
						<span>{data.interestRate} %</span>
					</div>
				</div>
			</div>
			<div className="flex justify-center items-center gap-6">
				<Link to={"/general/demandproducts/products"} className="w-full">
					<button type="button" className="p-6 bg-red rounded-xl w-full">
						<span className="text-3xl text-white">뒤로가기</span>
					</button>
				</Link>
				<button type="button" className="p-6 bg-blue rounded-xl w-full" onClick={handleOnClick}>
					<span className="text-3xl text-white">계좌 개설하기</span>
				</button>
			</div>
		</div>
	);
};

export default DemandProduct;
