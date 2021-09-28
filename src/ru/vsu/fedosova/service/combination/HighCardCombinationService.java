package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.Card;

import java.util.ArrayList;
import java.util.List;

public class HighCardCombinationService implements ICombinationService {

    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        Card high = unionCard.get(0);
        for(Card c: unionCard){
            if (c.compareTo(high) >= 0) {
                high = c;
            }
        }
        combination.add(high);
        return combination;
    }
}
