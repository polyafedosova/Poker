package ru.vsu.fedosova.service.combination;

import ru.vsu.fedosova.model.Card;
import ru.vsu.fedosova.model.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourOfAKindCombinationService implements ICombinationService {
    @Override
    public List<Card> calc(List<Card> unionCard) {
        List<Card> combination = new ArrayList<>();
        Map<Rank, Integer> countOfRank = new HashMap<>();
        for(Card c: unionCard) {
            countOfRank.put(c.getRank(),
                    (countOfRank.get(c.getRank()) == null) ? 1 : countOfRank.get(c.getRank()) + 1);
            if (countOfRank.get(c.getRank()) == 4) {
                for(Card c1: unionCard) {
                    if (c1.getRank() == c.getRank()) {
                        combination.add(c1);
                    }
                }
                return combination;
            }
        }
        return null;
    }
}
