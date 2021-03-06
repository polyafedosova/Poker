package ru.vsu.fedosova.service;

import ru.vsu.fedosova.model.*;

import java.util.Random;

public class ActionService {

    private final double blind;
    private int countCheck;

    public ActionService(double blind) {
        this.blind = blind;
    }

    public Bet makeBet(Player p, CombinationType c, Bet prevBet, Bet betPrevPlayer, double playerBank, double prevPlayerBank) {
        if (prevBet.getBetType() != BetType.CHECK && prevBet.getBet() >= betPrevPlayer.getBet() && playerBank >= prevPlayerBank) {
            countCheck++;
            return new Bet(BetType.CHECK, 0.0);
        } else {
            countCheck = 0;
            double myBetInMind = (c.ordinal() + 1) / 10.0 * p.getPot();
            if (myBetInMind == p.getPot()) return new Bet(BetType.ALL_IN, myBetInMind);

            if (betPrevPlayer.getBetType() == BetType.CHECK && countCheck == 0) {
                if (myBetInMind < blind) {
                    if (p.getPot() <= blind) return randomDecision(p.getPot());
                    else return new Bet(BetType.BET, blind);
                } else return new Bet(BetType.BET, myBetInMind);
            } else {
                if (playerBank + myBetInMind > prevPlayerBank) {
                    return new Bet(BetType.RAISE, myBetInMind);
                } else {
                    if (prevPlayerBank - playerBank < p.getPot()) return new Bet(BetType.CALL, prevPlayerBank - playerBank);
                    else return randomDecision(p.getPot());
                }
            }
        }
    }

    private Bet randomDecision(double potPlayer) {
        Random random = new Random();
        if (random.nextBoolean()) return new Bet(BetType.ALL_IN, potPlayer);
        else return new Bet(BetType.FOLD, 0.0);
    }

    public int getCountCheck() {
        return countCheck;
    }

    public void setCountCheck(int countCheck) {
        this.countCheck = countCheck;
    }
}
