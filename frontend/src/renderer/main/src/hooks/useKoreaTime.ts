import { useState, useEffect } from "react";

const useKoreaTime = (): string => {
	const [koreaTime, setKoreaTime] = useState("");

	useEffect(() => {
		const updateKoreaTime = (): void => {
			const now = new Date();
			const utc = now.getTime() + now.getTimezoneOffset() * 60000;
			const koreaTimeOffset = 9 * 60 * 60000;
			const koreaNow = new Date(utc + koreaTimeOffset);
			const formattedKoreaTime = koreaNow.toISOString().slice(0, 19);
			setKoreaTime(formattedKoreaTime);
		};

		updateKoreaTime();
		const intervalId = setInterval(updateKoreaTime, 1000);

		return (): void => clearInterval(intervalId);
	}, []);

	return koreaTime;
};

export default useKoreaTime;
