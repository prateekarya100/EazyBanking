export interface AccountsDto {
  accountNumber: number
  accountType: string
  branchAddress: string
}

export interface CustomerDto {
  name: string
  email: string
  mobileNumber: string
  accountsDto?: AccountsDto
}

export interface CardsDto {
  mobileNumber: string
  cardNumber: string
  cardType: string
  cardExpiryDate: string
  cardCVV: number
  cardName: string
  cardStatus: string
  cardIssuerBank: string
  totalLimit: number
  availableLimit: number
  amountUsed: number
}

export interface LoansDto {
  mobileNumber: string
  loanAccountNumber: string
  loanType: string
  totalLoan: number
  amountPaid: number
  outstandingAmount: number
}

export interface ConsolidatedCustomerDetailsDTO {
  name: string
  email: string
  mobileNumber: string
  accountsDto: AccountsDto
  cardsDto: CardsDto
  loansDto: LoansDto
}

export interface AccountStatusRequest {
  accountNumber: string
  reason: string
}

export interface AccountStatusResponse {
  accountNumber: string
  status: string
  message: string
  timestamp: string
}

export interface ResponseDto {
  statusCode?: string
  status?: string
  response: string
}
