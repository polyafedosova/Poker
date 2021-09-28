package ru.vsu.fedosova;

import java.util.*;

public class Game {

    //почему нет new для 1 мапы
    private Map<Player, List<Card>> cardsPlayer;
    private Queue<Player> playerQueue = new LinkedList<>();
    private Map<StepType, List<Card>> cardsStep = new LinkedHashMap<>();
    private List<Card> deck = new LinkedList<>();

    public Map<Player, List<Card>> getCardsPlayer() {
        return cardsPlayer;
    }

    public void setCardsPlayer(Map<Player, List<Card>> cardsPlayer) {
        this.cardsPlayer = cardsPlayer;
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
