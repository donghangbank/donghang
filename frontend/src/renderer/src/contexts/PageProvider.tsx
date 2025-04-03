import { useState } from "react";
import { PageContext } from "./PageContext";

export function PageProvider({ children }: { children: React.ReactNode }): JSX.Element {
	const [currentJob, setCurrentJob] = useState<string>("");

	return (
		<PageContext.Provider
			value={{
				currentJob,
				setCurrentJob
			}}
		>
			{children}
		</PageContext.Provider>
	);
}
