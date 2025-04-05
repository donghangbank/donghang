export interface Transaction {
	transactionId: number;
	transactionTime: string;
	type: "DEPOSIT" | "WITHDRAWAL";
	description: string;
	amount: number;
	balance: number;
}
