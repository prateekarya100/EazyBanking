package com.tomcat.Loans.service;

import com.tomcat.Loans.dto.LoansDto;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ILoansService {
    /**
     * @param mobileNumber
     * @return
     */
    boolean createNewLoan(String mobileNumber);

    LoansDto fetchLoanDetails(String mobileNumber);

    boolean updateLoanDetails(LoansDto loansDto);

    boolean loanClosure( String mobileNumber);
}
