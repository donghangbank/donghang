export interface DepositProduct {
	accountProductId: number;
	accountProductName: string;
	productName?: string;
	accountProductType: string;
	bankId: number;
	interestRate: number;
	maxSubscriptionBalance: number;
	minSubscriptionBalance: number;
	subscriptionPeriod: number;
	productDescription?: string;
}

export interface registerDepositProductAPIResponse {
	productName: string;
	withdrawalAccountNumber: string;
	payoutAccountNumber: string;
	accountNumber: string;
	accountBalance: number;
	interestRate: number;
	accountExpiryDate: string;
}

export interface getDepositProductsAPIResponse {
	pageToken: number | null;
	data: DepositProduct[];
	hasNext: boolean;
}
