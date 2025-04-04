import localAxios from "./http-commons";

interface withdrawalAPIResponse {
	accountNumber: string;
	amount: number;
	balance: number;
	transactionType: string;
	transactionTime: string;
}

export const withdrawalAPI = async ({
	receivingAccountNumber,
	amount,
	sessionStartTime,
	disableMasking
}: {
	receivingAccountNumber: string;
	amount: string;
	sessionStartTime: string;
	disableMasking: boolean;
}): Promise<withdrawalAPIResponse> => {
	const response = await localAxios.post<withdrawalAPIResponse>("/transactions/withdrawal", {
		accountNumber: receivingAccountNumber,
		amount: Number(amount),
		sessionStartTime,
		disableMasking
	});
	return response.data;
};
