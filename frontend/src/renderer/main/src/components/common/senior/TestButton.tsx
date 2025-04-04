import { useNavigate } from "react-router-dom";

// Define the props interface
interface TestButtonProps {
	prevRoute: string;
	nextRoute: string;
}

export default function TestButton({ prevRoute, nextRoute }: TestButtonProps): JSX.Element {
	const navigate = useNavigate();

	// Handle button clicks
	const handlePrevClick = (): void => {
		navigate(prevRoute);
	};

	const handleNextClick = (): void => {
		navigate(nextRoute);
	};

	return (
		<div className="absolute top-0 flex w-full h-fit justify-center">
			<div className="flex w-fit h-fit bg-white py-2 px-4 rounded-full shadow-md">
				<div className="flex justify-start items-end gap-4">
					<button
						className="w-[80px] h-[50px] rounded-full bg-rose-500 text-white font-semibold"
						onClick={handlePrevClick}
					>
						Prev
					</button>
					<button
						className="w-[80px] h-[50px] rounded-full bg-emerald-500 text-white font-semibold"
						onClick={handleNextClick}
					>
						Next
					</button>
				</div>
			</div>
		</div>
	);
}
