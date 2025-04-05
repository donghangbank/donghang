import { Transaction } from "@renderer/types/transaction";
import localAxios from "./http-commons";

interface balanceAPIResponse {
	accountNumber: string;
	bankName: string;
	balance: number;
}

export interface historyAPIResponse {
	pageToken: number | null;
	data: Transaction[];
	hasNext: boolean;
}

export const balanceAPI = async ({
	receivingAccountNumber,
	password
}: {
	receivingAccountNumber: string;
	password: string;
}): Promise<balanceAPIResponse> => {
	const response = await localAxios.post<balanceAPIResponse>("/accounts/balance", {
		accountNumber: receivingAccountNumber,
		password
	});
	return response.data;
};

export const historyAPI = async ({
	receivingAccountNumber,
	password,
	pageToken
}: {
	receivingAccountNumber: string;
	password: string;
	pageToken?: number;
}): Promise<historyAPIResponse> => {
	const response = await localAxios.post<historyAPIResponse>(
		"/transactions/histories",
		{ accountNumber: receivingAccountNumber, password },
		{ params: { pageToken } }
	);
	return response.data;
};
