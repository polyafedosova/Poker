package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StraightCombinationService implements ICombinationService {
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        unionCard.sort(Comparator.comparing(Card::getRank));
        Collections.reverse(unionCard);
        Card prev = unionCard.get(0);
        int count = 0;
        for(Card c: unionCard) {
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
        return null;
    }
}
