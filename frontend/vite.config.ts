import { defineConfig } from "vite"
import react from "@vitejs/plugin-react"

export default defineConfig({
  plugins: [react()],
  server: {
    host: "0.0.0.0",
    port: 5173,
    proxy: {
      "/eazybank": {
        target: "http://localhost:8072",
        changeOrigin: true,
      }
    }
  }
})
