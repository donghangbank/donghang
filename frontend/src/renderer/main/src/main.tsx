import "./styles/main.css";

import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import router from "./router";
import { AppProvider } from "./contexts/AppProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
	<QueryClientProvider client={queryClient}>
		<AppProvider>
			<RouterProvider router={router} />
		</AppProvider>
	</QueryClientProvider>
);
