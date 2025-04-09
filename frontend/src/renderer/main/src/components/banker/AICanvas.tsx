/* eslint-disable react/no-unknown-property */
import { Canvas } from "@react-three/fiber";
import { AvatarController } from "./AvatarController";
import {
	// Bloom,
	// BrightnessContrast,
	DepthOfField,
	EffectComposer
} from "@react-three/postprocessing";
import { UserContext } from "@renderer/contexts/UserContext";
import { useContext } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import Dialogue from "./Dialogue";

export default function AICanvas(): JSX.Element {
	const { isElderly, isUsingPhone, userMsg, isTalking } = useContext(UserContext);
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
			<div className="fixed left-4 bottom-32">
				<div className="flex flex-col gap-4">
					<span className="text-2xl text-black">{isElderly === 2 ? "노인" : "노인이 아님"}</span>
					<span className="text-2xl text-black">
						{isUsingPhone ? "휴대폰 사용 중" : "휴대폰 사용 중 아님"}
					</span>
					<span>{isTalking ? "사용자 말하는 중..." : "말하지 않는 중"}</span>
					<span>{"사용자 음성: " + userMsg}</span>
				</div>
			</div>
		</div>
	);
}
