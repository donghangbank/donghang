import { createHashRouter } from "react-router-dom";
import App from "./App";

const router = createHashRouter([
	{
		path: "/",
		children: [{ index: true, element: <App /> }]
	}
]);

export default router;
