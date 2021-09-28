package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.Card;
import ru.vsu.fedosova.Rank;
import ru.vsu.fedosova.Suit;

import java.util.*;

public class RoyalFleshCombinationService implements ICombinationService{
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
                for (Card c1 : unionCard) {
                    if (c1.getSuit() == c.getSuit()) {
                        combination.add(c1);
                    }
                }
            }
        }
        Collections.reverse(combination);
        if (combination.get(0).getRank() == Rank.TEN) {
            List<Card> flush = new ArrayList<>(combination);
            combination.clear();
            Card prev = flush.get(0);
            int count = 0;
            for (Card c : flush) {
                if (c.getRank().ordinal() - prev.getRank().ordinal() == 1) {
                    if (count == 0) {
                        combination.add(prev);
                    }
                    combination.add(c);
                    count++;
                    if (count >= 4) {
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
