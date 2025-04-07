import localAxios from "./http-commons";

interface depositAPIResponse {
	accountNumber: string;
	amount: number;
	balance: number;
	transactionType: string;
	transactionTime: string;
}

interface accountOwnerCheckAPIResponse {
	ownerName: string;
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

export const accountOwnerCheckAPI = async ({
	receivingAccountNumber
}: {
	receivingAccountNumber: string;
}): Promise<accountOwnerCheckAPIResponse> => {
	const response = await localAxios.post<accountOwnerCheckAPIResponse>("/accounts/owner", {
		accountNumber: receivingAccountNumber
	});
	return response.data;
};
