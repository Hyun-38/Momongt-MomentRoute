import axios from "axios";

const instance = axios.create({
  baseURL: "http://172.30.1.31:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// ⭐ 요청 시 토큰 자동 포함
instance.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default instance;
