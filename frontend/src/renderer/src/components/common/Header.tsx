import Logo from "@renderer/assets/logo.png";

export const Header = (): JSX.Element => {
	return (
		<div className="h-[10%] p-5">
			<img className="h-full" src={Logo} alt="logo" />
		</div>
	);
};

export default Header;
