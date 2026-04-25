import { useState, useCallback, useRef } from "react"
import { CheckCircle, XCircle, X } from "lucide-react"

type ToastType = "success" | "error"
interface Toast { id: number; type: ToastType; message: string }

export function useToast() {
  const [toasts, setToasts] = useState<Toast[]>([])
  const counter = useRef(0)

  const remove = useCallback((id: number) => {
    setToasts(p => p.filter(t => t.id !== id))
  }, [])

  const show = useCallback((type: ToastType, message: string) => {
    const id = ++counter.current
    setToasts(p => [...p, { id, type, message }])
    setTimeout(() => remove(id), 4000)
  }, [remove])

  return { toasts, show, remove }
}

export function ToastContainer({ toasts, onClose }: { toasts: Toast[]; onClose: (id: number) => void }) {
  return (
    <div className="fixed bottom-6 right-6 space-y-2 z-50 pointer-events-none">
      {toasts.map(t => (
        <div key={t.id} className={"pointer-events-auto flex items-center gap-3 px-4 py-3 rounded-lg shadow-lg text-white text-sm min-w-72 max-w-sm animate-pulse-once " + (t.type === "success" ? "bg-green-600" : "bg-red-600")}>
          {t.type === "success" ? <CheckCircle size={16} className="flex-shrink-0" /> : <XCircle size={16} className="flex-shrink-0" />}
          <span className="flex-1">{t.message}</span>
          <button onClick={() => onClose(t.id)} className="opacity-70 hover:opacity-100 flex-shrink-0"><X size={14} /></button>
        </div>
      ))}
    </div>
  )
}
