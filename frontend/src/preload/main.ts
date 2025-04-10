import { contextBridge, ipcRenderer } from "electron";

contextBridge.exposeInMainWorld("mainAPI", {
	// 서브 창에 "update-sub-state" 이벤트를 보낼 때 쓰는 함수
	updateSubState: (hasInputLink: boolean) => {
		ipcRenderer.send("update-sub-state", hasInputLink);
	},

	// 서브 윈도우 → 메인 윈도우
	onSubNumberUpdate: (callback: (value: string) => void) => {
		const handler = (_event: unknown, newVal: string): void => callback(newVal);
		ipcRenderer.on("update-main-input", handler);
		return (): void => {
			ipcRenderer.removeListener("update-main-input", handler);
		};
	},

	// 메인 윈도우가 값 바뀔 때 서브 윈도우에 알리는 IPC
	notifyMainNumberChange: (value: string) => {
		ipcRenderer.send("main-number-updated", value);
	},

	onCallConfirm: (callback: () => void) => {
		console.log("onCallConfirm called");
		const handler = (): void => callback();
		ipcRenderer.on("call-confirm", handler);
		return (): void => {
			ipcRenderer.removeListener("call-confirm", handler);
		};
	},
	onCallCancel: (callback: () => void) => {
		const handler = (): void => callback();
		ipcRenderer.on("call-cancel", handler);
		return (): void => {
			ipcRenderer.removeListener("call-cancel", handler);
		};
	},
	removeCallConfirm: (callback: () => void) => {
		ipcRenderer.removeListener("call-confirm", callback);
	},
	removeCallCancel: (callback: () => void) => {
		ipcRenderer.removeListener("call-cancel", callback);
	},

	updateSubType: (type: string) => {
		ipcRenderer.send("update-sub-type", type);
	},

	updateSubDisabled: (disabled: boolean) => {
		ipcRenderer.send("update-sub-disabled", disabled);
	},

	send: (channel, ...args) => ipcRenderer.send(channel, ...args)
});
