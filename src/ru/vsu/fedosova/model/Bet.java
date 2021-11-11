package ru.vsu.fedosova.model;

public class Bet {

    private BetType betType;
    private Double bet;

    public Bet(BetType betType, Double bet) {
        this.betType = betType;
        this.bet = bet;
    }

    public Bet(BetType betType) {
        this.betType = betType;
    }

    public BetType getBetType() {
        return betType;
    }

    public void setBetType(BetType betType) {
        this.betType = betType;
    }

    public Double getBet() {
        return bet;
    }

    public void setBet(Double bet) {
        this.bet = bet;
    }

    @Override
    public String toString() {
        return betType.toString() + '\n' + bet + '\n';
    }
}
