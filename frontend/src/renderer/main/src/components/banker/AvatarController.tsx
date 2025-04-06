import { useCallback, useContext, useEffect, useRef, useState } from "react";
import { useFrame } from "@react-three/fiber";
import { useFBX } from "@react-three/drei";
import * as THREE from "three";
import { Avatar } from "./Avatar";
import idleAnimation from "@renderer/assets/models/avatar_idle.fbx?url";
import walkAnimation from "@renderer/assets/models/avatar_walk.fbx?url";
import bowAnimation from "@renderer/assets/models/avatar_bow.fbx?url";
import focusBottomAnimation from "@renderer/assets/models/avatar_focus_bottom.fbx?url";
import { AIContext } from "@renderer/contexts/AIContext";

export type AnimationType = "idle" | "walk" | "bow" | "focusBottom";

export const AvatarController = (): JSX.Element => {
	const avatarRef = useRef<THREE.Group>(null);
	const mixerRef = useRef<THREE.AnimationMixer | null>(null);
	const { avatarState, setAvatarState } = useContext(AIContext);

	const [currentAction, setCurrentAction] = useState<AnimationType>("idle");
	const [isMoving, setIsMoving] = useState(false);

	const [isOut, setIsOut] = useState(false);
	const moveStartTime = useRef<number | null>(null);

	const idleAnim = useFBX(idleAnimation);
	const walkAnim = useFBX(walkAnimation);
	const bowAnim = useFBX(bowAnimation);
	const focusBottomAnim = useFBX(focusBottomAnimation);

	const actionsRef = useRef<Record<AnimationType, THREE.AnimationAction | null>>({
		idle: null,
		walk: null,
		bow: null,
		focusBottom: null
	});

	const fadeToAction = useCallback(
		(name: AnimationType, repeat: boolean = true, duration = 0.3): void => {
			if (!mixerRef.current) return;
			const nextAction = actionsRef.current[name];
			const current = actionsRef.current[currentAction];

			if (!nextAction || !current || current === nextAction) return;

			current.fadeOut(duration);
			nextAction.reset().fadeIn(duration).play();

			setCurrentAction(name);

			if (!repeat) {
				nextAction.clampWhenFinished = true;
				nextAction.setLoop(THREE.LoopOnce, 1);

				const onAnimationFinished = (e: THREE.Event & { action: THREE.AnimationAction }): void => {
					if (e.action === nextAction) {
						mixerRef.current?.removeEventListener("finished", onAnimationFinished);

						const idle = actionsRef.current["idle"];
						if (idle && idle !== nextAction) {
							nextAction.fadeOut(duration);
							idle.reset().fadeIn(duration).play();
							setCurrentAction("idle");
							setAvatarState("idle");
						}
					}
				};

				mixerRef.current.addEventListener("finished", onAnimationFinished);
			} else {
				nextAction.setLoop(THREE.LoopRepeat, Infinity);
				nextAction.clampWhenFinished = false;
			}
		},
		[currentAction, setAvatarState]
	);

	const rotateY = (angle: number): void => {
		if (avatarRef.current) {
			avatarRef.current.rotation.y += angle;
		}
	};

	useEffect(() => {
		if (avatarState === "idle") fadeToAction("idle");
		else fadeToAction(avatarState, false);

		console.log("Avatar state changed:", avatarState);
	}, [avatarState, fadeToAction]);

	useEffect(() => {
		if (avatarRef.current) {
			const mixer = new THREE.AnimationMixer(avatarRef.current);
			mixerRef.current = mixer;

			const idleAction = mixer.clipAction(idleAnim.animations[0]);
			const walkAction = mixer.clipAction(walkAnim.animations[0]);
			const bowAction = mixer.clipAction(bowAnim.animations[0]);
			const focusBottomAction = mixer.clipAction(focusBottomAnim.animations[0]);

			idleAction.play();

			actionsRef.current.idle = idleAction;
			actionsRef.current.walk = walkAction;
			actionsRef.current.bow = bowAction;
			actionsRef.current.focusBottom = focusBottomAction;
		}
	}, [idleAnim, walkAnim, bowAnim, focusBottomAnim]);

	useFrame((_, delta) => {
		if (mixerRef.current) {
			mixerRef.current.update(delta);
		}

		if (isMoving && avatarRef.current && moveStartTime.current) {
			const elapsed = performance.now() - moveStartTime.current;

			if (elapsed < 1500) {
				const direction = new THREE.Vector3(0, 0, 1);
				direction.applyQuaternion(avatarRef.current.quaternion);
				direction.normalize().multiplyScalar(0.02);
				avatarRef.current.position.add(direction);
			} else {
				setIsMoving(false);
				moveStartTime.current = null;
				fadeToAction("idle");

				if (isOut) {
					rotateY(-Math.PI / 2); // 되돌아온 뒤 오른쪽 90도 회전 (원래 방향)
				}

				setIsOut(!isOut); // 토글 상태 전환
			}
		}
	});

	return <Avatar ref={avatarRef} position={[0, -6, 3]} scale={4} />;
};
