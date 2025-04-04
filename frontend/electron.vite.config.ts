import { resolve } from "node:path";
import { defineConfig, externalizeDepsPlugin } from "electron-vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
	main: {
		plugins: [externalizeDepsPlugin()]
	},
	preload: {
		plugins: [externalizeDepsPlugin()]
	},
	renderer: {
		build: {
			rollupOptions: {
				input: {
					// "main" 창용 HTML
					main: resolve(__dirname, "src/renderer/main/index.html"),
					// "sub" 창용 HTML
					sub: resolve(__dirname, "src/renderer/sub/index.html")
				}
			}
		},
		resolve: {
			alias: {
				"@renderer": resolve("src/renderer/main/src"),
				"@renderer/sub": resolve("src/renderer/sub/src")
			}
		},
		plugins: [react()]
	}
});
