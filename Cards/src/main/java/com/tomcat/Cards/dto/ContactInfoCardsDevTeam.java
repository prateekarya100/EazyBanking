package com.tomcat.Cards.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "cards")
@Data
public class ContactInfoCardsDevTeam {
    private String message;
    private HashMap<String,String> contactDetails;
    private List<String> onCallSupport;
}
