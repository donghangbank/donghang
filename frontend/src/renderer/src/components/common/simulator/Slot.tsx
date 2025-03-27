interface SlotProps {
	title: string;
}

export const Slot = ({ title }: SlotProps): JSX.Element => {
	return (
		<div className="relative flex items-center justify-center h-full bg-gray-200 border-2 border-gray-500 rounded-md shadow-inner overflow-hidden">
			<div className="absolute inset-0 bg-gradient-to-b from-gray-300 to-gray-100 opacity-70"></div>
			<div className="absolute inset-0 bg-[url('/path/to/metal-texture.png')] bg-cover opacity-20"></div>
			<span className="relative z-10 font-semibold text-gray-800">{title}</span>
		</div>
	);
};

export default Slot;
