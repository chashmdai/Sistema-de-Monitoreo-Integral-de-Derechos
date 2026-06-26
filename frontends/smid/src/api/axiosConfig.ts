import axios from "axios";

export const api = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("smid_token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const url = error.config?.url ?? "";

    // 401 en una request autenticada (no en el propio login) = sesión expirada/inválida.
    // Se limpia la sesión y se redirige; el login maneja sus propios 401 en su catch.
    if (status === 401 && !url.includes("/auth/login")) {
      localStorage.removeItem("smid_token");
      localStorage.removeItem("smid_user");
      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  },
);
