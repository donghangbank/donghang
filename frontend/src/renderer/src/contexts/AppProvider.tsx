import React from "react";
import { AIProvider } from "./AIProvider";
import { UserProvider } from "./UserProvider";
import { InputProvider } from "./InputProvider";
import { PageProvider } from "./PageProvider";

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
	return (
		<AIProvider>
			<UserProvider>
				<InputProvider>
					<PageProvider>{children}</PageProvider>
				</InputProvider>
			</UserProvider>
		</AIProvider>
	);
};
