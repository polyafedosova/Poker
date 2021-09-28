package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.Card;
import ru.vsu.fedosova.Rank;

import java.util.*;

public class FullHouseCombinationService implements ICombinationService {
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        List<Card> pair = new ArrayList<>();
        List<Card> three = new ArrayList<>();
        unionCard.sort(Comparator.comparing(Card::getRank));
        Card prev = unionCard.get(0);
        int count = 0;
        for (Card c: unionCard) {
            if(prev.getRank() == c.getRank()) {
                count++;
            } else {
                if(count >=1) {
                    pair.add(prev);
                    pair.add(unionCard.get(unionCard.indexOf(prev) - 1));
                    if (count == 2) {
                        three.add(prev);
                        three.add(unionCard.get(unionCard.indexOf(prev) - 1));
                        three.add(unionCard.get(unionCard.indexOf(prev) - 2));
                    }
                    count = 0;
                }
                prev = c;
            }
        }
        if (three.size() == 6) {
            combination = three;
            int indexMinRank = Math.min(combination.get(0).getRank().ordinal(), combination.get(3).getRank().ordinal());
            for(Card c: combination){
                if (c.getRank().ordinal() == indexMinRank){
                    combination.remove(c);
                    return combination;
                }
            }
            return combination;
        } else if (three.size() == 3) {
            combination = three;
            if (pair.size() >= 2) {
                if (pair.size() >= 4){
                    int indexMinRank = Math.min(pair.get(0).getRank().ordinal(), pair.get(2).getRank().ordinal());
                    pair.removeIf(c -> c.getRank().ordinal() == indexMinRank);
                }
                combination.addAll(pair);
                return combination;
            }
        }
        return null;
    }
}
