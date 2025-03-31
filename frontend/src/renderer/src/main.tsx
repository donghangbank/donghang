import "./styles/main.css";

import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import router from "./router";
import { UserProvider } from "./contexts/UserProvider";
import { AIProvider } from "./contexts/AIProvider";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
	<React.StrictMode>
		<AIProvider>
			<UserProvider>
				<RouterProvider router={router} />
			</UserProvider>
		</AIProvider>
	</React.StrictMode>
);
