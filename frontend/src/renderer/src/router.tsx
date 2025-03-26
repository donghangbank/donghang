import { createHashRouter } from "react-router-dom";
import App from "./pages/App";
import DepositPage from "./pages/deposit/DepositPage";
import DepositConfirmPage from "./pages/deposit/DepositConfirmPage";

const router = createHashRouter([
	{
		path: "/",
		children: [
			{ index: true, element: <App /> },
			{ path: "deposit", element: <DepositPage /> },
			{ path: "deposit/confirm", element: <DepositConfirmPage /> }
		]
	}
]);

export default router;
