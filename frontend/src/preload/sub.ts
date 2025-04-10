import { contextBridge, ipcRenderer } from "electron";

contextBridge.exposeInMainWorld("subAPI", {
	onInputLinkUpdated: (callback: (hasInput: boolean) => void) => {
		ipcRenderer.on("sub-inputlink-updated", (event, hasInput: boolean) => {
			callback(hasInput);
		});
	},

	notifyNumberChange: (value: string) => {
		ipcRenderer.send("sub-number-updated", value);
	},
	onMainNumberUpdate: (callback: (value: string) => void) => {
		// 메인 윈도우에서 보낸 "update-sub-input" 이벤트를 받음
		ipcRenderer.on("update-sub-input", (event, newVal: string) => {
			callback(newVal);
		});
	},

	// 버튼 액션을 메인 프로세스에 전달
	notifyButtonAction: (action: "confirm" | "cancel") => {
		console.log("notifyButtonAction", action);
		ipcRenderer.send("sub-button-pressed", action);
	},

	onSubTypeUpdate: (callback: (newType: string) => void) => {
		ipcRenderer.on("set-sub-type", (event, newType) => {
			callback(newType);
		});
	},

	onDisabledUpdate: (callback: (disabled: boolean) => void) => {
		ipcRenderer.on("sub-disabled-updated", (event, disabled: boolean) => {
			callback(disabled);
		});
	},

	onSubModeUpdate: (callback) => ipcRenderer.on("set-sub-mode", (event, data) => callback(data))
});
