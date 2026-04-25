import axios from "axios"
import type { CustomerDto, CardsDto, LoansDto, AccountStatusRequest } from "./types"

const ACCOUNTS = "/eazybank/accounts/api"
const CARDS = "/eazybank/cards/api"
const LOANS = "/eazybank/loans/api"

export const api = {
  createAccount: (data: CustomerDto) => axios.post(ACCOUNTS + "/createAccount", data),
  fetchAccount: (mobileNumber: string) => axios.get(ACCOUNTS + "/fetchAccount", { params: { mobileNumber } }),
  updateAccount: (data: CustomerDto) => axios.put(ACCOUNTS + "/updateAccount", data),
  deleteAccount: (mobileNumber: string) => axios.delete(ACCOUNTS + "/closureAccount", { params: { mobileNumber } }),
  getAllAccounts: () => axios.get(ACCOUNTS + "/get-all-accounts"),
  getAccountStatus: (accountNumber: string) => axios.get(ACCOUNTS + "/status/" + accountNumber),
  freezeAccount: (data: AccountStatusRequest) => axios.post(ACCOUNTS + "/freeze", data),
  unfreezeAccount: (data: AccountStatusRequest) => axios.post(ACCOUNTS + "/unfreeze", data),
  getAccountsContactInfo: () => axios.get(ACCOUNTS + "/contact-info"),
  getConsolidated: (mobileNumber: string) => axios.get(ACCOUNTS + "/consolidated/CustomerDetails", { params: { mobileNumber } }),
  issueCard: (mobileNumber: string) => axios.post(CARDS + "/issueCard", null, { params: { mobileNumber } }),
  fetchCardByNumber: (cardNumber: string) => axios.get(CARDS + "/fetchCard", { params: { cardNumber } }),
  fetchCardByMobile: (mobileNumber: string) => axios.get(CARDS + "/fetch-card-by-mobileNumber", { params: { mobileNumber } }),
  updateCard: (data: CardsDto) => axios.put(CARDS + "/updateCard", data),
  deleteCard: (mobileNumber: string) => axios.delete(CARDS + "/cardClosure", { params: { mobileNumber } }),
  getAllCards: () => axios.get(CARDS + "/all-bank-cards"),
  resetCardLimit: (data: CardsDto) => axios.patch(CARDS + "/limit-reset", data),
  createLoan: (mobileNumber: string) => axios.post(LOANS + "/createLoan", null, { params: { mobileNumber } }),
  fetchLoan: (mobileNumber: string) => axios.get(LOANS + "/fetchLoan", { params: { mobileNumber } }),
  updateLoan: (data: LoansDto) => axios.put(LOANS + "/updateLoan", data),
  deleteLoan: (mobileNumber: string) => axios.delete(LOANS + "/loanClosure", { params: { mobileNumber } }),
}
