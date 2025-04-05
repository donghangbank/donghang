export const inputLinkMapping: Record<string, string> = {
	"/general/transfer/card/password": "/general/transfer/info/account",
	"/general/transfer/info/account": "/general/transfer/info/amount",
	"/general/transfer/info/amount": "/general/transfer/info/specsheet",
	"/general/deposit/card/password": "/general/deposit/payment",
	"/general/withdrawal/card/password": "/general/withdrawal/payment",
	"/general/withdrawal/info/amount": "/general/drawal/cash/output",
	"/general/balance/card/password": "/general/balance/specsheet",
	"/general/history/card/password": "/general/history/specsheet",
	"/senior/transfer/card/password": "/senior/transfer/info/account",
	"/senior/transfer/info/account": "/senior/transfer/info/amount",
	"/senior/transfer/info/amount": "/senior/transfer/info/specsheet"
};

export default inputLinkMapping;
