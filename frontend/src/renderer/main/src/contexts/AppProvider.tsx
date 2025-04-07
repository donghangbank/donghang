import { AIProvider } from "./AIProvider";
import { UserProvider } from "./UserProvider";
import { InputProvider } from "./InputProvider";
import { PageProvider } from "./PageProvider";
import { SpecSheetProvider } from "./SpecSheetProvider";
import { ProductProvider } from "./ProductProvider";

export const AppProvider = ({ children }: { children: React.ReactNode }): JSX.Element => {
	return (
		<AIProvider>
			<UserProvider>
				<InputProvider>
					<SpecSheetProvider>
						<ProductProvider>
							<PageProvider>{children}</PageProvider>
						</ProductProvider>
					</SpecSheetProvider>
				</InputProvider>
			</UserProvider>
		</AIProvider>
	);
};
