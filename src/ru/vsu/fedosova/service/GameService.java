package ru.vsu.fedosova.service;

import ru.vsu.fedosova.*;
import ru.vsu.fedosova.model.Card;
import ru.vsu.fedosova.model.Combination;
import ru.vsu.fedosova.model.Game;
import ru.vsu.fedosova.model.Player;
import ru.vsu.fedosova.service.combination.*;

import java.util.*;

public class GameService {

    private Map<CombinationType, ICombinationService> combinationServiceMap;

    public GameService() {
        combinationServiceMap = new LinkedHashMap<>();
        combinationServiceMap.put(CombinationType.ROYAL_FLUSH, new RoyalFleshCombinationService());
        combinationServiceMap.put(CombinationType.STRAIGHT_FLUSH, new StraightFlushCombinationService());
        combinationServiceMap.put(CombinationType.FOUR, new FourOfAKindCombinationService());
        //combinationServiceMap.put(CombinationType.FULL_HOUSE, new FullHouseCombinationService());
        combinationServiceMap.put(CombinationType.FLUSH, new FlushCombinationService());
        combinationServiceMap.put(CombinationType.STRAIGHT, new StraightCombinationService());
        combinationServiceMap.put(CombinationType.THREE_OF_A_KIND, new ThreeOfAKindCombinationService());
        combinationServiceMap.put(CombinationType.TWO_PAIR, new TwoPairCombinationService());
        combinationServiceMap.put(CombinationType.PAIR, new PairCombinationService());
        combinationServiceMap.put(CombinationType.HIGH_CARD, new HighCardCombinationService());
    }

    public Game createGame(Queue<Player> players) {
        Game game = new Game();
        //fill cards etc.
        game.setPlayerQueue(players);

        DeckService deckService = new DeckService();
        deckService.createDeck(game.getDeck());

        /*List<Card> combination = new ArrayList<>();
        combination.add(new Card(Suit.CLUBS, Rank.KING));
        combination.add(new Card(Suit.CLUBS, Rank.JACK));
        combination.add(new Card(Suit.SPADES, Rank.KING));
        combination.add(new Card(Suit.CLUBS, Rank.QUEEN));
        combination.add(new Card(Suit.CLUBS, Rank.NINE));
        combination.add(new Card(Suit.CLUBS, Rank.ACE));
        combination.add(new Card(Suit.CLUBS, Rank.TEN));

        System.out.println(calcCombination(combination).getType());
        System.out.println(" ");
        for(Card c: calcCombination(combination).getCardList()) {
            System.out.println(c.toString());
        }*/
        playGame(game);
        System.out.println("Карты игроков");
        for(Player p: game.getPlayerQueue()){
            System.out.println(p.getName());
            for(Card c: game.getCardsPlayers().get(p)) {
                System.out.println(c.toString());
            }
            System.out.println(" ");
        }
        /*System.out.println("Карты флоп");
        for (Card c: game.getCardsStep().get(StepType.FLOP)) {
            System.out.println(c.toString());
        }
        System.out.println("Карты терн");
        for (Card c: game.getCardsStep().get(StepType.TURN)) {
            System.out.println(c.toString());
        }
        System.out.println("Карты ривер");
        for (Card c: game.getCardsStep().get(StepType.RIVER)) {
            System.out.println(c.toString());
        }
        System.out.println(" ");*/
        for(Map.Entry<Player, Combination> p: getPlayerCombination(game).entrySet()) {
            System.out.println(p.getKey().getName());
            System.out.println(p.getValue().getType());
            for(Card c: p.getValue().getCardList()) {
                System.out.println(c.toString());
            }
        }
        System.out.println(" ");
        System.out.println("Winner" + getWinner(getPlayerCombination(game), game.getCardsPlayers()).getKey().getName());
        return game;

    }

    public void playGame(Game game) {
        //префлоп ставка

        //раздача карт
        Map<Player, List<Card>> cardsPlayer = new LinkedHashMap<>();
        List<List<Card>> list = new ArrayList<>();
        List<Card> cardsForPolya = new ArrayList<>();
        cardsForPolya.add(new Card(Suit.DIAMONDS, Rank.FIVE));
        cardsForPolya.add(new Card(Suit.CLUBS, Rank.FIVE));
        List<Card> cardsForElena = new ArrayList<>();
        cardsForElena.add(new Card(Suit.DIAMONDS, Rank.JACK));
        cardsForElena.add(new Card(Suit.CLUBS, Rank.KING));
        list.add(cardsForPolya);
        list.add(cardsForElena);

        for(Player p: game.getPlayerQueue()) {
            /*List<Card> cardsForPlayer = new ArrayList<>();
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            cardsForPlayer.add(getFromDeck(game.getDeck()));*/
            for (List<Card> l: list) {
                cardsPlayer.put(p, l);
                list.remove(0);
                break;
            }
        }
        game.setCardsPlayers(cardsPlayer);

        //ставка

        //флоп
        List<Card> river = new ArrayList<>();
        river.add(new Card(Suit.CLUBS, Rank.TEN));
        river.add(new Card(Suit.CLUBS, Rank.JACK));
        river.add(new Card(Suit.CLUBS, Rank.QUEEN));
        river.add(new Card(Suit.CLUBS, Rank.KING));
        river.add(new Card(Suit.CLUBS, Rank.ACE));
        game.getCardsStep().put(StepType.RIVER, river);


        //ставка

        //ривер

        //ставка и вскрытие
    }

    private Card getFromDeck(List<Card> cards) {
        Random rndGenerator = new Random();
        int index = rndGenerator.nextInt(cards.size());
        Card takenCard = cards.get(index);
        cards.remove(index);
        return takenCard;
    }

    private Combination calcCombination(List<Card> list) {
        for(Map.Entry<CombinationType, ICombinationService> entry: combinationServiceMap.entrySet()) {
            List<Card> cardList = entry.getValue().calc(list);
            if (cardList != null && !cardList.isEmpty()) {
                return new Combination(entry.getKey(), cardList);
            }
        }
        return null;
    }

    private Card calcHighKicker(List<Card> cardsPlayer) {
        if (cardsPlayer.get(0).compareTo(cardsPlayer.get(1)) > 0) return cardsPlayer.get(0);
        else return cardsPlayer.get(1);
    }

    private Map<Player, Combination> getPlayerCombination(Game game) {
        Map<Player, Combination> finalCombination = new HashMap<>();
        for(Player p: game.getPlayerQueue()){
            List<Card> unionCard = game.getCardsStep().get(StepType.RIVER);
            unionCard.addAll(game.getCardsPlayers().get(p));
            finalCombination.put(p, calcCombination(unionCard));
        }
        return finalCombination;
    }

    public Map.Entry<Player, Combination> getWinner(Map<Player, Combination> finalCombination, Map<Player, List<Card>> cardsPlayers) {
        Optional<Map.Entry<Player, Combination>> winner = finalCombination.entrySet().stream().findFirst();
        for (Map.Entry<Player, Combination> p: finalCombination.entrySet()){
            if (p.getValue().compareTo(winner.get().getValue()) > 0) {
                winner = Optional.of(p);
            }
            if (p.getValue().compareTo(winner.get().getValue()) == 0 || p.getKey().hashCode() != winner.get().getKey().hashCode()) {
                if (calcHighKicker(cardsPlayers.get(p.getKey())).compareTo(calcHighKicker(cardsPlayers.get(winner.get().getKey()))) > 0) {
                    winner = Optional.of(p);
                }
            }
        }
        return winner.get();
    }

    private void getStepCards(Game game) {
        List<Card> cards = new ArrayList<>();
        if (game.getCardsStep().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                cards.add(getFromDeck(game.getDeck()));
            }
        } else {
            cards.addAll(game.getCardsStep().get(StepType.values()[game.getCardsStep().size()]));
            cards.add(getFromDeck(game.getDeck()));
        }
        game.getCardsStep().put(StepType.values()[game.getCardsStep().size() + 1], cards);
    }
}
