package com.tomcat.Loans.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "loans")
@Data
public class ContactInfoLoansDevTeam {
    private String message;
    private HashMap<String,String> contactDetails;
    private List<String> onCallSupport;
}
