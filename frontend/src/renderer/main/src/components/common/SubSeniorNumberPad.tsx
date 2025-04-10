import { calculateNewValue } from "@renderer/utils/numberPadUtils";
import NumberButton from "./NumberButton";
import { useEffect, useState } from "react";

interface SubSeniorNumberPadProps {
	type?: string;
	localValue: string;
	setLocalValue: (value: string) => void;
	disabled?: boolean;
}

export const SubSeniorNumberPad = ({
	type,
	localValue,
	setLocalValue,
	disabled: initialDisabled = false
}: SubSeniorNumberPadProps): JSX.Element => {
	const [disabled, setDisabled] = useState(initialDisabled);

	useEffect(() => {
		window.subAPI.onDisabledUpdate((newDisabled) => {
			setDisabled(newDisabled);
		});
	}, [setDisabled]);

	// 숫자 클릭
	const handleNumberClick = (num: string): void => {
		if (disabled) return;
		const newValue = calculateNewValue(localValue, num, type);
		if (newValue !== localValue) {
			setLocalValue(newValue);
			window.subAPI.notifyNumberChange(newValue);
		}
	};

	// 지움(←)
	const handleNumberDeleteClick = (): void => {
		if (disabled) return;
		if (!localValue) return;
		const newValue = localValue.slice(0, -1);
		if (newValue !== localValue) {
			setLocalValue(newValue);
			window.subAPI.notifyNumberChange(newValue);
		}
	};

	// 정정(모두 지우기)
	const handleNumberClearClick = (): void => {
		if (disabled) return;
		if (localValue.length === 0) return;
		setLocalValue("");
		window.subAPI.notifyNumberChange("");
	};

	const handleConfirmClick = (): void => {
		// 서브 로직이 있다면 수행
		// 그 후 메인 쪽에 "confirm" 알림
		if (disabled) return;
		window.subAPI.notifyButtonAction("confirm");
	};

	const handleCancelClick = (): void => {
		// 서브 로직(필요하다면 수행)
		// 그 후 메인 쪽에 "cancel" 알림
		window.subAPI.notifyButtonAction("cancel");
	};

	// 메인 윈도우 → update-sub-input 이벤트 수신
	useEffect(() => {
		window.subAPI.onMainNumberUpdate((newVal) => {
			// 여기서도 무한 루프 방지 위해 newVal !== localValue 체크
			if (newVal !== localValue) {
				setLocalValue(newVal);
			}
		});
	}, [localValue, setLocalValue]);

	return (
		<>
			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				{["1", "2", "3"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor={disabled ? "bg-gray-300" : "bg-green"}
						isSquare
						textColor={disabled ? "gray-400" : "white"}
						onClick={!disabled ? (): void => handleNumberClick(num) : undefined}
					/>
				))}
			</div>
			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				{["4", "5", "6"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor={disabled ? "bg-gray-300" : "bg-green"}
						isSquare
						textColor={disabled ? "gray-400" : "white"}
						onClick={!disabled ? (): void => handleNumberClick(num) : undefined}
					/>
				))}
			</div>
			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				{["7", "8", "9"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor={disabled ? "bg-gray-300" : "bg-green"}
						isSquare
						textColor={disabled ? "gray-400" : "white"}
						onClick={!disabled ? (): void => handleNumberClick(num) : undefined}
					/>
				))}
			</div>

			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				<NumberButton
					text={"하나 지우기"}
					bgColor="bg-blue"
					isSquare
					onClick={handleNumberDeleteClick}
				/>
				<NumberButton
					text={"0"}
					bgColor={disabled ? "bg-gray-300" : "bg-green"}
					isSquare
					textColor={disabled ? "gray-400" : "white"}
					onClick={!disabled ? (): void => handleNumberClick("0") : undefined}
				/>
				<NumberButton
					text={"전부 지우기"}
					bgColor="bg-blue"
					isSquare
					onClick={handleNumberClearClick}
				/>
			</div>

			<div className="h-[20%] grid grid-cols-2 gap-2.5">
				<NumberButton
					text={"확인"}
					bgColor="bg-green"
					isSquare={false}
					onClick={handleConfirmClick}
				/>
				<NumberButton text={"취소"} bgColor="bg-red" isSquare={false} onClick={handleCancelClick} />
			</div>
		</>
	);
};

export default SubSeniorNumberPad;
