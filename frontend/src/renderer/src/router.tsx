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
					{ path: "final", element: <FinalPage /> }
				]
			},
			{
				path: "senior",
				children: [{ path: "", element: <SeniorMain /> }]
			}
		]
	}
]);

export default router;
