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
	nextInstallmentScheduleDate?: string;
}

export interface getDepositProductsAPIResponse {
	pageToken: number | null;
	data: DepositProduct[];
	hasNext: boolean;
}

export interface InstallmentProduct {
	productName?: string;
	productDescription?: string;
	accountProductId: number;
	accountProductName: string;
	bankId: number;
	bankName: string;
	bankLogoUrl: string;
	interestRate: number;
	minSubscriptionBalance: number;
	maxSubscriptionBalance: number;
	subscriptionPeriod: number;
	accountProductType: string;
	productTypeCode?: number;
	productTypeName: string;
	rateDescription: string;
}

export interface getInstallmentProductsAPIResponse {
	pageToken: number | null;
	data: InstallmentProduct[];
	hasNext: boolean;
}
