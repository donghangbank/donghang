import localAxios from "./http-commons";

interface depositAPIResponse {
	accountNumber: string;
	amount: number;
	balance: number;
	transactionType: string;
	transactionTime: string;
}

export const depositAPI = async ({
	receivingAccountNumber,
	amount,
	sessionStartTime,
	disableMasking
}: {
	receivingAccountNumber: string;
	amount: string;
	sessionStartTime: string;
	disableMasking: boolean;
}): Promise<depositAPIResponse> => {
	const response = await localAxios.post<depositAPIResponse>("/transactions/deposit", {
		accountNumber: receivingAccountNumber,
		amount: Number(amount),
		sessionStartTime,
		disableMasking
	});
	return response.data;
};
