/** @type {import('tailwindcss').Config} */
export default {
	content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
	theme: {
		extend: {
			colors: {
				red: "#FD2F2F",
				green: "#22B16E",
				blue: "#3D5EDC",
				background: "#F1F2F5"
			}
		}
	},
	plugins: []
};
