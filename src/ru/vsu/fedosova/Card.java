package ru.vsu.fedosova;

public class Card implements Comparable<Card> {
    private final char[] unicode;
    private final Suit suit;
    private final Rank rank;

    public Card(char[] unicode, Suit suit, Rank rank) {
        this.unicode = unicode;
        this.suit = suit;
        this.rank = rank;
    }

    public Card(Suit suit, Rank rank) {
        this.unicode = null;
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }


    @Override
    public String toString() {
        return "" + String.copyValueOf(unicode) + " " + rank + ' ' + suit;
    }

    @Override
    public int compareTo(Card c) {
        int result = rank.ordinal() - c.getRank().ordinal();
        if (result == 0) {
            result = c.getSuit().ordinal() - suit.ordinal();
        }
        return result;
    }
}
