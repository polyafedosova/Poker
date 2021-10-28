package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.model.Rank;
import ru.vsu.fedosova.model.Card;

import java.util.*;

public class FullHouseCombinationService implements ICombinationService {
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination;
        List<Card> pair = new ArrayList<>();
        List<Card> three = new ArrayList<>();
        Map<Rank, Integer> countOfRank = new HashMap<>();
        unionCard.sort(Comparator.comparing(Card::getRank));
        for(Card c: unionCard) {
            countOfRank.put(c.getRank(),
                    (countOfRank.get(c.getRank()) == null) ? 1 : countOfRank.get(c.getRank()) + 1);
            if (countOfRank.get(c.getRank()) == 2) {
                for(Card c1: unionCard) {
                    if (c1.getRank() == c.getRank()) {
                        pair.add(c1);
                    }
                }
            }
            if (countOfRank.get(c.getRank()) == 3) {
                for (int i = 0; i < 3; i++) {
                    three.add(pair.remove(pair.size() - 1));
                }
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
