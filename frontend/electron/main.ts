import { app, BrowserWindow } from "electron";
import * as path from "path";

const isDev = process.env.NODE_ENV === "development";

function createWindow() {
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      contextIsolation: true,
      nodeIntegration: false,
      preload: path.join(__dirname, "preload.js"),
    },
  });

  if (isDev) {
    // 개발 모드: Vite 개발 서버 주소 사용
    mainWindow.loadURL("http://localhost:5173");
  } else {
    // 배포 모드: 빌드된 정적 파일 로드
    // __dirname은 electron 디렉토리를 가리키므로, dist 폴더는 상위 폴더에 위치한다고 가정합니다.
    const indexPath = path.join(__dirname, "../dist/index.html");
    console.log("Loading index file from:", indexPath);
    mainWindow.loadFile(indexPath);
  }
}

app.whenReady().then(createWindow);

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

app.on("activate", () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});
