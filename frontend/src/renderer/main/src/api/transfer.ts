import localAxios from "./http-commons";

interface CardCheckAPIResponse {
	cardNumber: string;
	password: string;
	fullAccountNumber: string;
	ownerName: string;
	ownerId: number;
}

interface accountOwnerCheckAPIResponse {
	ownerName: string;
}

interface transferAPIResponse {
	transactionTime: string;
	recipientName: string;
	receivingAccountNumber: string;
	amount: number;
	sendingAccountBalance: number;
}

export const cardCheckAPI = async ({
	cardNumber,
	password
}: {
	cardNumber: string;
	password: string;
}): Promise<CardCheckAPIResponse> => {
	const response = await localAxios.post<CardCheckAPIResponse>("/cards/check", {
		cardNumber,
		password
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

export const transferAPI = async ({
	sendingAccountNumber,
	receivingAccountNumber,
	amount,
	description,
	sessionStartTime,
	disableMasking
}: {
	sendingAccountNumber: string;
	receivingAccountNumber: string;
	amount: string;
	description: string;
	sessionStartTime: string;
	disableMasking: boolean;
}): Promise<transferAPIResponse> => {
	const response = await localAxios.post<transferAPIResponse>("/transactions", {
		sendingAccountNumber,
		receivingAccountNumber,
		amount: Number(amount),
		description,
		sessionStartTime,
		disableMasking
	});
	return response.data;
};
