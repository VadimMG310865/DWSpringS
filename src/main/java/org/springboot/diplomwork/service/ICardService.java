package org.springboot.diplomwork.service;

import org.springboot.diplomwork.entity.Card;
import org.springboot.diplomwork.entity.Category;

import java.util.List;

public interface ICardService {
        public Card saveCard(Card card);
        public Boolean existCard(String name);
        public List<Card> getAllCard();
        public List<Card> getAllCardByIdUser(int id_user);
        public Boolean deleteCard(int id);
        public Card getCardById(int id);
    }
