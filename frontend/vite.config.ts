import { defineConfig, loadEnv } from "vite"
import react from "@vitejs/plugin-react"

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), "")
  const gatewayUrl = env.VITE_GATEWAY_URL || "http://localhost:8072"

  return {
    plugins: [react()],
    server: {
      host: "0.0.0.0",
      port: 5173,
      warmup: {
        clientFiles: [
          "./src/main.tsx",
          "./src/App.tsx",
          "./src/pages/Dashboard.tsx",
          "./src/pages/Accounts.tsx",
          "./src/pages/Cards.tsx",
          "./src/pages/Loans.tsx",
          "./src/pages/Customer360.tsx",
        ],
      },
      proxy: {
        "/eazybank": {
          target: gatewayUrl,
          changeOrigin: true,
        }
      }
    }
  }
})
