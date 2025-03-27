import { createHashRouter } from "react-router-dom";
import App from "./pages/App";
import DepositPage from "./pages/deposit/DepositPage";
import DepositConfirmPage from "./pages/deposit/DepositConfirmPage";
import MainLayout from "./layouts/MainLayout";
import DepositAccountPage from "./pages/deposit/DepositAccountPage";
import { NotFoundPage } from "./pages/common/NotFoundPage";
import DepositPasswordPage from "./pages/deposit/DepositPasswordPage";
import DepositOptionPage from "./pages/deposit/DepositOptionPage";
import { DepositAuthPage } from "./pages/deposit/DepositAuthPage";
import DepositCashInputPage from "./pages/deposit/DepositCashInputPage";
import DepositCashCountingPage from "./pages/deposit/DepositCashCountingPage";
import DepositSpecSheetPage from "./pages/deposit/DepositSpecSheetPage";
import FinalPage from "./pages/common/FinalPage";

const router = createHashRouter([
	{
		path: "/",
		element: <MainLayout />,
		errorElement: <NotFoundPage />,
		children: [
			{ index: true, element: <App /> },
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
			{ path: "final", element: <FinalPage /> }
		]
	}
]);

export default router;
