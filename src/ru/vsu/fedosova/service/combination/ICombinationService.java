package ru.vsu.fedosova.service.combination;


import ru.vsu.fedosova.model.Card;

import java.util.List;

public interface ICombinationService {

    List<Card> calc(List<Card> unionCard);
}
