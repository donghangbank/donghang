import { app, BrowserWindow, ipcMain, screen } from "electron";
import { join } from "path";
import { electronApp, optimizer, is } from "@electron-toolkit/utils";
import icon from "../../resources/icon.png?asset";

let mainWindow: BrowserWindow | null = null;
let subWindow: BrowserWindow | null = null;

const USE_SINGLE_MONITOR = false;

function createWindows(): void {
	const displays = screen.getAllDisplays();
	const primaryDisplay = displays[0];
	const secondaryDisplay = displays[1];

	// Common window options
	const commonOptions = {
		kiosk: true,
		frame: false,
		autoHideMenuBar: true,
		icon: process.platform === "linux" ? icon : undefined,
		webPreferences: {
			preload: join(__dirname, "../preload/index.js"),
			contextIsolation: true,
			nodeIntegration: false,
			backgroundThrottling: false
		}
	};

	if (USE_SINGLE_MONITOR || displays.length === 1) {
		// Single-monitor mode: Split the primary display into two windows
		const halfWidth = primaryDisplay.bounds.width / 2;

		// Main Window (left half)
		mainWindow = new BrowserWindow({
			x: primaryDisplay.bounds.x,
			y: primaryDisplay.bounds.y,
			width: halfWidth,
			height: primaryDisplay.bounds.height,
			...commonOptions
		});

		// Sub Window (right half)
		subWindow = new BrowserWindow({
			x: primaryDisplay.bounds.x + halfWidth,
			y: primaryDisplay.bounds.y,
			width: halfWidth,
			height: primaryDisplay.bounds.height,
			...commonOptions
		});
	} else {
		// Dual-monitor mode: Use separate displays
		// Main Window (primary monitor)
		mainWindow = new BrowserWindow({
			x: primaryDisplay.bounds.x,
			y: primaryDisplay.bounds.y,
			width: primaryDisplay.bounds.width,
			height: primaryDisplay.bounds.height,
			...commonOptions
		});

		// Sub Window (secondary monitor)
		subWindow = new BrowserWindow({
			x: secondaryDisplay.bounds.x,
			y: secondaryDisplay.bounds.y,
			width: secondaryDisplay.bounds.width,
			height: secondaryDisplay.bounds.height,
			...commonOptions
		});
	}

	// Load content for Main Window
	if (is.dev && process.env["ELECTRON_RENDERER_URL"]) {
		mainWindow.loadURL(`${process.env["ELECTRON_RENDERER_URL"]}/main/index.html`);
	} else {
		mainWindow.loadFile(join(__dirname, "../renderer/main/index.html"));
	}

	mainWindow.on("ready-to-show", () => {
		// console.log("[MainWindow] bounds:", mainWindow?.getBounds());
		mainWindow?.show();
	});

	// Load content for Sub Window
	if (is.dev && process.env["ELECTRON_RENDERER_URL"]) {
		subWindow.loadURL(`${process.env["ELECTRON_RENDERER_URL"]}/sub/index.html`);
	} else {
		subWindow.loadFile(join(__dirname, "../renderer/sub/index.html"));
	}

	subWindow.on("ready-to-show", () => {
		// console.log("[SubWindow] bounds:", subWindow?.getBounds());
		subWindow?.show();
	});
}

// Rest of your code remains unchanged
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
		if (subWindow && !subWindow.isDestroyed()) {
			subWindow.webContents.send("sub-inputlink-updated", hasInputLink);
		}
	});

	ipcMain.on("sub-number-updated", (event, newValue: string) => {
		if (mainWindow && !mainWindow.isDestroyed()) {
			mainWindow.webContents.send("update-main-input", newValue);
		}
	});

	ipcMain.on("main-number-updated", (event, value: string) => {
		subWindow?.webContents.send("update-sub-input", value);
	});

	ipcMain.on("sub-button-pressed", (event, action: "confirm" | "cancel") => {
		if (mainWindow && !mainWindow.isDestroyed()) {
			if (action === "confirm") {
				mainWindow.webContents.send("call-confirm");
			} else {
				mainWindow.webContents.send("call-cancel");
			}
		}
	});

	ipcMain.on("update-sub-type", (event, type: string) => {
		if (subWindow && !subWindow.isDestroyed()) {
			subWindow.webContents.send("set-sub-type", type);
		}
	});

	ipcMain.on("update-sub-disabled", (event, disabled: boolean) => {
		if (subWindow && !subWindow.isDestroyed()) {
			subWindow.webContents.send("sub-disabled-updated", disabled);
		}
	});

	ipcMain.on(
		"set-sub-mode",
		// eslint-disable-next-line @typescript-eslint/no-explicit-any
		(event, mode: "numpad" | "scam-warning" | "card-warning", data?: any) => {
			if (subWindow && !subWindow.isDestroyed()) {
				subWindow.webContents.send("set-sub-mode", { mode, data });
			}
		}
	);
});

app.on("window-all-closed", () => {
	if (process.platform !== "darwin") {
		app.quit();
	}
});
