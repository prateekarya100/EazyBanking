import { useState, useEffect, useCallback } from "react"
import { Plus, Search, RefreshCw, Pencil, Trash2, Eye, Lock, Unlock, Activity } from "lucide-react"
import { api } from "../lib/api"
import type { CustomerDto, AccountStatusResponse } from "../lib/types"
import Modal from "../components/Modal"
import PageHeader from "../components/PageHeader"
import StatusBadge from "../components/StatusBadge"
import { useToast, ToastContainer } from "../components/Toast"

const empty: CustomerDto = { name: "", email: "", mobileNumber: "", accountsDto: { accountNumber: 0, accountType: "Savings", branchAddress: "" } }

export default function Accounts() {
  const [accounts, setAccounts] = useState<any[]>([])
  const [loading, setLoading] = useState(false)
  const [search, setSearch] = useState("")
  const [modal, setModal] = useState("")
  const [selected, setSelected] = useState<CustomerDto | null>(null)
  const [statusInfo, setStatusInfo] = useState<AccountStatusResponse | null>(null)
  const [form, setForm] = useState<CustomerDto>(empty)
  const [freezeReason, setFreezeReason] = useState("")
  const [freezeAccNum, setFreezeAccNum] = useState("")
  const [submitting, setSubmitting] = useState(false)
  const { toasts, show, remove } = useToast()

  const load = useCallback(() => {
    setLoading(true)
    api.getAllAccounts().then(r => setAccounts(r.data || [])).catch(() => show("error", "Failed to load accounts")).finally(() => setLoading(false))
  }, [show])

  useEffect(() => { load() }, [load])

  const filtered = accounts.filter(a => !search || (a.mobileNumber||"").includes(search) || (a.accountNumber?.toString()||"").includes(search) || (a.accountType||"").toLowerCase().includes(search.toLowerCase()))

  const openView = (acc: any) => { api.fetchAccount(acc.mobileNumber||"").then(r => { setSelected(r.data); setModal("view") }).catch(() => show("error", "Failed")) }
  const openEdit = (acc: any) => { api.fetchAccount(acc.mobileNumber||"").then(r => { setForm(r.data); setModal("edit") }).catch(() => show("error", "Failed")) }
  const openStatus = (acc: any) => { api.getAccountStatus(String(acc.accountNumber)).then(r => { setStatusInfo(r.data); setModal("status") }).catch(() => show("error", "Failed")) }

  const handleCreate = async () => { setSubmitting(true); try { await api.createAccount(form); show("success", "Account created"); setModal(""); load() } catch(e: any) { show("error", e?.response?.data?.message||"Failed") } finally { setSubmitting(false) } }
  const handleUpdate = async () => { setSubmitting(true); try { await api.updateAccount(form); show("success", "Account updated"); setModal(""); load() } catch(e: any) { show("error", e?.response?.data?.message||"Failed") } finally { setSubmitting(false) } }
  const handleDelete = async (mob: string) => { if(!confirm("Close this account?")) return; try { await api.deleteAccount(mob); show("success", "Account closed"); load() } catch(e: any) { show("error", e?.response?.data?.message||"Failed") } }

  const handleFreeze = async (freeze: boolean) => {
    if (!freezeAccNum) return
    setSubmitting(true)
    try { const fn = freeze ? api.freezeAccount : api.unfreezeAccount; await fn({ accountNumber: freezeAccNum, reason: freezeReason }); show("success", freeze ? "Account frozen" : "Unfrozen"); setModal(""); load() }
    catch(e: any) { show("error", e?.response?.data?.message||"Failed") }
    finally { setSubmitting(false) }
  }

  const Field = ({ label, value }: { label: string; value?: string | number }) => (
    <div className="py-2 border-b border-gray-100 last:border-0">
      <dt className="text-xs font-medium text-gray-500 uppercase tracking-wide">{label}</dt>
      <dd className="mt-0.5 text-sm text-gray-900 font-medium">{value ?? "N/A"}</dd>
    </div>
  )
  const Inp = ({ label, val, onChange, placeholder, type = "text" }: { label: string; val: string; onChange: (v: string)=>void; placeholder?: string; type?: string }) => (
    <div>
      <label className="block text-xs font-medium text-gray-700 mb-1">{label}</label>
      <input type={type} className="input-field" value={val} onChange={e => onChange(e.target.value)} placeholder={placeholder || label} />
    </div>
  )

  return (
    <div className="p-8">
      <ToastContainer toasts={toasts} onClose={remove} />
      <PageHeader title="Accounts" subtitle="Manage bank account records" actions={<>
        <button onClick={() => { setForm(empty); setModal("create") }} className="btn-primary flex items-center gap-2"><Plus size={16} /> New Account</button>
        <button onClick={() => { setFreezeAccNum(""); setFreezeReason(""); setModal("freeze") }} className="btn-secondary flex items-center gap-2"><Lock size={16} /> Freeze/Unfreeze</button>
      </>} />
      <div className="card mb-4">
        <div className="p-4 flex gap-3">
          <div className="relative flex-1"><Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><input className="input-field pl-9" placeholder="Search mobile, account no, type" value={search} onChange={e => setSearch(e.target.value)} /></div>
          <button onClick={load} className="btn-secondary flex items-center gap-2"><RefreshCw size={16} /> Refresh</button>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-y border-gray-200"><tr>{["Account No.","Type","Branch","Mobile","Actions"].map(h => <th key={h} className="text-left text-xs font-semibold text-gray-500 uppercase px-4 py-3">{h}</th>)}</tr></thead>
            <tbody className="divide-y divide-gray-100">
              {loading ? <tr><td colSpan={5} className="text-center py-8 text-gray-400">Loading...</td></tr>
              : filtered.length===0 ? <tr><td colSpan={5} className="text-center py-8 text-gray-400">No accounts found</td></tr>
              : filtered.map((a,i) => (
                <tr key={i} className="hover:bg-gray-50">
                  <td className="px-4 py-3 text-sm font-mono text-blue-700 font-semibold">{a.accountNumber}</td>
                  <td className="px-4 py-3 text-sm">{a.accountType}</td>
                  <td className="px-4 py-3 text-sm text-gray-500">{a.branchAddress}</td>
                  <td className="px-4 py-3 text-sm font-medium">{a.mobileNumber}</td>
                  <td className="px-4 py-3"><div className="flex gap-1">
                    <button onClick={() => openView(a)} className="p-1.5 hover:bg-blue-50 text-blue-600 rounded" title="View"><Eye size={14} /></button>
                    <button onClick={() => openEdit(a)} className="p-1.5 hover:bg-amber-50 text-amber-600 rounded" title="Edit"><Pencil size={14} /></button>
                    <button onClick={() => openStatus(a)} className="p-1.5 hover:bg-purple-50 text-purple-600 rounded" title="Status"><Activity size={14} /></button>
                    <button onClick={() => handleDelete(a.mobileNumber||"")} className="p-1.5 hover:bg-red-50 text-red-600 rounded" title="Close"><Trash2 size={14} /></button>
                  </div></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="px-4 py-3 bg-gray-50 border-t text-xs text-gray-500">{filtered.length} account(s) shown</div>
      </div>

      <Modal open={modal==="create"} onClose={() => setModal("")} title="Create Account"><div className="space-y-3">
        <Inp label="Full Name" val={form.name} onChange={v => setForm(p => ({...p, name: v}))} />
        <Inp label="Email" val={form.email} onChange={v => setForm(p => ({...p, email: v}))} type="email" />
        <Inp label="Mobile Number" val={form.mobileNumber} onChange={v => setForm(p => ({...p, mobileNumber: v}))} />
        <div><label className="block text-xs font-medium text-gray-700 mb-1">Account Type</label><select className="input-field" value={form.accountsDto?.accountType} onChange={e => setForm(p => ({...p, accountsDto: {...p.accountsDto!, accountType: e.target.value}}))}>
          <option>Savings</option><option>Current</option><option>Fixed Deposit</option></select></div>
        <Inp label="Branch Address" val={form.accountsDto?.branchAddress||""} onChange={v => setForm(p => ({...p, accountsDto: {...p.accountsDto!, branchAddress: v}}))} />
        <div className="flex gap-2 pt-2"><button onClick={handleCreate} disabled={submitting} className="btn-primary flex-1">{submitting?"Creating...":"Create Account"}</button><button onClick={() => setModal("")} className="btn-secondary">Cancel</button></div>
      </div></Modal>

      <Modal open={modal==="edit"} onClose={() => setModal("")} title="Edit Account"><div className="space-y-3">
        <Inp label="Full Name" val={form.name} onChange={v => setForm(p => ({...p, name: v}))} />
        <Inp label="Email" val={form.email} onChange={v => setForm(p => ({...p, email: v}))} type="email" />
        <Inp label="Mobile" val={form.mobileNumber} onChange={v => setForm(p => ({...p, mobileNumber: v}))} />
        <Inp label="Account Type" val={form.accountsDto?.accountType||""} onChange={v => setForm(p => ({...p, accountsDto: {...p.accountsDto!, accountType: v}}))} />
        <Inp label="Branch" val={form.accountsDto?.branchAddress||""} onChange={v => setForm(p => ({...p, accountsDto: {...p.accountsDto!, branchAddress: v}}))} />
        <div className="flex gap-2 pt-2"><button onClick={handleUpdate} disabled={submitting} className="btn-primary flex-1">{submitting?"Saving...":"Save Changes"}</button><button onClick={() => setModal("")} className="btn-secondary">Cancel</button></div>
      </div></Modal>

      <Modal open={modal==="view"} onClose={() => setModal("")} title="Account Details">{selected && <dl><Field label="Name" value={selected.name} /><Field label="Email" value={selected.email} /><Field label="Mobile" value={selected.mobileNumber} /><Field label="Account Number" value={selected.accountsDto?.accountNumber} /><Field label="Account Type" value={selected.accountsDto?.accountType} /><Field label="Branch" value={selected.accountsDto?.branchAddress} /></dl>}</Modal>

      <Modal open={modal==="status"} onClose={() => setModal("")} title="Account Status">{statusInfo && <dl><Field label="Account Number" value={statusInfo.accountNumber} /><div className="py-2 border-b"><dt className="text-xs font-medium text-gray-500 uppercase">Status</dt><dd className="mt-1"><StatusBadge status={statusInfo.status} /></dd></div><Field label="Message" value={statusInfo.message} /><Field label="Timestamp" value={statusInfo.timestamp} /></dl>}</Modal>

      <Modal open={modal==="freeze"} onClose={() => setModal("")} title="Freeze / Unfreeze Account"><div className="space-y-3">
        <Inp label="Account Number" val={freezeAccNum} onChange={setFreezeAccNum} />
        <Inp label="Reason" val={freezeReason} onChange={setFreezeReason} />
        <div className="flex gap-2 pt-2"><button onClick={() => handleFreeze(true)} disabled={submitting||!freezeAccNum} className="btn-primary flex-1 flex items-center justify-center gap-2"><Lock size={15}/> Freeze</button><button onClick={() => handleFreeze(false)} disabled={submitting||!freezeAccNum} className="btn-success flex-1 flex items-center justify-center gap-2"><Unlock size={15}/> Unfreeze</button></div>
      </div></Modal>
    </div>
  )
}
