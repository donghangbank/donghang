import type {
	DemandProduct,
	DepositProduct,
	getDemandProductsAPIResponse,
	getDepositProductsAPIResponse,
	getInstallmentProductsAPIResponse,
	InstallmentProduct,
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

export const getInstallmentProductsAPI = async ({
	pageToken
}: {
	pageToken?: number;
}): Promise<getInstallmentProductsAPIResponse> => {
	const response = await localAxios.get<getInstallmentProductsAPIResponse>(
		"/accountproducts/installments",
		{
			params: { pageToken }
		}
	);
	return response.data;
};

export const getInstallmentProductAPI = async ({
	id
}: {
	id: string;
}): Promise<InstallmentProduct> => {
	const response = await localAxios.get<InstallmentProduct>(`/accountproducts/${id}`);
	return response.data;
};

export const registerInstallmentProductAPI = async ({
	memberId,
	accountProductId,
	password,
	withdrawalAccountNumber,
	payoutAccountNumber,
	amount,
	day,
	disableMasking
}: {
	memberId: number;
	accountProductId: number;
	password: string;
	withdrawalAccountNumber: string;
	payoutAccountNumber: string;
	amount: string;
	day: string;
	disableMasking: boolean;
}): Promise<registerDepositProductAPIResponse> => {
	const response = await localAxios.post<registerDepositProductAPIResponse>(
		"/accounts/installments",
		{
			memberId,
			accountProductId,
			password,
			withdrawalAccountNumber,
			payoutAccountNumber,
			monthlyInstallmentAmount: Number(amount),
			monthlyInstallmentDay: Number(day),
			disableMasking
		}
	);
	return response.data;
};

export const getDemandProductsAPI = async ({
	pageToken
}: {
	pageToken?: number;
}): Promise<getDemandProductsAPIResponse> => {
	const response = await localAxios.get<getDemandProductsAPIResponse>("/accountproducts/demands", {
		params: { pageToken }
	});
	return response.data;
};

export const getDemandProductAPI = async ({ id }: { id: string }): Promise<DemandProduct> => {
	const response = await localAxios.get<DemandProduct>(`/accountproducts/${id}`);
	return response.data;
};

export const getDemandProductByNameAPI = async ({
	name
}: {
	name: string;
}): Promise<DemandProduct> => {
	console.log("name: " + name);
	const response = await localAxios.get<DemandProduct>(`/accountproducts/search`, {
		params: { keyword: name }
	});
	return response.data;
};

export const registerDemandProductAPI = async ({
	memberId,
	accountProductId,
	password,
	disableMasking
}: {
	memberId: number;
	accountProductId: number;
	password: string;
	disableMasking: boolean;
}): Promise<registerDepositProductAPIResponse> => {
	const response = await localAxios.post<registerDepositProductAPIResponse>("/accounts/demands", {
		memberId,
		accountProductId,
		password,
		disableMasking
	});
	return response.data;
};
