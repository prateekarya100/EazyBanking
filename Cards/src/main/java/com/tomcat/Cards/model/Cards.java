package com.tomcat.Cards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder @ToString
@JsonIgnoreProperties({"createdAt","createdBy","updatedAt","updatedBy"})
public class Cards extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardsId;

    private String mobileNumber;
    private String cardNumber;
    private String cardType;
    private LocalDateTime cardExpiryDate;
    private long cardCVV;
    private String cardName;
    private String cardStatus;
    private String cardIssuerBank;
    private long totalLimit;
    private long availableLimit;
    private long amountUsed;
}
