// src/theme.ts
// ============================================================================
// SISTEMA DE DISEÑO INSTITUCIONAL SMID — Capa Mantine (+ glassmorphism)
// ----------------------------------------------------------------------------
// 1. Paleta ESPEJO de tailwind.config.js. El magenta #9E1B54 vive en el índice
//    6 (default de Mantine) y su hover #7A1440 en el 7.
// 2. Profundidad: Paper/Modal/Drawer se vuelven superficies de cristal por
//    defecto vía la API `styles` (estilos inline → máxima especificidad, no
//    pelean con utilidades Tailwind). Los overlays de Modal/Drawer se
//    desenfocan para reforzar el efecto multicapa.
// ============================================================================
import {
  createTheme,
  Paper,
  Modal,
  Drawer,
  type MantineColorsTuple,
} from '@mantine/core';

// --- Marca: #9E1B54 (6) / hover #7A1440 (7) -----------------------------------
const simdPrimary: MantineColorsTuple = [
  '#f9f1f5',
  '#eed6e0',
  '#daa8be',
  '#c77b9c',
  '#b5527d',
  '#a83265',
  '#9e1b54', // 6 — base institucional (#9E1B54)
  '#7a1440', // 7 — hover institucional (#7A1440)
  '#681237',
  '#4f0e2a',
];

// --- Acento (naranja del isotipo): #F99E39 ------------------------------------
const simdAccent: MantineColorsTuple = [
  '#fff9f3',
  '#feeedb',
  '#fddab4',
  '#fcc78c',
  '#fab569',
  '#faa84d',
  '#f99e39', // 6
  '#cc822f',
  '#a46826',
  '#7c4f1c',
];

// --- Éxito: #93C43A -----------------------------------------------------------
const simdSuccess: MantineColorsTuple = [
  '#f9fbf3',
  '#ecf4dc',
  '#d6e9b4',
  '#c0dd8d',
  '#add269',
  '#9eca4e',
  '#93c43a', // 6
  '#79a130',
  '#618126',
  '#4a621d',
];

// --- Advertencia: #F4DD26 -----------------------------------------------------
const simdWarning: MantineColorsTuple = [
  '#fefdf2',
  '#fdf9d8',
  '#fbf2ad',
  '#f9eb81',
  '#f7e55a',
  '#f5e03c',
  '#f4dd26', // 6
  '#c8b51f',
  '#a19219',
  '#7a6e13',
];

// --- Peligro (rosa/sandía): #E55572 -------------------------------------------
const simdDanger: MantineColorsTuple = [
  '#fdf5f7',
  '#fae0e6',
  '#f5bec9',
  '#f09cad',
  '#eb7e94',
  '#e86680',
  '#e55572', // 6
  '#bc465d',
  '#97384b',
  '#722a39',
];

// --- Info / Gris institucional: #5D6062 ---------------------------------------
const simdInfo: MantineColorsTuple = [
  '#f5f5f6',
  '#e2e2e3',
  '#c1c3c3',
  '#a1a3a4',
  '#848688',
  '#6d7072',
  '#5d6062', // 6
  '#4c4f50',
  '#3d3f41',
  '#2e3031',
];

// --- Recetas de cristal reutilizadas en los overrides de componentes ----------
const glassSurface = {
  backgroundColor: 'rgba(255, 255, 255, 0.66)',
  backdropFilter: 'blur(16px) saturate(140%)',
  WebkitBackdropFilter: 'blur(16px) saturate(140%)',
  border: '1px solid rgba(255, 255, 255, 0.55)',
  boxShadow:
    '0 8px 30px rgba(16, 24, 40, 0.08), inset 0 1px 0 rgba(255, 255, 255, 0.5)',
};

const glassSurfaceStrong = {
  backgroundColor: 'rgba(255, 255, 255, 0.86)',
  backdropFilter: 'blur(22px) saturate(150%)',
  WebkitBackdropFilter: 'blur(22px) saturate(150%)',
  border: '1px solid rgba(255, 255, 255, 0.6)',
};

export const theme = createTheme({
  colors: {
    simdPrimary,
    simdAccent,
    simdSuccess,
    simdWarning,
    simdDanger,
    simdInfo,
  },
  primaryColor: 'simdPrimary',
  primaryShade: 6, // El color base #9E1B54 vive en el índice 6

  fontFamily: 'Inter, system-ui, -apple-system, sans-serif',
  fontFamilyMonospace: 'ui-monospace, SFMono-Regular, Menlo, monospace',
  headings: {
    fontFamily: 'Inter, system-ui, sans-serif',
    fontWeight: '700',
  },

  defaultRadius: 'md',
  radius: {
    xs: '0.375rem',
    sm: '0.5rem',
    md: '0.75rem',
    lg: '1rem',
    xl: '1.25rem',
  },

  shadows: {
    xs: '0 1px 2px rgba(16, 24, 40, 0.04)',
    sm: '0 1px 3px rgba(16, 24, 40, 0.06), 0 1px 2px rgba(16, 24, 40, 0.04)',
    md: '0 6px 16px -8px rgba(16, 24, 40, 0.10)',
    lg: '0 20px 40px -18px rgba(16, 24, 40, 0.14)',
  },

  // --- Superficies de cristal por defecto -------------------------------------
  components: {
    // Todo Paper (paneles, tarjetas internas, filtros) pasa a cristal.
    Paper: Paper.extend({
      styles: {
        root: glassSurface,
      },
    }),

    // El contenido del Modal es cristal fuerte y su overlay se desenfoca.
    Modal: Modal.extend({
      defaultProps: {
        radius: 'lg',
        overlayProps: { backgroundOpacity: 0.4, blur: 6 },
      },
      styles: {
        content: glassSurfaceStrong,
        header: { backgroundColor: 'transparent' },
      },
    }),

    // Idéntico tratamiento para el Drawer lateral.
    Drawer: Drawer.extend({
      defaultProps: {
        overlayProps: { backgroundOpacity: 0.4, blur: 6 },
      },
      styles: {
        content: {
          ...glassSurfaceStrong,
          backgroundColor: 'rgba(255, 255, 255, 0.88)',
        },
        header: { backgroundColor: 'transparent' },
      },
    }),
  },
});
