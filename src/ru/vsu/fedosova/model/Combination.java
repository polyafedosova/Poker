package ru.vsu.fedosova.model;

import java.util.List;

public class Combination implements Comparable<Combination>{

    private final CombinationType type;
    private final List<Card> cardList;

    public Combination(CombinationType type, List<Card> cardList) {
        this.type = type;
        this.cardList = cardList;
    }

    public CombinationType getType() {
        return type;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    @Override
    public int compareTo(Combination c) {
        int result = type.ordinal() - c.getType().ordinal();
        if (result == 0) {
            result = cardList.get(cardList.size() - 1).compareTo(c.getCardList().get(c.getCardList().size() - 1));
        }
        return result;
    }
}
