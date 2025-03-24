import { createHashRouter } from "react-router-dom";
import App from "./pages/App";
import DepositPage from "./pages/deposit/DepositPage";

// createHashRouter를 사용해 라우트를 설정
const router = createHashRouter([
	{
		path: "/",
		children: [
			{ index: true, element: <App /> },
		]
	}
]);

export default router;
