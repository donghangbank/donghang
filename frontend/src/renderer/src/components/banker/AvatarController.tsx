import React, { useEffect, useRef } from "react";
import { useFrame } from "@react-three/fiber";
import { useFBX } from "@react-three/drei";
import * as THREE from "three";
import { Avatar } from "./Avatar";
import idleAnimation from "@renderer/assets/models/avatar_idle.fbx?url";

export const AvatarController: React.FC = () => {
	const avatarRef = useRef<THREE.Group>(null);
	const mixerRef = useRef<THREE.AnimationMixer | null>(null);
	const idleAnim = useFBX(idleAnimation);

	useEffect(() => {
		if (avatarRef.current) {
			mixerRef.current = new THREE.AnimationMixer(avatarRef.current);
			const action = mixerRef.current.clipAction(idleAnim.animations[0]);
			action.play();
		}
	}, [idleAnim]);

	useFrame((_, delta) => {
		if (mixerRef.current) {
			mixerRef.current.update(delta);
		}
	});

	return <Avatar ref={avatarRef} position={[0, -2.5, 3]} scale={2} />;
};
