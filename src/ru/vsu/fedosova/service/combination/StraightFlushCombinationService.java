package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.model.Card;
import ru.vsu.fedosova.model.Suit;

import java.util.*;

public class StraightFlushCombinationService implements ICombinationService{
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        unionCard.sort(Comparator.comparing(Card::getRank));
        Collections.reverse(unionCard);
        Suit s = null;
        Map<Suit, Integer> countOfSuit = new HashMap<>();
        for(Card c: unionCard) {
            countOfSuit.put(c.getSuit(),
                    (countOfSuit.get(c.getSuit()) == null) ? 1 : countOfSuit.get(c.getSuit()) + 1);
            if (countOfSuit.get(c.getSuit()) == 5) {
                s = c.getSuit();
            }
        }

        if (Collections.max(countOfSuit.values()) >= 5) {
            for(Card c1: unionCard) {
                if (c1.getSuit() == s) {
                    combination.add(c1);
                }
            }
            List<Card> flush = new ArrayList<>(combination);
            combination.clear();
            Card prev = flush.get(0);
            int count = 0;
            for(Card c: flush) {
                if (prev.getRank().ordinal() - c.getRank().ordinal() == 1) {
                    if(count == 0) {
                        combination.add(prev);
                    }
                    combination.add(c);
                    count++;
                    if (count >= 4) {
                        Collections.reverse(combination);
                        return combination;
                    }
                } else {
                    combination.clear();
                    count = 0;
                }
                prev = c;
            }
        }
        return null;
    }
}
