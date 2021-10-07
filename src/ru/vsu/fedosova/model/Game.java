package ru.vsu.fedosova.model;

import ru.vsu.fedosova.StepType;

import java.util.*;

public class Game {

    private Map<Player, List<Card>> cardsPlayers;
    private Queue<Player> playerQueue = new LinkedList<>();
    private Map<StepType, List<Card>> cardsStep = new LinkedHashMap<>();
    private List<Card> deck = new LinkedList<>();

    public Map<Player, List<Card>> getCardsPlayers() {
        return cardsPlayers;
    }

    public void setCardsPlayers(Map<Player, List<Card>> cardsPlayers) {
        this.cardsPlayers = cardsPlayers;
    }

    public Queue<Player> getPlayerQueue() {
        return playerQueue;
    }

    public void setPlayerQueue(Queue<Player> playerQueue) {
        this.playerQueue = playerQueue;
    }

    public Map<StepType, List<Card>> getCardsStep() {
        return cardsStep;
    }

    public void setCardsStep(Map<StepType, List<Card>> cardsStep) {
        this.cardsStep = cardsStep;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }
}
