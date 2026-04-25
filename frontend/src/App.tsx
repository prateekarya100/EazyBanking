import { Routes, Route } from "react-router-dom"
import Layout from "./components/Layout"
import Dashboard from "./pages/Dashboard"
import Accounts from "./pages/Accounts"
import Cards from "./pages/Cards"
import Loans from "./pages/Loans"
import Customer360 from "./pages/Customer360"

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Dashboard />} />
        <Route path="accounts" element={<Accounts />} />
        <Route path="cards" element={<Cards />} />
        <Route path="loans" element={<Loans />} />
        <Route path="customer" element={<Customer360 />} />
      </Route>
    </Routes>
  )
}
