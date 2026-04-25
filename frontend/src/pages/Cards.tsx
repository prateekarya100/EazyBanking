import { useState, useEffect, useCallback } from "react"
import { Plus, Search, RefreshCw, Pencil, Trash2, Eye, RotateCcw } from "lucide-react"
import { api } from "../lib/api"
import type { CardsDto } from "../lib/types"
import Modal from "../components/Modal"
import PageHeader from "../components/PageHeader"
import StatusBadge from "../components/StatusBadge"
import { useToast, ToastContainer } from "../components/Toast"

const emptyCard: CardsDto = { mobileNumber:"", cardNumber:"", cardType:"Credit Card", cardExpiryDate:"", cardCVV:0, cardName:"", cardStatus:"ACTIVE", cardIssuerBank:"EazyBank", totalLimit:100000, availableLimit:100000, amountUsed:0 }

export default function Cards() {
  const [cards, setCards] = useState<any[]>([])
  const [loading, setLoading] = useState(false)
  const [search, setSearch] = useState("")
  const [modal, setModal] = useState("")
  const [selected, setSelected] = useState<CardsDto | null>(null)
  const [form, setForm] = useState<CardsDto>(emptyCard)
  const [issueMobile, setIssueMobile] = useState("")
  const [submitting, setSubmitting] = useState(false)
  const { toasts, show } = useToast()

  const load = useCallback(() => {
    setLoading(true)
    api.getAllCards().then(r => setCards(r.data||[])).catch(() => show("error","Failed to load cards")).finally(() => setLoading(false))
  }, [show])

  useEffect(() => { load() }, [load])

  const filtered = cards.filter(c => !search || (c.mobileNumber||"").includes(search) || (c.cardNumber||"").includes(search) || (c.cardType||"").toLowerCase().includes(search.toLowerCase()))

  const openView = (c: any) => { setSelected(c); setModal("view") }
  const openEdit = (c: any) => { setForm(c); setModal("edit") }

  const handleIssue = async () => {
    if (!issueMobile) return
    setSubmitting(true)
    try { await api.issueCard(issueMobile); show("success","Card issued"); setModal(""); load() }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
    finally { setSubmitting(false) }
  }

  const handleUpdate = async () => {
    setSubmitting(true)
    try { await api.updateCard(form); show("success","Card updated"); setModal(""); load() }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
    finally { setSubmitting(false) }
  }

  const handleDelete = async (mob: string) => {
    if (!confirm("Close this card?")) return
    try { await api.deleteCard(mob); show("success","Card closed"); load() }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
  }

  const handleResetLimit = async (c: any) => {
    try { await api.resetCardLimit({...c, amountUsed:0, availableLimit:c.totalLimit}); show("success","Limit reset"); load() }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
  }

  const Field = ({ label, value }: { label: string; value?: string | number }) => (<div className="py-2 border-b border-gray-100 last:border-0"><dt className="text-xs font-medium text-gray-500 uppercase">{label}</dt><dd className="mt-0.5 text-sm font-medium">{value ?? "N/A"}</dd></div>)
  const Inp = ({ label, val, onChange, type="text" }: { label: string; val: string|number; onChange: (v: string)=>void; type?: string }) => (<div><label className="block text-xs font-medium text-gray-700 mb-1">{label}</label><input type={type} className="input-field" value={val} onChange={e => onChange(e.target.value)} placeholder={label} /></div>)

  return (
    <div className="p-8">
      <ToastContainer toasts={toasts} onClose={() => {}} />
      <PageHeader title="Cards" subtitle="Issue and manage bank cards" actions={<button onClick={() => { setIssueMobile(""); setModal("issue") }} className="btn-primary flex items-center gap-2"><Plus size={16} /> Issue Card</button>} />
      <div className="card mb-4">
        <div className="p-4 flex gap-3">
          <div className="relative flex-1"><Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><input className="input-field pl-9" placeholder="Search mobile, card number, type" value={search} onChange={e => setSearch(e.target.value)} /></div>
          <button onClick={load} className="btn-secondary flex items-center gap-2"><RefreshCw size={16} /> Refresh</button>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-y border-gray-200"><tr>{["Card Number","Type","Card Name","Issuer","Limit","Status","Mobile","Actions"].map(h => <th key={h} className="text-left text-xs font-semibold text-gray-500 uppercase px-4 py-3">{h}</th>)}</tr></thead>
            <tbody className="divide-y divide-gray-100">
              {loading ? <tr><td colSpan={8} className="text-center py-8 text-gray-400">Loading...</td></tr>
              : filtered.length===0 ? <tr><td colSpan={8} className="text-center py-8 text-gray-400">No cards found</td></tr>
              : filtered.map((c,i) => (
                <tr key={i} className="hover:bg-gray-50">
                  <td className="px-4 py-3 text-sm font-mono text-purple-700 font-semibold">{c.cardNumber}</td>
                  <td className="px-4 py-3 text-sm">{c.cardType}</td>
                  <td className="px-4 py-3 text-sm">{c.cardName}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{c.cardIssuerBank}</td>
                  <td className="px-4 py-3 text-sm"><div className="text-xs text-gray-500">Total: {c.totalLimit?.toLocaleString()}</div><div className="text-green-600 font-medium">{c.availableLimit?.toLocaleString()} avail</div></td>
                  <td className="px-4 py-3"><StatusBadge status={c.cardStatus} /></td>
                  <td className="px-4 py-3 text-sm">{c.mobileNumber}</td>
                  <td className="px-4 py-3"><div className="flex gap-1">
                    <button onClick={() => openView(c)} className="p-1.5 hover:bg-blue-50 text-blue-600 rounded" title="View"><Eye size={14} /></button>
                    <button onClick={() => openEdit(c)} className="p-1.5 hover:bg-amber-50 text-amber-600 rounded" title="Edit"><Pencil size={14} /></button>
                    <button onClick={() => handleResetLimit(c)} className="p-1.5 hover:bg-green-50 text-green-600 rounded" title="Reset Limit"><RotateCcw size={14} /></button>
                    <button onClick={() => handleDelete(c.mobileNumber||"")} className="p-1.5 hover:bg-red-50 text-red-600 rounded" title="Close"><Trash2 size={14} /></button>
                  </div></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="px-4 py-3 bg-gray-50 border-t text-xs text-gray-500">{filtered.length} card(s) shown</div>
      </div>

      <Modal open={modal==="issue"} onClose={() => setModal("")} title="Issue New Card"><div className="space-y-3">
        <div><label className="block text-xs font-medium text-gray-700 mb-1">Mobile Number</label><input className="input-field" value={issueMobile} onChange={e => setIssueMobile(e.target.value)} placeholder="10 digit mobile number" /></div>
        <p className="text-xs text-gray-500">A new credit card will be issued for this customer with default limits.</p>
        <div className="flex gap-2 pt-2"><button onClick={handleIssue} disabled={submitting||!issueMobile} className="btn-primary flex-1">{submitting?"Issuing...":"Issue Card"}</button><button onClick={() => setModal("")} className="btn-secondary">Cancel</button></div>
      </div></Modal>

      <Modal open={modal==="view"} onClose={() => setModal("")} title="Card Details" wide>{selected && <dl>
        <Field label="Card Number" value={selected.cardNumber} />
        <Field label="Card Name" value={selected.cardName} />
        <Field label="Card Type" value={selected.cardType} />
        <Field label="Issuer Bank" value={selected.cardIssuerBank} />
        <Field label="Mobile" value={selected.mobileNumber} />
        <Field label="Total Limit" value={selected.totalLimit?.toLocaleString()} />
        <Field label="Available Limit" value={selected.availableLimit?.toLocaleString()} />
        <Field label="Amount Used" value={selected.amountUsed?.toLocaleString()} />
        <div className="py-2"><dt className="text-xs font-medium text-gray-500 uppercase">Status</dt><dd className="mt-1"><StatusBadge status={selected.cardStatus} /></dd></div>
      </dl>}</Modal>

      <Modal open={modal==="edit"} onClose={() => setModal("")} title="Edit Card"><div className="space-y-3">
        <Inp label="Card Name" val={form.cardName} onChange={v => setForm(p=>({...p,cardName:v}))} />
        <Inp label="Card Type" val={form.cardType} onChange={v => setForm(p=>({...p,cardType:v}))} />
        <Inp label="Total Limit" val={form.totalLimit} onChange={v => setForm(p=>({...p,totalLimit:Number(v)}))} type="number" />
        <div><label className="block text-xs font-medium text-gray-700 mb-1">Status</label><select className="input-field" value={form.cardStatus} onChange={e => setForm(p=>({...p,cardStatus:e.target.value}))}><option>ACTIVE</option><option>BLOCKED</option><option>EXPIRED</option></select></div>
        <div className="flex gap-2 pt-2"><button onClick={handleUpdate} disabled={submitting} className="btn-primary flex-1">{submitting?"Saving...":"Save Changes"}</button><button onClick={() => setModal("")} className="btn-secondary">Cancel</button></div>
      </div></Modal>
    </div>
  )
}
