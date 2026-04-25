/** @type {import("tailwindcss").Config} */
export default {
  content: ["./index.html","./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        navy: {
          50: "#f0f4ff",
          100: "#e0e9ff",
          500: "#1a3a6b",
          600: "#152f58",
          700: "#102445",
          800: "#0b1932",
          900: "#060e1f"
        }
      }
    }
  },
  plugins: []
}
