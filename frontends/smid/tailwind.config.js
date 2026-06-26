/** @type {import('tailwindcss').Config} */
import defaultTheme from 'tailwindcss/defaultTheme';

// ============================================================================
// SISTEMA DE DISEÑO INSTITUCIONAL SMID — Fuente única de verdad (Tailwind)
// ----------------------------------------------------------------------------
// Los tokens de color están ESPEJADOS en src/theme.ts (Mantine) y en las
// variables CSS de src/index.css. No introducir hex sueltos en JSX: usar
// siempre los tokens simd* (admiten opacidad, p.ej. `bg-simdPrimary/5`).
// Las primitivas de profundidad (glass-panel/glass-card/aurora) viven en
// src/index.css @layer components; aquí se añaden las sombras de cristal.
// ============================================================================
export default {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      // 🖋️ Tipografía institucional
      fontFamily: {
        sans: ['Inter', ...defaultTheme.fontFamily.sans],
      },

      // 🎨 Paleta institucional SMID
      colors: {
        // Marca
        simdPrimary: '#9E1B54',       // Magenta institucional
        simdPrimaryHover: '#7A1440',  // Magenta oscuro (hover)
        simdAccent: '#F99E39',        // Naranja del isotipo

        // Funcionales (semántica de estado)
        simdSuccess: '#93C43A',       // Verde
        simdWarning: '#F4DD26',       // Amarillo
        simdDanger: '#E55572',        // Rosa/Sandía
        simdInfo: '#5D6062',          // Gris institucional

        // Neutros y superficies
        simdBg: '#F4F5F7',            // Fondo general (canónico)
        simdSurface: '#FFFFFF',       // Fondo de tarjetas/paneles (sólido)
        simdTextMain: '#4A4D4E',      // Texto principal
        simdTextMuted: '#828587',     // Texto secundario
        simdBorder: '#E5E7EB',        // Bordes de tablas/tarjetas
      },

      // 🌑 Elevaciones unificadas: sólidas (card) y de cristal (glass/glow)
      boxShadow: {
        card: '0 1px 3px rgba(16, 24, 40, 0.06), 0 1px 2px rgba(16, 24, 40, 0.04)',
        'card-hover': '0 20px 40px -18px rgba(16, 24, 40, 0.14)',
        glass:
          '0 8px 32px rgba(16, 24, 40, 0.10), inset 0 1px 0 rgba(255, 255, 255, 0.55)',
        'glass-hover':
          '0 24px 48px -16px rgba(16, 24, 40, 0.18), inset 0 1px 0 rgba(255, 255, 255, 0.6)',
        glow: '0 0 0 1px rgba(158, 27, 84, 0.12), 0 14px 44px -12px rgba(158, 27, 84, 0.28)',
      },

      borderRadius: {
        card: '1rem',
      },

      backdropBlur: {
        xs: '2px',
      },
    },
  },
  plugins: [],
};
