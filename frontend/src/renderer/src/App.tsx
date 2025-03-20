import { Canvas } from "@react-three/fiber";
import { AvatarController } from "./components/banker/AvatarController";
import {
	EffectComposer,
	Bloom,
	DepthOfField,
	BrightnessContrast
} from "@react-three/postprocessing";

/* eslint-disable react/no-unknown-property */

function App(): JSX.Element {
	return (
		<>
			<div className="flex">
				{/* Canvas 영역 (33.33% 화면 차지) */}
				<div style={{ width: "33.33vw" }}>
					<Canvas
						shadows
						gl={{ antialias: true }}
						camera={{ position: [0, 0.6, 5.5], rotation: [-0.15, 0, 0], fov: 70 }}
						style={{ width: "100%", height: "100vh" }}
					>
						{/* 전체적인 조명 */}
						<ambientLight intensity={0.6} color={"#ffffff"} />

						{/* 방향성 조명 (그림자 생성) */}
						<directionalLight
							castShadow
							intensity={1}
							position={[5, 10, 5]}
							shadow-mapSize-width={2048}
							shadow-mapSize-height={2048}
						/>

						{/* 추가적인 포인트 라이트 */}
						<pointLight position={[0, 5, 8]} intensity={120} castShadow />
						<pointLight position={[5, 5, 8]} intensity={80} castShadow />
						<pointLight position={[-5, 5, 8]} intensity={80} castShadow />
						<pointLight position={[0, -5, 8]} intensity={60} castShadow />

						{/* 3D 은행원원 컨트롤러 */}
						<AvatarController />

						{/* 포스트 프로세싱 효과 */}
						<EffectComposer>
							<Bloom
								intensity={0.2}
								luminanceThreshold={0.9}
								luminanceSmoothing={0.3}
								height={300}
							/>
							<DepthOfField focusDistance={0} focalLength={0.5} bokehScale={1} />
							<BrightnessContrast brightness={0} contrast={-0.1} />
						</EffectComposer>
					</Canvas>
				</div>
				<div style={{ width: "66.67vw" }}></div>
			</div>
		</>
	);
}

export default App;
