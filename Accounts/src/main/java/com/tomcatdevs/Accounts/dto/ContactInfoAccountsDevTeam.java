package com.tomcatdevs.Accounts.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "accounts")
@Data
public class ContactInfoAccountsDevTeam {
    private String message;
    private HashMap<String,String> contactDetails;
    private List<String> onCallSupport;
}