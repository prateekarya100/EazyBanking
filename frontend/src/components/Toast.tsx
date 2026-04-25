import { useState, useCallback } from "react"
import { CheckCircle, XCircle, X } from "lucide-react"

type ToastType = "success" | "error"
interface Toast { id: number; type: ToastType; message: string }

let toastFn: ((type: ToastType, message: string) => void) | null = null

export function useToast() {
  const [toasts, setToasts] = useState<Toast[]>([])
  const show = useCallback((type: ToastType, message: string) => {
    const id = Date.now()
    setToasts(p => [...p, { id, type, message }])
    setTimeout(() => setToasts(p => p.filter(t => t.id !== id)), 4000)
  }, [])
  return { toasts, show }
}

export function ToastContainer({ toasts, onClose }: { toasts: { id: number; type: string; message: string }[]; onClose: (id: number) => void }) {
  return (
    <div className="fixed bottom-6 right-6 space-y-2 z-50">
      {toasts.map(t => (
        <div key={t.id} className={"flex items-center gap-3 px-4 py-3 rounded-lg shadow-lg text-white text-sm min-w-64 " + (t.type === "success" ? "bg-green-600" : "bg-red-600")}>
          {t.type === "success" ? <CheckCircle size={16} /> : <XCircle size={16} />}
          <span className="flex-1">{t.message}</span>
          <button onClick={() => onClose(t.id)} className="opacity-70 hover:opacity-100"><X size={14} /></button>
        </div>
      ))}
    </div>
  )
}
