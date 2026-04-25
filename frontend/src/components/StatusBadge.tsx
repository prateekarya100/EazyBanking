export default function StatusBadge({ status }: { status: string }) {
  const s = (status || "").toUpperCase()
  const cls = s === "ACTIVE" ? "bg-green-100 text-green-700" : s === "FROZEN" ? "bg-blue-100 text-blue-700" : s === "CLOSED" ? "bg-gray-100 text-gray-600" : "bg-yellow-100 text-yellow-700"
  return <span className={"inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium " + cls}>{status || "N/A"}</span>
}
