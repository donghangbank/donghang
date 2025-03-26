/** @type {import('tailwindcss').Config} */
export default {
	content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
	theme: {
		extend: {
			colors: {
				red: "#DE0202",
				green: "#177D4D",
				blue: "#3D5EDC",
				background: "#F1F2F5",
				cloudyBlue: "#E5EBFF",
				roseBlush: "#FFE5E5"
			},
			boxShadow: {
				custom: "0px 2px 8px 0px rgba(0, 0, 0, 0.08)"
			},
			backgroundImage: {
				"purple-linear": "linear-gradient(90deg, #667EEA 0%, #764BA2 100%)"
			}
		}
	},
	plugins: []
};
