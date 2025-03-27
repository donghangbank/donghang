import { Link } from "react-router-dom";

export const NotFoundPage = (): JSX.Element => {
	return (
		<div className="flex flex-col h-screen items-center justify-center gap-10">
			<span className="text-8xl font-bold">존재하지 않는 페이지입니다.</span>
			<Link to={"/"}>
				<button className="text-7xl p-5 bg-black text-white rounded-full">홈으로</button>
			</Link>
		</div>
	);
};

export default NotFoundPage;
