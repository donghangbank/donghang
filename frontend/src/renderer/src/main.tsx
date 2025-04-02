import "./styles/main.css";

import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import router from "./router";
import { UserProvider } from "./contexts/UserProvider";
import { AIProvider } from "./contexts/AIProvider";
import { InputProvider } from "./contexts/InputProvider";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
	<React.StrictMode>
		<AIProvider>
			<UserProvider>
				<InputProvider>
					<RouterProvider router={router} />
				</InputProvider>
			</UserProvider>
		</AIProvider>
	</React.StrictMode>
);
