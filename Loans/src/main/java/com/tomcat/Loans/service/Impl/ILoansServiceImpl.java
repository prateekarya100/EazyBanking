package com.tomcat.Loans.service.Impl;

import com.tomcat.Loans.constants.LoansConstants;
import com.tomcat.Loans.dto.LoansDto;
import com.tomcat.Loans.exception.LoanAlreadyExistsException;
import com.tomcat.Loans.exception.ResourceNotFoundException;
import com.tomcat.Loans.mapper.LoansMapper;
import com.tomcat.Loans.model.Loans;
import com.tomcat.Loans.repository.LoansRepository;
import com.tomcat.Loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ILoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    @Override
    public boolean createNewLoan(String mobileNumber)  {
        boolean loanCreated = false;
        Optional<Loans> loansOptional = loansRepository.findByMobileNumber(mobileNumber);
        if (loansOptional.isPresent()) {
            throw new LoanAlreadyExistsException("loan already exists with mobile number : "+mobileNumber);
        }else{
            loansRepository.save(createNewLoanAccount(mobileNumber));
            loanCreated = true;
        }
        return loanCreated;
    }

    private Loans createNewLoanAccount(String mobileNumber) {
        Loans loans = new Loans();

        long loanAccountNumber = 1000000000L + new Random().nextInt(900000000);
        loans.setLoanAccountNumber(String.valueOf(loanAccountNumber));
        loans.setMobileNumber(mobileNumber);
        loans.setTotalLoan(LoansConstants.TOTAL_LOAN_AMOUNT_DISBURSED);
        loans.setAmountPaid(0);
        loans.setOutstandingAmount(LoansConstants.TOTAL_LOAN_AMOUNT_DISBURSED);
        loans.setLoanType(LoansConstants.LOAN_TYPE_PERSONAL);
        return loans;
    }

    @Override
    public LoansDto fetchLoanDetails(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("loan","mobile",mobileNumber)
        );
        return LoansMapper.mapToDto(loans,new LoansDto());
    }

    /**
     * @param loansDto
     * @return
     */
    @Override
    public boolean updateLoanDetails(LoansDto loansDto) {
        boolean isLoanDetailsUpdated=false;
        Loans loans = loansRepository.findByLoanAccountNumber(loansDto.getLoanAccountNumber()).orElseThrow(
                ()->new ResourceNotFoundException("loan","mobile",loansDto.getMobileNumber())
        );
        LoansMapper.mapToLoans(loansDto,loans);
        loansRepository.save(loans);
        return isLoanDetailsUpdated=true;
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean loanClosure(String mobileNumber) {
        Optional<Loans> loansOptional = loansRepository.findByMobileNumber(mobileNumber);
        if (loansOptional.isPresent()) {
            loansRepository.delete(loansOptional.get());
        }else{
            throw new ResourceNotFoundException("loan","mobile",mobileNumber);
        }
        return true;
    }
}
