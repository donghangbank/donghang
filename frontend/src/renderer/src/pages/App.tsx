import { Link } from "react-router-dom";

export const App = (): JSX.Element => {
	return (
		<div className="h-full flex items-center justify-center">
			<div className="flex items-start justify-center  gap-5">
				<Link to={"/senior"}>
					<button type="button" className="p-5 bg-blue rounded-3xl text-white">
						노인
					</button>
				</Link>
				<Link to={"/general"}>
					<button type="button" className="p-5 bg-red rounded-3xl text-white">
						일반
					</button>
				</Link>
			</div>
		</div>
	);
};

export default App;
