interface StepConfig {
	path: string;
	name: string;
}

interface ServiceConfig {
	title: string;
	steps: StepConfig[];
}

const ServiceProgress = ({
	title,
	steps,
	pathname
}: ServiceConfig & { pathname: string }): JSX.Element => {
	const pathSegments = pathname.split("/").filter(Boolean);

	const getCurrentStepIndex = (): number => {
		let currentIndex = -1;
		steps.forEach((step, index) => {
			if (pathSegments.includes(step.path)) {
				currentIndex = index;
			}
		});
		return currentIndex;
	};

	const currentStepIndex = getCurrentStepIndex();

	return (
		<div className="flex items-center gap-5">
			{/* 서비스 제목 */}
			<div className="flex items-center gap-5">
				<div className="p-5 rounded-full text-2xl font-bold shadow-custom bg-blue text-white">
					{title}
				</div>
				<span className="mx-2 text-gray-400">▶</span>
			</div>

			{/* 단계 표시 */}
			{steps.map((step, index) => {
				const isCurrent = index === currentStepIndex;
				return (
					<div key={`${index}-${step}`} className="flex items-center gap-5">
						<div
							className={`p-5 rounded-full text-2xl font-bold shadow-custom ${
								isCurrent ? "bg-blue text-white" : "bg-cloudyBlue text-gray-600"
							}`}
						>
							{step.name}
						</div>
						{index < steps.length - 1 && <span className="mx-2 text-gray-400">▶</span>}
					</div>
				);
			})}
		</div>
	);
};

export default ServiceProgress;
