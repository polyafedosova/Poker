package ru.vsu.fedosova;

import java.util.List;

public class DeckService {

    public void createDeck(List<Card> deck){
        for(int i = 0x0001F0A1; i < 0x0001F0DF; ) {
            for(Suit suit: Suit.values()){
                for(Rank rank: Rank.values()){
                    if (rank == Rank.TWO) {
                        deck.add(new Card(Character.toChars(i), suit, Rank.ACE));
                        i++;
                    }
                    if (i % 0x10 == 12) {
                        i++;
                    }
                    if (i % 0x10 == 15) {
                        i += 2;
                    }
                    if (rank != Rank.ACE) {
                        deck.add(new Card(Character.toChars(i), suit, rank));
                        i++;
                    }
                }
            }
        }
    }
}
