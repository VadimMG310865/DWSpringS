package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Card;
import org.springboot.diplomwork.entity.Category;
import org.springboot.diplomwork.repository.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService implements ICardService{

    @Autowired
    private CardRepo cardRepo;

    @Override
    public Card saveCard(Card card) {
        return cardRepo.save(card);
    }

    @Override
    public Boolean existCard(String num) {
        return cardRepo.existsByNum(num);
    }

    @Override
    public List<Card> getAllCard() {
        return cardRepo.findAll();
    }



    @Override
    public List<Card> getAllCardByIdUser(int id_user) {
        Card cards = cardRepo.findById(id_user).orElse(null);
        return (List<Card>) cards;

    }

    @Override
    public Boolean deleteCard(int id) {
        Card card = cardRepo.findById(id).orElse(null);
        if (card != null) {
            cardRepo.delete(card);
            return true;
        }
        return false;
    }

    @Override
    public Card getCardById(int id) {
        Card card = cardRepo.findById(id).orElse(null);
        return card;
    }
}
