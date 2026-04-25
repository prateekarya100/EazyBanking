import { useState, useEffect, useCallback } from "react"
import { Plus, Search, RefreshCw, Pencil, Trash2, Eye } from "lucide-react"
import { api } from "../lib/api"
import type { LoansDto } from "../lib/types"
import Modal from "../components/Modal"
import PageHeader from "../components/PageHeader"
import { useToast, ToastContainer } from "../components/Toast"

const emptyLoan: LoansDto = { mobileNumber:"", loanAccountNumber:"", loanType:"Home Loan", totalLoan:500000, amountPaid:0, outstandingAmount:500000 }

export default function Loans() {
  const [loans, setLoans] = useState<LoansDto[]>([])
  const [loading, setLoading] = useState(false)
  const [search, setSearch] = useState("")
  const [modal, setModal] = useState("")
  const [selected, setSelected] = useState<LoansDto | null>(null)
  const [form, setForm] = useState<LoansDto>(emptyLoan)
  const [createMobile, setCreateMobile] = useState("")
  const [submitting, setSubmitting] = useState(false)
  const { toasts, show, remove } = useToast()

  const load = useCallback((mob?: string) => {
    if (mob) {
      setLoading(true)
      api.fetchLoan(mob).then(r => setLoans([r.data])).catch(() => { show("error","Loan not found"); setLoans([]) }).finally(() => setLoading(false))
    }
  }, [show])

  useEffect(() => {}, [])

  const filtered = loans.filter(l => !search || (l.mobileNumber||"").includes(search) || (l.loanAccountNumber||"").includes(search) || (l.loanType||"").toLowerCase().includes(search.toLowerCase()))

  const handleCreate = async () => {
    if (!createMobile) return
    setSubmitting(true)
    try { await api.createLoan(createMobile); show("success","Loan created"); setModal(""); load(createMobile) }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
    finally { setSubmitting(false) }
  }

  const handleUpdate = async () => {
    setSubmitting(true)
    try { await api.updateLoan(form); show("success","Loan updated"); setModal(""); load(form.mobileNumber) }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
    finally { setSubmitting(false) }
  }

  const handleDelete = async (mob: string) => {
    if(!confirm("Close this loan?")) return
    try { await api.deleteLoan(mob); show("success","Loan closed"); setLoans([]) }
    catch(e: any) { show("error", e?.response?.data?.response||"Failed") }
  }

  const Field = ({ label, value }: { label: string; value?: string | number }) => (<div className="py-2 border-b border-gray-100 last:border-0"><dt className="text-xs font-medium text-gray-500 uppercase">{label}</dt><dd className="mt-0.5 text-sm font-medium">{value ?? "N/A"}</dd></div>)
  const Inp = ({ label, val, onChange, type="text" }: { label: string; val: string|number; onChange: (v:string)=>void; type?: string }) => (<div><label className="block text-xs font-medium text-gray-700 mb-1">{label}</label><input type={type} className="input-field" value={val} onChange={e => onChange(e.target.value)} placeholder={label} /></div>)

  return (
    <div className="p-8">
      <ToastContainer toasts={toasts} onClose={remove} />
      <PageHeader title="Loans" subtitle="Manage loan accounts" actions={<button onClick={() => { setCreateMobile(""); setModal("create") }} className="btn-primary flex items-center gap-2"><Plus size={16} /> New Loan</button>} />
      <div className="card mb-4">
        <div className="p-4 flex gap-3 border-b">
          <div className="relative flex-1"><Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><input className="input-field pl-9" placeholder="Enter mobile number to search loans" value={search} onChange={e => setSearch(e.target.value)} /></div>
          <button onClick={() => load(search)} disabled={!search} className="btn-primary">Search</button>
          <button onClick={() => { setLoans([]); setSearch("") }} className="btn-secondary flex items-center gap-2"><RefreshCw size={16} /> Clear</button>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-y border-gray-200"><tr>{["Loan Account No.","Type","Total Loan","Paid","Outstanding","Mobile","Actions"].map(h => <th key={h} className="text-left text-xs font-semibold text-gray-500 uppercase px-4 py-3">{h}</th>)}</tr></thead>
            <tbody className="divide-y divide-gray-100">
              {loading ? <tr><td colSpan={7} className="text-center py-8 text-gray-400">Loading...</td></tr>
              : filtered.length===0 ? <tr><td colSpan={7} className="text-center py-8 text-gray-400">Search by mobile number to view loans</td></tr>
              : filtered.map((l,i) => (
                <tr key={i} className="hover:bg-gray-50">
                  <td className="px-4 py-3 text-sm font-mono text-green-700 font-semibold">{l.loanAccountNumber}</td>
                  <td className="px-4 py-3 text-sm">{l.loanType}</td>
                  <td className="px-4 py-3 text-sm font-medium">{l.totalLoan?.toLocaleString()}</td>
                  <td className="px-4 py-3 text-sm text-green-600">{l.amountPaid?.toLocaleString()}</td>
                  <td className="px-4 py-3 text-sm text-red-600 font-medium">{l.outstandingAmount?.toLocaleString()}</td>
                  <td className="px-4 py-3 text-sm">{l.mobileNumber}</td>
                  <td className="px-4 py-3"><div className="flex gap-1">
                    <button onClick={() => { setSelected(l); setModal("view") }} className="p-1.5 hover:bg-blue-50 text-blue-600 rounded"><Eye size={14} /></button>
                    <button onClick={() => { setForm(l); setModal("edit") }} className="p-1.5 hover:bg-amber-50 text-amber-600 rounded"><Pencil size={14} /></button>
                    <button onClick={() => handleDelete(l.mobileNumber||"")} className="p-1.5 hover:bg-red-50 text-red-600 rounded"><Trash2 size={14} /></button>
                  </div></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="px-4 py-3 bg-gray-50 border-t text-xs text-gray-500">{filtered.length} loan(s) shown</div>
      </div>

      <Modal open={modal==="create"} onClose={() => setModal("")} title="Create Loan"><div className="space-y-3">
        <div><label className="block text-xs font-medium text-gray-700 mb-1">Mobile Number</label><input className="input-field" value={createMobile} onChange={e => setCreateMobile(e.target.value)} placeholder="10 digit mobile number" /></div>
        <p className="text-xs text-gray-500">A new loan account will be created with default terms for this mobile number.</p>
        <div className="flex gap-2 pt-2"><button onClick={handleCreate} disabled={submitting||!createMobile} className="btn-primary flex-1">{submitting?"Creating...":"Create Loan"}</button><button onClick={() => setModal("")} className="btn-secondary">Cancel</button></div>
      </div></Modal>

      <Modal open={modal==="view"} onClose={() => setModal("")} title="Loan Details">{selected && <dl>
        <Field label="Loan Account Number" value={selected.loanAccountNumber} />
        <Field label="Loan Type" value={selected.loanType} />
        <Field label="Mobile" value={selected.mobileNumber} />
        <Field label="Total Loan" value={selected.totalLoan?.toLocaleString()} />
        <Field label="Amount Paid" value={selected.amountPaid?.toLocaleString()} />
        <Field label="Outstanding" value={selected.outstandingAmount?.toLocaleString()} />
        <div className="mt-4 bg-gray-50 rounded-lg p-3"><div className="flex justify-between text-xs text-gray-500 mb-2"><span>Repayment Progress</span><span>{selected.totalLoan ? Math.round((selected.amountPaid/selected.totalLoan)*100) : 0}%</span></div><div className="bg-gray-200 rounded-full h-2"><div className="bg-green-500 h-2 rounded-full" style={{width: selected.totalLoan ? Math.round((selected.amountPaid/selected.totalLoan)*100)+"%" : "0%"}}></div></div></div>
      </dl>}</Modal>

      <Modal open={modal==="edit"} onClose={() => setModal("")} title="Update Loan"><div className="space-y-3">
        <Inp label="Loan Type" val={form.loanType} onChange={v => setForm(p=>({...p,loanType:v}))} />
        <Inp label="Total Loan" val={form.totalLoan} onChange={v => setForm(p=>({...p,totalLoan:Number(v)}))} type="number" />
        <Inp label="Amount Paid" val={form.amountPaid} onChange={v => setForm(p=>({...p,amountPaid:Number(v)}))} type="number" />
        <Inp label="Outstanding Amount" val={form.outstandingAmount} onChange={v => setForm(p=>({...p,outstandingAmount:Number(v)}))} type="number" />
        <div className="flex gap-2 pt-2"><button onClick={handleUpdate} disabled={submitting} className="btn-primary flex-1">{submitting?"Saving...":"Save Changes"}</button><button onClick={() => setModal("")} className="btn-secondary">Cancel</button></div>
      </div></Modal>
    </div>
  )
}
