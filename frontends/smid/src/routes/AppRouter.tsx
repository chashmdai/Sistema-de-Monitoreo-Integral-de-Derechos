import { Routes, Route, Navigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { MainLayout } from "../layouts/MainLayout";
import { Login } from "../pages/Login";
import { Home } from "../pages/Home";
// Cambia esto en la línea 7:
import SgsDashboard from "../pages/sgs/SgsDashboard"; // Sin llaves si es export default
import EsnnaDashboard from "../pages/esnna/EsnnaDashboard"; // Corregido: Importación por defecto (sin llaves)

// SMID Module
import { SmidHome } from "../pages/smid/SmidHome";
import { RequerimientosListPage } from "../pages/smid/RequerimientosListPage";
import { RequerimientoFormPage } from "../pages/smid/RequerimientoFormPage";
import { RequerimientoDetailPage } from "../pages/smid/RequerimientoDetailPage";
import { EvaluacionFormPage } from "../pages/smid/EvaluacionFormPage";
import { OficioFormPage } from "../pages/smid/OficioFormPage";
import { LegislativoListPage } from "../pages/smid/LegislativoListPage";
import { LeyFormPage } from "../pages/smid/LeyFormPage";
import { LeyDetailPage } from "../pages/smid/LeyDetailPage";
import { TramiteFormPage } from "../pages/smid/TramiteFormPage";
import { ReportesDashboardPage } from "../pages/smid/ReportesDashboardPage";

const PrivateRoute = ({ children }: { children: JSX.Element }) => {
  const { token } = useContext(AuthContext);
  return token ? children : <Navigate to="/login" replace />;
};

const PublicRoute = ({ children }: { children: JSX.Element }) => {
  const { token } = useContext(AuthContext);
  return !token ? children : <Navigate to="/home" replace />;
};

export const AppRouter = () => {
  return (
    <Routes>
      <Route
        path="/login"
        element={
          <PublicRoute>
            <Login />
          </PublicRoute>
        }
      />

      <Route
        path="/"
        element={
          <PrivateRoute>
            <MainLayout />
          </PrivateRoute>
        }
      >
        <Route index element={<Navigate to="/home" replace />} />
        <Route path="home" element={<Home />} />
        <Route path="sgs" element={<SgsDashboard />} />
        <Route path="esnna" element={<EsnnaDashboard />} />

        {/* ==========================================
            MÓDULO SMID — Rutas protegidas
            ========================================== */}
        <Route path="smid" element={<SmidHome />} />
        <Route
          path="smid/requerimientos"
          element={<RequerimientosListPage />}
        />
        <Route
          path="smid/requerimientos/nuevo"
          element={<RequerimientoFormPage />}
        />
        <Route
          path="smid/requerimientos/:codigo"
          element={<RequerimientoDetailPage />}
        />
        <Route
          path="smid/evaluaciones/nueva/:reqId"
          element={<EvaluacionFormPage />}
        />
        <Route path="smid/oficios/nuevo/:evalId" element={<OficioFormPage />} />
        <Route path="smid/legislativo" element={<LegislativoListPage />} />
        <Route path="smid/legislativo/nueva" element={<LeyFormPage />} />
        <Route path="smid/legislativo/:id" element={<LeyDetailPage />} />
        <Route
          path="smid/legislativo/tramite/nuevo/:leyId"
          element={<TramiteFormPage />}
        />
        <Route path="smid/reportes" element={<ReportesDashboardPage />} />
      </Route>

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
};
