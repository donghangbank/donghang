import React from "react";
import { AIProvider } from "./AIProvider";
import { UserProvider } from "./UserProvider";
import { InputProvider } from "./InputProvider";
import { PageProvider } from "./PageProvider";
import { SpecSheetProvider } from "./SpecSheetProvider";

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
	return (
		<AIProvider>
			<UserProvider>
				<InputProvider>
					<SpecSheetProvider>
						<PageProvider>{children}</PageProvider>
					</SpecSheetProvider>
				</InputProvider>
			</UserProvider>
		</AIProvider>
	);
};
