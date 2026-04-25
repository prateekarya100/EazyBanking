import { NavLink } from "react-router-dom"
import { Building2, CreditCard, Landmark, LayoutDashboard, Users, ChevronRight } from "lucide-react"

const nav = [
  { to: "/", icon: LayoutDashboard, label: "Dashboard" },
  { to: "/accounts", icon: Building2, label: "Accounts" },
  { to: "/cards", icon: CreditCard, label: "Cards" },
  { to: "/loans", icon: Landmark, label: "Loans" },
  { to: "/customer", icon: Users, label: "Customer View" },
]

export default function Sidebar() {
  return (
    <aside className="w-64 min-h-screen bg-slate-900 text-white flex flex-col">
      <div className="p-6 border-b border-slate-700">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-blue-500 rounded-lg flex items-center justify-center">
            <Landmark size={20} className="text-white" />
          </div>
          <div>
            <h1 className="font-bold text-lg leading-none">EazyBank</h1>
            <p className="text-slate-400 text-xs mt-1">Admin Portal</p>
          </div>
        </div>
      </div>
      <nav className="flex-1 p-4 space-y-1">
        {nav.map(({ to, icon: Icon, label }) => (
          <NavLink
            key={to}
            to={to}
            end={to === "/"}
            className={({ isActive }) =>
              "flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all duration-150 group " +
              (isActive ? "bg-blue-600 text-white" : "text-slate-300 hover:bg-slate-800 hover:text-white")
            }
          >
            <Icon size={18} />
            <span className="flex-1">{label}</span>
            <ChevronRight size={14} className="opacity-0 group-hover:opacity-100 transition-opacity" />
          </NavLink>
        ))}
      </nav>
      <div className="p-4 border-t border-slate-700">
        <p className="text-slate-500 text-xs text-center">EazyBank v1.0 &copy; 2024</p>
      </div>
    </aside>
  )
}
