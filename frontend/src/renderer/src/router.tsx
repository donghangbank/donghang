import { createHashRouter } from "react-router-dom";
import App from "./App";
import DepositPage from "./pages/deposit/DepositPage";

const router = createHashRouter([
	{
		path: "/",
		children: [
			{ index: true, element: <App /> },
			{ path: "deposit", element: <DepositPage /> }
		]
	}
]);

export default router;
