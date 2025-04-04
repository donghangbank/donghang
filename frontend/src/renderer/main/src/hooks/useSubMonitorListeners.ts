import { useEffect } from "react";

export const useSubMonitorListeners = (
	onNumberUpdate: (value: string) => void,
	onConfirm: () => void,
	onCancel: () => void
): void => {
	useEffect(() => {
		const numberCleanup = window.mainAPI.onSubNumberUpdate(onNumberUpdate);
		const confirmCleanup = window.mainAPI.onCallConfirm(onConfirm);
		const cancelCleanup = window.mainAPI.onCallCancel(onCancel);

		return (): void => {
			numberCleanup();
			confirmCleanup();
			cancelCleanup();
		};
	}, [onNumberUpdate, onConfirm, onCancel]);
};
