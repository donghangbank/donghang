/* eslint-disable react/no-unknown-property */
import { Canvas } from "@react-three/fiber";
import { AvatarController } from "./AvatarController";
import {
	// Bloom,
	// BrightnessContrast,
	DepthOfField,
	EffectComposer
} from "@react-three/postprocessing";
import { useContext } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import Dialogue from "./Dialogue";

export default function AICanvas(): JSX.Element {
	const { dialogue } = useContext(AIContext);

	return (
		<div className="relative w-full h-full">
			<div className="absolute w-full flex justify-center top-24 text-3xl z-10">
				<Dialogue text={dialogue} />
			</div>
			<Canvas
				shadows
				gl={{ antialias: true }}
				camera={{ position: [0, 0.4, 7.5], rotation: [-0.15, 0, 0], fov: 50 }}
				onCreated={(state) => state.gl.setClearColor("#F1F2F5")}
			>
				{/* 전체적인 조명 */}
				<ambientLight intensity={1.3} color={"#ffffff"} />

				{/* 방향성 조명 (그림자 생성) */}
				<directionalLight
					castShadow
					intensity={4}
					position={[2, 12, 20]}
					shadow-mapSize-width={2048}
					shadow-mapSize-height={2048}
				/>

				{/* 추가적인 포인트 라이트 */}
				{/* <pointLight position={[0, 5, 8]} intensity={120} castShadow /> */}
				{/* <pointLight position={[5, 5, 8]} intensity={100} castShadow /> */}
				{/* <pointLight position={[-5, 5, 8]} intensity={100} castShadow /> */}
				{/* <pointLight position={[0, -5, 8]} intensity={150} castShadow /> */}
				{/* <pointLight position={[1, 0.2, 3.4]} intensity={0.5} castShadow /> */}
				{/* 3D 은행원원 컨트롤러 */}
				<AvatarController />

				{/* 포스트 프로세싱 효과 */}
				<EffectComposer>
					{/* <Bloom intensity={0.2} luminanceThreshold={0.9} luminanceSmoothing={0.3} height={300} /> */}
					<DepthOfField focusDistance={0} focalLength={0.5} bokehScale={1} />
					{/* <BrightnessContrast brightness={0} contrast={-0.1} /> */}
				</EffectComposer>
			</Canvas>
		</div>
	);
}
