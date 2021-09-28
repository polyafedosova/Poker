package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.Card;
import ru.vsu.fedosova.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeOfAKindCombinationService implements ICombinationService {
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        int countPair = 0;
        Map<Rank, Integer> countOfRank = new HashMap<>();
        for(Card c: unionCard) {
            countOfRank.put(c.getRank(),
                    (countOfRank.get(c.getRank()) == null) ? 1 : countOfRank.get(c.getRank()) + 1);
            if (countOfRank.get(c.getRank()) == 3) {
                for(Card c1: unionCard) {
                    if (c1.getRank() == c.getRank()) {
                        combination.add(c1);
                    }
                }
                countPair++;
            }
        }
        if (countPair >= 1) {
            if (countPair == 2) {
                int indexMinRank = Math.min(combination.get(0).getRank().ordinal(), combination.get(3).getRank().ordinal());
                combination.removeIf(c -> c.getRank().ordinal() == indexMinRank);
                return combination;
            }
            return combination;
        } else return null;
    }
}
