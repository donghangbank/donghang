import { createHashRouter } from "react-router-dom";
import App from "./pages/App";
import DepositPage from "./pages/general/deposit/DepositPage";
import DepositConfirmPage from "./pages/general/deposit/DepositConfirmPage";
import MainLayout from "./layouts/MainLayout";
import DepositAccountPage from "./pages/general/deposit/DepositAccountPage";
import { NotFoundPage } from "./pages/common/NotFoundPage";
import DepositPasswordPage from "./pages/general/deposit/DepositPasswordPage";
import DepositOptionPage from "./pages/general/deposit/DepositOptionPage";
import { DepositAuthPage } from "./pages/general/deposit/DepositAuthPage";
import DepositCashInputPage from "./pages/general/deposit/DepositCashInputPage";
import DepositCashCountingPage from "./pages/general/deposit/DepositCashCountingPage";
import DepositSpecSheetPage from "./pages/general/deposit/DepositSpecSheetPage";
import FinalPage from "./pages/common/FinalPage";
import WithDrawalPage from "./pages/general/withdrawal/WithDrawalPage";
import WithDrawalCardInputPage from "./pages/general/withdrawal/WithDrawalCardInputPage";
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
import TransferScamWarning from "./pages/senior/transfer/TransferScamWarning";
import TransferOptionSeniorPage from "./pages/senior/transfer/TransferOptionPage";
import TransferCheck from "./pages/senior/transfer/transferCheck";

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
							{ index: true, element: <DepositPage /> },
							{ path: "account", element: <DepositAccountPage /> },
							{ path: "confirm", element: <DepositConfirmPage /> },
							{ path: "password", element: <DepositPasswordPage /> },
							{ path: "option", element: <DepositOptionPage /> },
							{ path: "auth", element: <DepositAuthPage /> },
							{ path: "specsheet", element: <DepositSpecSheetPage /> },
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
							}
						]
					},
					{
						path: "withdrawal",
						children: [
							{ index: true, element: <WithDrawalPage /> },
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
					{ path: "final", element: <FinalPage /> }
				]
			},
			{
				path: "senior",
				children: [
					{ path: "", element: <SeniorMain /> },
					{ path: "transfer-check", element: <TransferCheck /> },
					{ path: "transfer-scam-warning", element: <TransferScamWarning /> },
					{ path: "transfer-card-warning", element: <TransferCardWarningPage /> },
					{ path: "transfer-option", element: <TransferOptionSeniorPage /> }
				]
			}
		]
	}
]);

export default router;
