package com.tomcat.Loans.mapper;

import com.tomcat.Loans.dto.LoansDto;
import com.tomcat.Loans.model.Loans;

public class LoansMapper {

    public static Loans mapToLoans(LoansDto loansDto,Loans loans) {
        loans.setLoanAccountNumber(loansDto.getLoanAccountNumber());
        loans.setLoanType(loansDto.getLoanType());
        loans.setMobileNumber(loansDto.getMobileNumber());
        loans.setTotalLoan(loansDto.getTotalLoan());
        loans.setAmountPaid(loans.getAmountPaid());
        loans.setOutstandingAmount(loans.getOutstandingAmount());
        return loans;
    }

    public static LoansDto mapToDto(Loans loans,LoansDto loansDto) {
        loansDto.setLoanAccountNumber(loans.getLoanAccountNumber());
        loansDto.setLoanType(loans.getLoanType());
        loansDto.setMobileNumber(loans.getMobileNumber());
        loansDto.setTotalLoan(loans.getTotalLoan());
        loans.setAmountPaid(loans.getAmountPaid());
        loansDto.setOutstandingAmount(loans.getOutstandingAmount());
        return loansDto;
    }
}
