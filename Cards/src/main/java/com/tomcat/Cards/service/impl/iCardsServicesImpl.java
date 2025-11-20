package com.tomcat.Cards.service.impl;

import com.tomcat.Cards.constants.CardsConstants;
import com.tomcat.Cards.dto.CardsDto;
import com.tomcat.Cards.exception.CreditCardAlreadyExists;
import com.tomcat.Cards.exception.ResourceNotFoundException;
import com.tomcat.Cards.mapper.CardsMapper;
import com.tomcat.Cards.model.Cards;
import com.tomcat.Cards.repository.CardsRepository;
import com.tomcat.Cards.service.iCardsServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class iCardsServicesImpl implements iCardsServices {

    private CardsRepository cardsRepository;

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean createNewCard(String mobileNumber) {
            Optional<Cards> cards = Optional.ofNullable(cardsRepository.findByMobileNumber(mobileNumber));
            if (cards.isPresent()) {
                throw new CreditCardAlreadyExists("card","mobile",mobileNumber);
            }else {
                cardsRepository.save(newCardIssued(mobileNumber));
                return true;
            }
    }

    /**
     * @return
     */
    @Override
    public List<Cards> fetchingAllCustomersCards() {
        return cardsRepository.findAll();
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public CardsDto fetchCardDetailsByCardNumber(String cardNumber) {
        Cards cards = cardsRepository.findByCardNumber(cardNumber).orElseThrow(
                ()->new ResourceNotFoundException("card","card number",cardNumber)
        );
       return CardsMapper.mapToDto(cards,new CardsDto());
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean cardDetailsUpdation(CardsDto cardsDto) {
        Optional<Cards> cards = Optional.ofNullable(cardsRepository.findByCardNumber(cardsDto.getCardNumber()))
                .orElseThrow(
                () -> new ResourceNotFoundException("card", "card number", cardsDto.getCardNumber())
        );
        CardsMapper.mapToCards(cardsDto,cards.get());
        cardsRepository.save(cards.get());
        return true;
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean cardClosureHandler(String mobileNumber) {
        Optional<Cards> cards = Optional.ofNullable(Optional.ofNullable(cardsRepository.findByMobileNumber(mobileNumber)).orElseThrow(
                () -> new ResourceNotFoundException("card", "mobile number", mobileNumber)
        ));
        cardsRepository.deleteByMobileNumber(cards.get().getMobileNumber());
        return true;
    }

    /**
     * @param card
     * @return
     */
    @Override
    public Cards resetCardLimit(CardsDto cardData) {
       Optional<Cards> cardsOptional = cardsRepository.findByCardNumber(cardData.getCardNumber());
       cardsOptional.get().setAvailableLimit(CardsConstants.CARD_LIMIT);
       return cardsRepository.save(cardsOptional.get());
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public CardsDto fetchCardDetailsByMobileNumber(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber);
       return CardsMapper.mapToDto(cards,new CardsDto());
    }

    private Cards newCardIssued(String mobileNumber) {
        Cards card = new Cards();

//        Long newCreditCardNumber = 1000000000L + new Random().nextLong(900000000);
        String newCreditCardNumber = String.valueOf(1000000000L + new Random().nextLong(900000000));
        long card_CVV_code = 100L+new Random().nextLong(900);

        card.setMobileNumber(mobileNumber);
        card.setTotalLimit(CardsConstants.CARD_LIMIT);
        card.setCardCVV(card_CVV_code);
        card.setCardNumber(newCreditCardNumber);
        card.setCardName(CardsConstants.CARD_NAME2);
        card.setCardIssuerBank(CardsConstants.CARD_ISSUER_BANK1);
        card.setCardExpiryDate(LocalDateTime.now().plusYears(4));
        card.setAvailableLimit(CardsConstants.CARD_LIMIT);
        card.setCardType(CardsConstants.DEBIT_CARD);
        card.setCardStatus(CardsConstants.CARD_STATUS_ACTIVE);
        return card;
    }
}
