#!/usr/bin/env node
/**
 * EazyBank CORS Proxy
 * Runs locally on your machine alongside the Docker backend.
 * Forwards requests to the Gateway and adds CORS headers so
 * the browser preview can reach your local Docker services.
 *
 * Usage:
 *   node cors-proxy.js
 *   node cors-proxy.js --gateway http://localhost:8072 --port 3001
 */
const http = require("http")
const https = require("https")

const args = process.argv.slice(2)
const getArg = (flag, def) => { const i = args.indexOf(flag); return i !== -1 ? args[i + 1] : def }

const GATEWAY = getArg("--gateway", "http://localhost:8072")
const PORT = parseInt(getArg("--port", "3001"), 10)
const gwUrl = new URL(GATEWAY)
const isHttps = gwUrl.protocol === "https:"
const gwHost = gwUrl.hostname
const gwPort = parseInt(gwUrl.port || (isHttps ? "443" : "80"), 10)

const CORS_HEADERS = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "GET, POST, PUT, PATCH, DELETE, OPTIONS",
  "Access-Control-Allow-Headers": "Content-Type, Authorization, X-Requested-With, X-User-Id",
  "Access-Control-Allow-Credentials": "true",
  "Access-Control-Max-Age": "86400",
}

const server = http.createServer((req, res) => {
  if (req.method === "OPTIONS") {
    res.writeHead(204, CORS_HEADERS)
    res.end()
    return
  }

  const options = {
    hostname: gwHost,
    port: gwPort,
    path: req.url,
    method: req.method,
    headers: { ...req.headers, host: gwHost + ":" + gwPort },
  }

  const proto = isHttps ? https : http
  const proxyReq = proto.request(options, (proxyRes) => {
    const headers = { ...proxyRes.headers, ...CORS_HEADERS }
    res.writeHead(proxyRes.statusCode, headers)
    proxyRes.pipe(res, { end: true })
  })

  proxyReq.on("error", (err) => {
    console.error("[cors-proxy] Gateway unreachable:", err.message)
    res.writeHead(502, { ...CORS_HEADERS, "Content-Type": "application/json" })
    res.end(JSON.stringify({ error: "Gateway unreachable", message: err.message, gateway: GATEWAY }))
  })

  req.pipe(proxyReq, { end: true })
})

server.listen(PORT, () => {
  console.log("")
  console.log("  EazyBank CORS Proxy running")
  console.log("  Proxy : http://localhost:" + PORT)
  console.log("  Target: " + GATEWAY)
  console.log("")
  console.log("  Browser API calls now work with no CORS errors.")
  console.log("  Press Ctrl+C to stop.")
  console.log("")
})
