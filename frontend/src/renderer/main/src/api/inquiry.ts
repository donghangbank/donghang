import localAxios from "./http-commons";

interface balanceAPIResponse {
	accountNumber: string;
	bankName: string;
	balance: number;
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
