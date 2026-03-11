package com.tomcat.Cards.service;

import com.tomcat.Cards.dto.CardsDto;
import com.tomcat.Cards.model.Cards;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.smartcardio.CardNotPresentException;
import java.util.List;

public interface iCardsServices {
    boolean createNewCard(String mobileNumber) throws CardNotPresentException;

    List<Cards> fetchingAllCustomersCards();

    CardsDto fetchCardDetailsByCardNumber(String cardNumber);

    boolean cardDetailsUpdation(CardsDto cardsDto);

    boolean cardClosureHandler(String mobileNumber);

    Cards resetCardLimit(@Valid CardsDto card);

    CardsDto fetchCardDetailsByMobileNumber(String mobileNumber);
}
