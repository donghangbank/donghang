import type {
	DepositProduct,
	getDepositProductsAPIResponse,
	registerDepositProductAPIResponse
} from "@renderer/types/products";
import localAxios from "./http-commons";

export const getDepositProductsAPI = async ({
	pageToken
}: {
	pageToken?: number;
}): Promise<getDepositProductsAPIResponse> => {
	const response = await localAxios.get<getDepositProductsAPIResponse>(
		"/accountproducts/deposits",
		{ params: { pageToken } }
	);
	return response.data;
};

export const getDepositProductAPI = async ({ id }: { id: string }): Promise<DepositProduct> => {
	const response = await localAxios.get<DepositProduct>(`/accountproducts/${id}`);
	return response.data;
};

export const registerDepositProductAPI = async ({
	memberId,
	accountProductId,
	password,
	withdrawalAccountNumber,
	payoutAccountNumber,
	amount,
	disableMasking
}: {
	memberId: number;
	accountProductId: number;
	password: string;
	withdrawalAccountNumber: string;
	payoutAccountNumber: string;
	amount: string;
	disableMasking: boolean;
}): Promise<registerDepositProductAPIResponse> => {
	const response = await localAxios.post<registerDepositProductAPIResponse>("/accounts/deposits", {
		memberId,
		accountProductId,
		password,
		withdrawalAccountNumber,
		payoutAccountNumber,
		initDepositAmount: amount,
		disableMasking
	});
	return response.data;
};
