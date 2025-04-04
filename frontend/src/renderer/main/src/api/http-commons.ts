import axios from "axios";
const localAxios = axios.create({
	baseURL: "http://localhost:8080/api/v1/",
	headers: {
		"Content-Type": "application/json;charset=utf-8"
	}
});

export default localAxios;
