package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.Card;
import ru.vsu.fedosova.Rank;
import ru.vsu.fedosova.Suit;

import java.util.*;

public class FlushCombinationService implements ICombinationService {
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        unionCard.sort(Comparator.comparing(Card::getRank));
        Collections.reverse(unionCard);
        Map<Suit, Integer> countOfSuit = new HashMap<>();
        for(Card c: unionCard) {
            countOfSuit.put(c.getSuit(),
                    (countOfSuit.get(c.getSuit()) == null) ? 1 : countOfSuit.get(c.getSuit()) + 1);
            if (countOfSuit.get(c.getSuit()) == 5) {
                int count = 0;
                for(Card c1: unionCard) {
                    if (c1.getSuit() == c.getSuit()) {
                        combination.add(c1);
                        count++;
                    }
                    if (count == 5) {
                        return combination;
                    }

                }
            }
        }
        return null;
    }
}
