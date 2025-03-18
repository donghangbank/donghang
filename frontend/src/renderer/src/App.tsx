import Versions from "./components/Versions";
import electronLogo from "./assets/electron.svg";
import { Canvas } from "@react-three/fiber";
import { Avatar } from "./components/banker/Avatar";

/* eslint-disable react/no-unknown-property */

function App(): JSX.Element {
	const ipcHandle = (): void => window.electron.ipcRenderer.send("ping");

	return (
		<>
			<img alt="logo" className="logo" src={electronLogo} />
			<div className="text-3xl text-red-500">Powered by electron-vite</div>
			<div className="text">
				Build an Electron app with <span className="react">React</span>
				&nbsp;and <span className="ts">TypeScript</span>
			</div>
			<p className="tip">
				Please try pressing <code>F12</code> to open the devTool
			</p>
			<div className="actions">
				<div className="action">
					<a href="https://electron-vite.org/" target="_blank" rel="noreferrer">
						Documentation
					</a>
				</div>
				<div className="action">
					<a target="_blank" rel="noreferrer" onClick={ipcHandle}>
						Send IPC
					</a>
				</div>
			</div>
			<Versions></Versions>
			<Canvas style={{ width: "100%", height: "300px" }}>
				<ambientLight />
				<pointLight position={[5, 5, 5]} />
				<Avatar position={[0, -3, 3]} scale={2} />
			</Canvas>
		</>
	);
}

export default App;
