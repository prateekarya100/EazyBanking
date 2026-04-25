import { useState } from "react"
import { Search, User, Building2, CreditCard, Landmark } from "lucide-react"
import { api } from "../lib/api"
import type { ConsolidatedCustomerDetailsDTO } from "../lib/types"
import StatusBadge from "../components/StatusBadge"
import { useToast, ToastContainer } from "../components/Toast"

export default function Customer360() {
  const [mobile, setMobile] = useState("")
  const [data, setData] = useState<ConsolidatedCustomerDetailsDTO | null>(null)
  const [loading, setLoading] = useState(false)
  const [notFound, setNotFound] = useState(false)
  const { toasts, show, remove } = useToast()

  const search = async () => {
    if (!mobile) return
    setLoading(true); setData(null); setNotFound(false)
    try { const r = await api.getConsolidated(mobile); setData(r.data) }
    catch(e: any) { setNotFound(true); show("error", e?.response?.data?.message||"Customer not found") }
    finally { setLoading(false) }
  }

  const Field = ({ label, value }: { label: string; value?: string | number }) => (
    <div className="flex justify-between py-2 border-b border-gray-100 last:border-0">
      <span className="text-sm text-gray-500">{label}</span>
      <span className="text-sm font-medium text-gray-900">{value ?? "N/A"}</span>
    </div>
  )

  const Section = ({ title, icon: Icon, color, children }: { title: string; icon: any; color: string; children: React.ReactNode }) => (
    <div className="card p-6">
      <div className={"flex items-center gap-3 mb-4 pb-3 border-b"}>
        <div className={"w-9 h-9 rounded-lg flex items-center justify-center " + color}><Icon size={18} /></div>
        <h3 className="font-semibold text-gray-900">{title}</h3>
      </div>
      {children}
    </div>
  )

  return (
    <div className="p-8">
      <ToastContainer toasts={toasts} onClose={remove} />
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">Customer 360 View</h1>
        <p className="text-gray-500 text-sm mt-1">Get consolidated account, card and loan details for a customer</p>
      </div>

      <div className="card p-6 mb-6">
        <div className="flex gap-3">
          <div className="relative flex-1"><Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" /><input className="input-field pl-9" placeholder="Enter mobile number" value={mobile} onChange={e => setMobile(e.target.value)} onKeyDown={e => e.key==="Enter" && search()} /></div>
          <button onClick={search} disabled={loading || !mobile} className="btn-primary px-6">{loading ? "Searching..." : "Search"}</button>
        </div>
      </div>

      {notFound && !loading && (
        <div className="card p-12 text-center">
          <User size={48} className="mx-auto text-gray-300 mb-3" />
          <p className="text-gray-500">No customer found for this mobile number</p>
        </div>
      )}

      {data && (
        <div className="space-y-4">
          <Section title="Customer Information" icon={User} color="bg-blue-50 text-blue-600">
            <Field label="Full Name" value={data.name} />
            <Field label="Email" value={data.email} />
            <Field label="Mobile Number" value={data.mobileNumber} />
          </Section>

          {data.accountsDto && (
            <Section title="Account Details" icon={Building2} color="bg-indigo-50 text-indigo-600">
              <Field label="Account Number" value={data.accountsDto.accountNumber} />
              <Field label="Account Type" value={data.accountsDto.accountType} />
              <Field label="Branch Address" value={data.accountsDto.branchAddress} />
            </Section>
          )}

          {data.cardsDto && (
            <Section title="Card Details" icon={CreditCard} color="bg-purple-50 text-purple-600">
              <Field label="Card Number" value={data.cardsDto.cardNumber} />
              <Field label="Card Type" value={data.cardsDto.cardType} />
              <Field label="Card Name" value={data.cardsDto.cardName} />
              <Field label="Total Limit" value={data.cardsDto.totalLimit?.toLocaleString()} />
              <Field label="Available Limit" value={data.cardsDto.availableLimit?.toLocaleString()} />
              <div className="flex justify-between py-2 border-b border-gray-100 last:border-0">
                <span className="text-sm text-gray-500">Status</span>
                <StatusBadge status={data.cardsDto.cardStatus} />
              </div>
            </Section>
          )}

          {data.loansDto && (
            <Section title="Loan Details" icon={Landmark} color="bg-green-50 text-green-600">
              <Field label="Loan Account Number" value={data.loansDto.loanAccountNumber} />
              <Field label="Loan Type" value={data.loansDto.loanType} />
              <Field label="Total Loan" value={data.loansDto.totalLoan?.toLocaleString()} />
              <Field label="Amount Paid" value={data.loansDto.amountPaid?.toLocaleString()} />
              <Field label="Outstanding" value={data.loansDto.outstandingAmount?.toLocaleString()} />
              <div className="mt-3 bg-gray-50 rounded-lg p-3"><div className="flex justify-between text-xs text-gray-500 mb-1"><span>Repayment</span><span>{data.loansDto.totalLoan ? Math.round((data.loansDto.amountPaid/data.loansDto.totalLoan)*100) : 0}%</span></div><div className="bg-gray-200 rounded-full h-2"><div className="bg-green-500 h-2 rounded-full" style={{width:(data.loansDto.totalLoan ? Math.round((data.loansDto.amountPaid/data.loansDto.totalLoan)*100):0)+"%"}}></div></div></div>
            </Section>
          )}
        </div>
      )}
    </div>
  )
}
