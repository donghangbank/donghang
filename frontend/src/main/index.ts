import { app, BrowserWindow, ipcMain, screen } from "electron";
import { join } from "path";
import { electronApp, optimizer, is } from "@electron-toolkit/utils";
import icon from "../../resources/icon.png?asset";

let mainWindow: BrowserWindow | null = null;
let subWindow: BrowserWindow | null = null;

function createWindows(): void {
	const displays = screen.getAllDisplays();
	const primaryDisplay = displays[0];
	const secondaryDisplay = displays[1];

	// 메인 윈도우 (1번 모니터)
	mainWindow = new BrowserWindow({
		x: primaryDisplay.bounds.x,
		y: primaryDisplay.bounds.y,
		width: primaryDisplay.bounds.width,
		height: primaryDisplay.bounds.height,
		kiosk: true,
		frame: false,
		autoHideMenuBar: true,
		icon: process.platform === "linux" ? icon : undefined,
		webPreferences: {
			preload: join(__dirname, "../preload/index.js"),
			contextIsolation: true,
			nodeIntegration: false
		}
	});

	if (is.dev && process.env["ELECTRON_RENDERER_URL"]) {
		// dev 모드 → "메인" 창은 /main.html
		mainWindow.loadURL(`${process.env["ELECTRON_RENDERER_URL"]}/main/index.html`);
	} else {
		// 프로덕션 빌드 → dist/renderer/main/index.html
		mainWindow.loadFile(join(__dirname, "../renderer/main/index.html"));
	}

	mainWindow.on("ready-to-show", () => {
		console.log("[MainWindow] bounds:", mainWindow?.getBounds());
		mainWindow?.show();
	});

	// 서브 윈도우 (2번 모니터)
	subWindow = new BrowserWindow({
		x: secondaryDisplay.bounds.x,
		y: secondaryDisplay.bounds.y,
		width: secondaryDisplay.bounds.width,
		height: secondaryDisplay.bounds.height,
		kiosk: true,
		frame: false,
		autoHideMenuBar: true,
		icon: process.platform === "linux" ? icon : undefined,
		webPreferences: {
			preload: join(__dirname, "../preload/index.js"),
			contextIsolation: true,
			nodeIntegration: false
		}
	});

	if (is.dev && process.env["ELECTRON_RENDERER_URL"]) {
		// dev 모드 → "서브" 창은 /sub.html
		subWindow.loadURL(`${process.env["ELECTRON_RENDERER_URL"]}/sub/index.html`);
	} else {
		// 프로덕션 빌드 → dist/renderer/sub/index.html
		subWindow.loadFile(join(__dirname, "../renderer/sub/index.html"));
	}

	subWindow.on("ready-to-show", () => {
		console.log("[SubWindow] bounds:", subWindow?.getBounds());
		subWindow?.show();
	});
}

app.whenReady().then(() => {
	electronApp.setAppUserModelId("com.electron");

	app.on("browser-window-created", (_, window) => {
		optimizer.watchWindowShortcuts(window);
	});

	createWindows();

	app.on("activate", () => {
		if (BrowserWindow.getAllWindows().length === 0) createWindows();
	});

	ipcMain.on("update-sub-state", (event, hasInputLink: boolean) => {
		// 서브 윈도우가 열려 있고 파괴되지 않았다면
		if (subWindow && !subWindow.isDestroyed()) {
			// 서브 윈도우에 "sub-inputlink-updated" 이벤트를 보냄
			subWindow.webContents.send("sub-inputlink-updated", hasInputLink);
		}
	});

	ipcMain.on("sub-number-updated", (event, newValue: string) => {
		if (mainWindow && !mainWindow.isDestroyed()) {
			mainWindow.webContents.send("update-main-input", newValue);
		}
	});

	ipcMain.on("main-number-updated", (event, value: string) => {
		// 메인 → 서브
		// 서브 윈도우에 'update-sub-input' 이벤트로 알려줌
		subWindow?.webContents.send("update-sub-input", value);
	});

	ipcMain.on("sub-button-pressed", (event, action: "confirm" | "cancel") => {
		// 여기서 "confirm" or "cancel"에 따라 메인 윈도우에 알림
		if (mainWindow && !mainWindow.isDestroyed()) {
			if (action === "confirm") {
				mainWindow.webContents.send("call-confirm");
			} else {
				mainWindow.webContents.send("call-cancel");
			}
		}
	});

	ipcMain.on("update-sub-type", (event, type: string) => {
		// 서브 윈도우에 전달
		if (subWindow && !subWindow.isDestroyed()) {
			subWindow.webContents.send("set-sub-type", type);
		}
	});

	ipcMain.on("update-sub-disabled", (event, disabled: boolean) => {
		if (subWindow && !subWindow.isDestroyed()) {
			subWindow.webContents.send("sub-disabled-updated", disabled);
		}
	});
});

app.on("window-all-closed", () => {
	if (process.platform !== "darwin") {
		app.quit();
	}
});
