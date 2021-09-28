package ru.vsu.fedosova.service;

import ru.vsu.fedosova.*;
import ru.vsu.fedosova.service.combination.*;

import java.util.*;

public class GameService {

    private Map<CombinationType, ICombinationService> combinationServiceMap;

    public GameService() {
        combinationServiceMap = new LinkedHashMap<>();
        combinationServiceMap.put(CombinationType.ROYAL_FLUSH, new RoyalFleshCombinationService());
        combinationServiceMap.put(CombinationType.STRAIGHT_FLUSH, new StraightFlushCombinationService());
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
        /*for(Card c: game.getDeck()){
            System.out.println(c.toString());
        }*/
        List<Card> combination = new ArrayList<>();
        combination.add(new Card(Suit.DIAMONDS, Rank.SEVEN));
        combination.add(new Card(Suit.CLUBS, Rank.TEN));
        combination.add(new Card(Suit.CLUBS, Rank.JACK));
        combination.add(new Card(Suit.CLUBS, Rank.KING));
        combination.add(new Card(Suit.HEARTS, Rank.SEVEN));
        combination.add(new Card(Suit.CLUBS, Rank.ACE));
        combination.add(new Card(Suit.CLUBS, Rank.QUEEN));
        System.out.println(calcCombination(combination).getType());
        System.out.println(" ");
        calcCombination(combination).getCardList();
        return game;
    }

    public void playGame(Game game) {
        //префлоп ставка

        //раздача карт
        Map<Player, List<Card>> cardsPlayer = new LinkedHashMap<>();
        for(Player p: game.getPlayerQueue()) {
            List<Card> cardsForPlayer = new ArrayList<>();
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            cardsPlayer.put(p, cardsForPlayer);
        }
        game.setCardsPlayer(cardsPlayer);

        //ставка

        //флоп
        List<Card> flopCards = getFlopCards(game.getDeck());
        game.getCardsStep().put(StepType.FLOP, flopCards);

        //ставка

        //терн
        List<Card> turnCards = getFlopCards(game.getDeck());
        turnCards.add(getFromDeck(game.getDeck()));
        game.getCardsStep().put(StepType.TURN, flopCards);

        //ставка

        //ривер
        List<Card> riverCards = getFlopCards(game.getDeck());
        riverCards.add(getFromDeck(game.getDeck()));
        game.getCardsStep().put(StepType.RIVER, flopCards);

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

    public List<Map.Entry<Player, Combination>> getWinner(Game game) {
        return null;
    }

    private List<Card> getFlopCards(List<Card> cards) {
        List<Card> flopCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            flopCards.add(getFromDeck(cards));
        }
        return flopCards;
    }
}
