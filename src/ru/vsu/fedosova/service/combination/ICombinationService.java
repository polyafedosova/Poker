package ru.vsu.fedosova.service.combination;


import ru.vsu.fedosova.Card;

import java.util.List;

public interface ICombinationService {

    List<Card> calc(List<Card> unionCard);
}
