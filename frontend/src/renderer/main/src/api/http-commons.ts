import axios from "axios";

const localAxios = axios.create({
	baseURL: `${import.meta.env.VITE_API_BASE_URL}/api/v1/`,
	headers: {
		"Content-Type": "application/json;charset=utf-8"
	}
});

export default localAxios;
