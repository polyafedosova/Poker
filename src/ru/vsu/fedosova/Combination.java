package ru.vsu.fedosova;

import java.util.List;

public class Combination {

    private CombinationType type;

    private List<Card> cardList;

    public Combination(CombinationType type, List<Card> cardList) {
        this.type = type;
        this.cardList = cardList;
    }

    public CombinationType getType() {
        return type;
    }

    public void getCardList() {
        for(Card c: cardList){
            System.out.println(c.getSuit() + " " + c.getRank());
        }
    }
}
