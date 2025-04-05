import { createHashRouter } from "react-router-dom";
import App from "./pages/App";
import DepositConfirmPage from "./pages/general/deposit/DepositConfirmPage";
import MainLayout from "./layouts/MainLayout";
import { NotFoundPage } from "./pages/common/NotFoundPage";
import DepositOptionPage from "./pages/general/deposit/DepositOptionPage";
import DepositCashInputPage from "./pages/general/deposit/DepositCashInputPage";
import DepositCashCountingPage from "./pages/general/deposit/DepositCashCountingPage";
import DepositSpecSheetPage from "./pages/general/deposit/DepositSpecSheetPage";
import FinalPage from "./pages/common/FinalPage";
import WithDrawalCardAuthPage from "./pages/general/withdrawal/WithDrawalCardAuthPage";
import GeneralMain from "./pages/GeneralMain";
import SeniorMain from "./pages/SeniorMain";
import TransferScamWarningPage from "./pages/general/transfer/TransferScamWarningPage";
import TransferCardWarningPage from "./pages/general/transfer/TransferCardWarningPage";
import TransferOptionPage from "./pages/general/transfer/TransferOptionPage";
import TransferCardInputPage from "./pages/general/transfer/TransferCardInputPage";
import { TransferCardAuthPage } from "./pages/general/transfer/TransferCardAuthPage";
import TransferCardPasswordPage from "./pages/general/transfer/TransferCardPasswordPage";
import TransferInfoAccountPage from "./pages/general/transfer/TransferInfoAccountPage";
import TransferInfoAmountPage from "./pages/general/transfer/TransferInfoAmountPage";
import TransferInfoSpecSheetPage from "./pages/general/transfer/TransferInfoSpecSheetPage";
import SeniorTransferScamWarningPage from "./pages/senior/transfer/SeniorTransferScamWarningPage";
import SeniorTransferOptionSeniorPage from "./pages/senior/transfer/SeniorTransferOptionPage";
import SeniorTransferCheckPage from "./pages/senior/transfer/SeniorTransferCheckPage";
import SeniorTransferCardWaringPage from "./pages/senior/transfer/SeniorTransferCardWarningPage";
import SeniorTransferCardInputPage from "./pages/senior/transfer/SeniorTransferCardInputPage";
import SeniorTransferCheckCardPage from "./pages/senior/transfer/SeniorTransferCheckCardPage";
import SeniorTransferCardPasswordPage from "./pages/senior/transfer/SeniorTransferCardPasswordPage";
import SeniorTransferInfoAccountPage from "./pages/senior/transfer/SeniorTransferInfoAccountPage";
import SeniorTransferInfoAmountPage from "./pages/senior/transfer/SeniorTransferInfoAmountPage";
import SeniorTransferInfoSpecSheetPage from "./pages/senior/transfer/SeniorTransferInfoSpecSheetPage";
import { SeniorFinalPage } from "./pages/senior/common/SeniorFinalPage";
import DepositScamWarningPage from "./pages/general/deposit/DepositScamWarningPage";
import DepositCardWarningPage from "./pages/general/deposit/DepositCardWarning";
import DepositCardInputPage from "./pages/general/deposit/DepositCardInputPage";
import DepositCardAuthPage from "./pages/general/deposit/DepositCardAuthPage";
import DepositCardPasswordPage from "./pages/general/deposit/DepositCardPasswordPage";
import DepositPaymentPage from "./pages/general/deposit/DepositPaymentPage";
import WithDrawalScamWarningPage from "./pages/general/withdrawal/WithDrawalScamWarningPage";
import WithDrawalCardWarningPage from "./pages/general/withdrawal/WithDrawalCardWarningPage";
import WithDrawalOptionPage from "./pages/general/withdrawal/WithDrawalOptionPage";
import WithDrawalCardPasswordPage from "./pages/general/withdrawal/WithDrawalCardPasswordPage";
import WithDrawalCashOutputPage from "./pages/general/withdrawal/WithDrawalCashOutputPage";
import WithDrawalInfoAmountPage from "./pages/general/withdrawal/WithDrawalInfoAmountPage";
import WithDrawalSpecSheetPage from "./pages/general/withdrawal/WithDrawalSpecSheetPage";
import WithDrawalCardInputPage from "./pages/general/withdrawal/WithDrawalCardInputPage";
import InquiryMainPage from "./pages/general/inquiry/InquiryMainPage";
import InquiryBalanceOptionPage from "./pages/general/inquiry/balance/InquiryBalanceOptionPage";
import InquiryBalanceCardWarningPage from "./pages/general/inquiry/balance/InquiryBalanceCardWarningPage";
import InquiryBalanceCardInputPage from "./pages/general/inquiry/balance/InquiryBalanceCardInputPage";
import InquiryBalanceCardAuthPage from "./pages/general/inquiry/balance/InquiryBalanceCardAuthPage";
import InquiryBalanceCardPasswordPage from "./pages/general/inquiry/balance/InquiryBalanceCardPasswordPage";
import InquiryBalanceSpecSheetPage from "./pages/general/inquiry/balance/InquiryBalanceSpecSheetPage";

const router = createHashRouter([
	{
		path: "/",
		element: <MainLayout />,
		errorElement: <NotFoundPage />,
		children: [
			{ index: true, element: <App /> },
			{
				path: "general",
				children: [
					{
						path: "",
						element: <GeneralMain />
					},
					{
						path: "deposit",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <DepositScamWarningPage />
									},
									{
										path: "card",
										element: <DepositCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <DepositOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <DepositCardInputPage />
									},
									{
										path: "auth",
										element: <DepositCardAuthPage />
									},
									{
										path: "password",
										element: <DepositCardPasswordPage />
									}
								]
							},
							{
								path: "payment",
								element: <DepositPaymentPage />
							},
							{
								path: "cash",
								children: [
									{
										path: "input",
										element: <DepositCashInputPage />
									},
									{
										path: "counting",
										element: <DepositCashCountingPage />
									}
								]
							},
							{
								path: "confirm",
								element: <DepositConfirmPage />
							},
							{
								path: "specsheet",
								element: <DepositSpecSheetPage />
							}
						]
					},
					{
						path: "withdrawal",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <WithDrawalScamWarningPage />
									},
									{
										path: "card",
										element: <WithDrawalCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <WithDrawalOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <WithDrawalCardInputPage />
									},
									{
										path: "auth",
										element: <WithDrawalCardAuthPage />
									},
									{
										path: "password",
										element: <WithDrawalCardPasswordPage />
									}
								]
							},
							{
								path: "cash",
								children: [
									{
										path: "output",
										element: <WithDrawalCashOutputPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "amount",
										element: <WithDrawalInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <WithDrawalSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "transfer",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <TransferScamWarningPage />
									},
									{
										path: "card",
										element: <TransferCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <TransferOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <TransferCardInputPage />
									},
									{
										path: "auth",
										element: <TransferCardAuthPage />
									},
									{
										path: "password",
										element: <TransferCardPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "account",
										element: <TransferInfoAccountPage />
									},
									{
										path: "amount",
										element: <TransferInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <TransferInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{
						path: "inquiry",
						children: [
							{
								path: "option",
								element: <InquiryMainPage />
							}
						]
					},
					{
						path: "balance",
						children: [
							{
								path: "warning",
								children: [
									{
										path: "card",
										element: <InquiryBalanceCardWarningPage />
									}
								]
							},
							{
								path: "option",
								element: <InquiryBalanceOptionPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <InquiryBalanceCardInputPage />
									},
									{
										path: "auth",
										element: <InquiryBalanceCardAuthPage />
									},
									{
										path: "password",
										element: <InquiryBalanceCardPasswordPage />
									}
								]
							},
							{
								path: "specsheet",
								element: <InquiryBalanceSpecSheetPage />
							}
						]
					},
					{ path: "final", element: <FinalPage /> }
				]
			},
			{
				path: "senior",
				children: [
					{
						path: "",
						element: <SeniorMain />
					},
					{
						path: "transfer",
						children: [
							{
								path: "check",
								element: <SeniorTransferCheckPage />
							},
							{
								path: "warning",
								children: [
									{
										path: "scam",
										element: <SeniorTransferScamWarningPage />
									},
									{
										path: "card",
										element: <SeniorTransferCardWaringPage />
									}
								]
							},
							{
								path: "option",
								element: <SeniorTransferOptionSeniorPage />
							},
							{
								path: "card",
								children: [
									{
										path: "input",
										element: <SeniorTransferCardInputPage />
									},
									{
										path: "check",
										element: <SeniorTransferCheckCardPage />
									},
									{
										path: "password",
										element: <SeniorTransferCardPasswordPage />
									}
								]
							},
							{
								path: "info",
								children: [
									{
										path: "account",
										element: <SeniorTransferInfoAccountPage />
									},
									{
										path: "amount",
										element: <SeniorTransferInfoAmountPage />
									},
									{
										path: "specsheet",
										element: <SeniorTransferInfoSpecSheetPage />
									}
								]
							}
						]
					},
					{ path: "final", element: <SeniorFinalPage /> }
				]
			}
		]
	}
]);

export default router;
