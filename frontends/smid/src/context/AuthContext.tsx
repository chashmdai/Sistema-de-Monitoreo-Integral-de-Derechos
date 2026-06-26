import { createContext, useState, type ReactNode } from "react";

export interface UserData {
  // Identificador publico opaco del usuario (claim 'sub' del JWT de smid-auth).
  altKey: string;
  fullName: string;
  roles: string[];
  // Territorio: alt_key (UUID) de sede/unidad, segun el contrato de smid-auth.
  idSede: string | null;
  sedeNombre: string | null;
  idUnidad: string | null;
  unidadNombre: string | null;
  alcance: string | null;
}

interface AuthContextProps {
  token: string | null;
  user: UserData | null;
  login: (token: string, user: UserData) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextProps>(
  {} as AuthContextProps,
);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(() =>
    localStorage.getItem("smid_token"),
  );

  const [user, setUser] = useState<UserData | null>(() => {
    const savedUser = localStorage.getItem("smid_user");
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const login = (newToken: string, newUser: UserData) => {
    setToken(newToken);
    setUser(newUser);
    localStorage.setItem("smid_token", newToken);
    localStorage.setItem("smid_user", JSON.stringify(newUser));
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem("smid_token");
    localStorage.removeItem("smid_user");
  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
