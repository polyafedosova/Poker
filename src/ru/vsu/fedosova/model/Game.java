package ru.vsu.fedosova.model;

import java.util.*;

public class Game {

    private Map<Player, List<Card>> cardsPlayers = new LinkedHashMap<>();
    private ArrayList<Player> playerLinkedList = new ArrayList<>();
    private Map<StepType, List<Card>> cardsStep = new LinkedHashMap<>();
    private Map<Player, Bet> actionsPlayers = new LinkedHashMap<>();
    Map<Player, Combination> playerCombination = new LinkedHashMap<>();
    private List<Card> deck = new LinkedList<>();
    private Map<Player, Double> pots = new LinkedHashMap<>();

    public Map<Player, List<Card>> getCardsPlayers() {
        return cardsPlayers;
    }

    public void setCardsPlayers(Map<Player, List<Card>> cardsPlayers) {
        this.cardsPlayers = cardsPlayers;
    }

    public ArrayList<Player> getPlayerLinkedList() {
        return playerLinkedList;
    }

    public void setPlayerList(ArrayList<Player> playerLinkedList) {
        this.playerLinkedList = playerLinkedList;
    }

    public Map<StepType, List<Card>> getCardsStep() {
        return cardsStep;
    }

    public void setCardsStep(Map<StepType, List<Card>> cardsStep) {
        this.cardsStep = cardsStep;
    }

    public Map<Player, Bet> getActionsPlayers() {
        return actionsPlayers;
    }

    public void setActionsPlayers(Map<Player, Bet> actionsPlayers) {
        this.actionsPlayers = actionsPlayers;
    }

    public Map<Player, Combination> getPlayerCombination() {
        return playerCombination;
    }

    public void setPlayerCombination(Map<Player, Combination> playerCombination) {
        this.playerCombination = playerCombination;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public Map<Player, Double> getPots() {
        return pots;
    }
}
