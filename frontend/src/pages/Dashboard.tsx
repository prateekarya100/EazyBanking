import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Building2, CreditCard, Landmark, Users, ArrowRight, Activity } from "lucide-react"
import { api } from "../lib/api"

export default function Dashboard() {
  const [counts, setCounts] = useState({ accounts: 0, cards: 0 })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.allSettled([api.getAllAccounts(), api.getAllCards()]).then(([acc, cards]) => {
      setCounts({
        accounts: acc.status === "fulfilled" ? (acc.value.data?.length ?? 0) : 0,
        cards: cards.status === "fulfilled" ? (cards.value.data?.length ?? 0) : 0,
      })
      setLoading(false)
    })
  }, [])

  const stats = [
    { label: "Total Accounts", value: loading ? "..." : counts.accounts, icon: Building2, color: "bg-blue-50 text-blue-600", link: "/accounts" },
    { label: "Total Cards", value: loading ? "..." : counts.cards, icon: CreditCard, color: "bg-purple-50 text-purple-600", link: "/cards" },
    { label: "Active Services", value: "3", icon: Activity, color: "bg-green-50 text-green-600", link: "/" },
    { label: "Microservices", value: "5", icon: Users, color: "bg-orange-50 text-orange-600", link: "/" },
  ]

  const quickActions = [
    { icon: Building2, title: "Manage Accounts", desc: "Create, view, update and manage bank accounts", link: "/accounts", color: "border-blue-200 hover:border-blue-400" },
    { icon: CreditCard, title: "Manage Cards", desc: "Issue and manage credit/debit cards", link: "/cards", color: "border-purple-200 hover:border-purple-400" },
    { icon: Landmark, title: "Manage Loans", desc: "Create and track loan accounts", link: "/loans", color: "border-green-200 hover:border-green-400" },
    { icon: Users, title: "Customer 360", desc: "Full consolidated view per customer", link: "/customer", color: "border-orange-200 hover:border-orange-400" },
  ]

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Welcome back!</h1>
        <p className="text-gray-500 mt-1">EazyBank Administration Portal</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        {stats.map(({ label, value, icon: Icon, color, link }) => (
          <Link to={link} key={label} className="stat-card hover:shadow-md transition-shadow cursor-pointer">
            <div className={"w-10 h-10 rounded-lg flex items-center justify-center mb-4 " + color}>
              <Icon size={20} />
            </div>
            <p className="text-2xl font-bold text-gray-900">{value}</p>
            <p className="text-gray-500 text-sm mt-1">{label}</p>
          </Link>
        ))}
      </div>

      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {quickActions.map(({ icon: Icon, title, desc, link, color }) => (
            <Link to={link} key={title} className={"card p-6 border-2 transition-all hover:shadow-md flex items-center gap-4 group " + color}>
              <div className="w-12 h-12 bg-gray-100 rounded-xl flex items-center justify-center flex-shrink-0">
                <Icon size={22} className="text-gray-600" />
              </div>
              <div className="flex-1">
                <h3 className="font-semibold text-gray-900">{title}</h3>
                <p className="text-gray-500 text-sm mt-0.5">{desc}</p>
              </div>
              <ArrowRight size={18} className="text-gray-400 group-hover:text-gray-600 transition-colors" />
            </Link>
          ))}
        </div>
      </div>

      <div className="mt-8 bg-gradient-to-r from-blue-600 to-blue-700 rounded-2xl p-6 text-white">
        <h2 className="text-xl font-bold mb-2">System Architecture</h2>
        <p className="text-blue-100 text-sm mb-4">This portal connects to EazyBank microservices via the Gateway (port 8072)</p>
        <div className="flex flex-wrap gap-2">
          {["Gateway :8072","Accounts :8081","Cards :8082","Loans :8083","Eureka :8761","Config :8071"].map(s => (
            <span key={s} className="bg-white/20 text-white text-xs px-3 py-1.5 rounded-full font-medium">{s}</span>
          ))}
        </div>
      </div>
    </div>
  )
}
