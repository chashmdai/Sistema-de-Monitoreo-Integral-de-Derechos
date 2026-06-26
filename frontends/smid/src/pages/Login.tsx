import { useState, useContext, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { AuthContext } from "../context/AuthContext";
import { api } from "../api/axiosConfig";
import { User, Lock } from "lucide-react";
import {
  TextInput,
  PasswordInput,
  Button,
  Title,
  Text,
  Box,
} from "@mantine/core";
import { notifications } from "@mantine/notifications";

export const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const { login, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    // 1. Limpieza preventiva: Si había basura de un error anterior, la matamos antes de intentar loguear
    logout();
    localStorage.removeItem("smid_token");

    try {
      const response = await api.post("/auth/login", {
        email: email.trim(),
        password,
      });

      // Contrato de smid-auth (6.1): { accessToken, refreshToken, expiraEn, usuario }.
      const { accessToken, usuario } = response.data;

      // Validación estricta: evitamos guardar un token undefined en localStorage.
      if (!accessToken || accessToken === "undefined") {
        throw new Error(
          "El servidor no proporcionó un token de seguridad válido.",
        );
      }

      const fullName = `${usuario?.nombres ?? ""} ${usuario?.apellidos ?? ""}`.trim();

      // Mapea el perfil público (usuario) de smid-auth al contexto del front.
      login(accessToken, {
        altKey: usuario?.altKey,
        fullName,
        roles: usuario?.roles ?? [],
        idSede: usuario?.sede?.altKey ?? null,
        sedeNombre: usuario?.sede?.nombre ?? null,
        idUnidad: usuario?.unidad?.altKey ?? null,
        unidadNombre: usuario?.unidad?.nombre ?? null,
        alcance: usuario?.alcance ?? null,
      });

      notifications.show({
        title: "Acceso autorizado",
        message: `Bienvenido al sistema, ${fullName}`,
        color: "teal",
        autoClose: 3000,
      });

      // Hub canónico tras login. AppRouter no define "/sgs/dashboard"
      // (esa ruta cae en el catch-all "*"); "/home" es el destino real
      // al que redirigen tanto el índice como PublicRoute.
      navigate("/home");
    } catch (err: unknown) {
      let errorMessage =
        "Error de conexión con el servidor. Intente más tarde.";

      if (axios.isAxiosError(err)) {
        // El sobre de error del ecosistema usa 'mensaje' (es), con fallback a 'message'.
        errorMessage =
          err.response?.data?.mensaje || err.response?.data?.message || err.message;
      } else if (err instanceof Error) {
        errorMessage = err.message;
      }

      notifications.show({
        title: "Error de Autenticación",
        message: errorMessage,
        color: "red",
        autoClose: 5000,
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative isolate min-h-screen flex flex-col items-center justify-center p-4 sm:p-8 font-sans">
      {/* Capa de luz ambiental institucional */}
      <div aria-hidden className="aurora aurora--login" />

      <div className="glass-card w-full max-w-5xl rounded-2xl overflow-hidden flex flex-col lg:flex-row min-h-[560px]">
        {/* Lado izquierdo - Logo */}
        <div className="w-full lg:w-1/2 flex flex-col items-center justify-center p-12 relative">
          <img
            src="/img/defensoria-vertical.png"
            alt="Defensoría de la Niñez"
            className="w-[65%] max-w-[280px] object-contain drop-shadow-sm transition-transform duration-500 hover:scale-[1.02]"
          />
          <div className="absolute bottom-8 text-center w-full">
            <p className="text-[11px] font-bold text-simdTextMuted uppercase tracking-[0.2em]">
              Panel Regional O'Higgins y Maule
            </p>
          </div>
        </div>

        <div className="hidden lg:block w-px bg-gradient-to-b from-transparent via-white/50 to-transparent my-12"></div>

        {/* Lado derecho - Formulario */}
        <div className="w-full lg:w-1/2 flex flex-col justify-center p-8 sm:p-12">
          <Box className="w-full max-w-[340px] mx-auto">
            <Box mb="xl">
              <Title
                order={2}
                className="text-gray-900 font-extrabold tracking-tight mb-1 text-3xl"
              >
                Iniciar Sesión
              </Title>
              <Text c="dimmed" size="sm" className="font-medium">
                Ingrese sus credenciales para continuar
              </Text>
            </Box>

            <form onSubmit={handleSubmit} className="space-y-5">
              <TextInput
                label="Correo institucional"
                placeholder="usuario@defensorianinez.cl"
                required
                type="email"
                size="md"
                variant="filled"
                leftSection={<User size={18} className="text-simdTextMuted" />}
                value={email}
                onChange={(e) => setEmail(e.currentTarget.value)}
                styles={{
                  input: {
                    fontSize: "14px",
                    backgroundColor: "var(--glass-input)",
                    border: "1px solid rgba(255, 255, 255, 0.6)",
                    backdropFilter: "blur(8px)",
                    WebkitBackdropFilter: "blur(8px)",
                  },
                  label: {
                    fontSize: "13px",
                    marginBottom: "6px",
                    fontWeight: 600,
                    color: "var(--simd-text-main)",
                  },
                }}
              />

              <PasswordInput
                label="Contraseña"
                placeholder="••••••••"
                required
                size="md"
                variant="filled"
                leftSection={<Lock size={18} className="text-simdTextMuted" />}
                value={password}
                onChange={(e) => setPassword(e.currentTarget.value)}
                styles={{
                  input: {
                    fontSize: "14px",
                    backgroundColor: "var(--glass-input)",
                    border: "1px solid rgba(255, 255, 255, 0.6)",
                    backdropFilter: "blur(8px)",
                    WebkitBackdropFilter: "blur(8px)",
                  },
                  label: {
                    fontSize: "13px",
                    marginBottom: "6px",
                    fontWeight: 600,
                    color: "var(--simd-text-main)",
                  },
                }}
              />

              <Button
                type="submit"
                fullWidth
                size="md"
                mt="xl"
                loading={isLoading}
                variant="gradient"
                gradient={{ from: "simdPrimary.6", to: "simdPrimary.8", deg: 135 }}
                className="transition-all active:scale-[0.98] shadow-sm hover:shadow-md font-bold tracking-wide"
                radius="md"
              >
                Ingresar al Sistema
              </Button>
            </form>
          </Box>
        </div>
      </div>

      <div className="mt-8 text-center text-simdTextMuted">
        <p className="text-xs font-medium">
          Sistema Integral de Monitoreo de Derechos &copy; 2026
        </p>
      </div>
    </div>
  );
};
