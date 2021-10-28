package ru.vsu.fedosova.service;

import ru.vsu.fedosova.model.*;
import ru.vsu.fedosova.service.combination.*;

import java.util.*;

public class GameService {

    private final Map<CombinationType, ICombinationService> combinationServiceMap;
    private Game game;

    public GameService() {
        combinationServiceMap = new LinkedHashMap<>();
        combinationServiceMap.put(CombinationType.ROYAL_FLUSH, new RoyalFleshCombinationService());
        combinationServiceMap.put(CombinationType.STRAIGHT_FLUSH, new StraightFlushCombinationService());
        combinationServiceMap.put(CombinationType.FOUR, new FourOfAKindCombinationService());
        combinationServiceMap.put(CombinationType.FULL_HOUSE, new FullHouseCombinationService());
        combinationServiceMap.put(CombinationType.FLUSH, new FlushCombinationService());
        combinationServiceMap.put(CombinationType.STRAIGHT, new StraightCombinationService());
        combinationServiceMap.put(CombinationType.THREE_OF_A_KIND, new ThreeOfAKindCombinationService());
        combinationServiceMap.put(CombinationType.TWO_PAIR, new TwoPairCombinationService());
        combinationServiceMap.put(CombinationType.PAIR, new PairCombinationService());
        combinationServiceMap.put(CombinationType.HIGH_CARD, new HighCardCombinationService());
    }

    public Game createGame(LinkedList<Player> players) {
        Game game = new Game();
        setGame(game);
        game.setPlayerLinkedList(players);

        DeckService deckService = new DeckService();
        deckService.createDeck(game.getDeck());

        playGame();

        return game;
    }

    public void playGame() {
        //начальная ставка
        /*double[] blinds = makeBlinds(game.getPlayerLinkedList());
        for (int i = 0; i < Objects.requireNonNull(blinds).length; i++) {
            game.setBank(game.getBank() + blinds[i]);
        }*/

        //раздача карт
        for(Player p: game.getPlayerLinkedList()) {
            List<Card> cardsForPlayer = new ArrayList<>();
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            game.getCardsPlayers().put(p, cardsForPlayer);
        }

        for (Player p : game.getPlayerLinkedList()) {
            game.getActionsPlayers().put(p, new Bet(BetType.CHECK, 0.0));
        }
        game.getActionsPlayers().put(game.getPlayerLinkedList().get(0), new Bet(BetType.BET, 10.0));

        //step & bet
        ActionService actionService = new ActionService(10.0);
        for (StepType s: StepType.values()) {
            System.out.println(s);
            if(s != StepType.PRE_FLOP) {
                getStepCards();
                System.out.println(game.getCardsStep().get(s));
            }
            System.out.println();
            //высчитывает комбинации каждого игрока
            getPlayerCombination(s);
            int i = 0;
            if (s == StepType.PRE_FLOP) i = 1;
            while (actionService.getCountCheck() != game.getPlayerLinkedList().size() & game.getPlayerCombination().size() > 1) {
                if(!game.getActionsPlayers().get(game.getPlayerLinkedList().get(i)).getBetType().equals(BetType.ALL_IN)) {
                    Player player = game.getPlayerLinkedList().get(i);
                    Player prevPlayer = game.getPlayerLinkedList().get((i == 0) ? (game.getPlayerLinkedList().size() - 1) : (i - 1));
                    Bet newBet = actionService.makeBet(player, game.getPlayerCombination().get(player).getType(), game.getActionsPlayers().get(player),
                            game.getActionsPlayers().get(prevPlayer), s);
                    game.getActionsPlayers().put(player, newBet);
                    player.setPot(player.getPot() - newBet.getBet());
                    game.setBank(game.getBank() + newBet.getBet());

                    System.out.println(player.getName());
                    System.out.println(player.getPot());
                    System.out.println(game.getPlayerCombination().get(player).getType());
                    System.out.println(game.getPlayerCombination().get(player).getCardList());
                    System.out.println(newBet.getBetType());
                    System.out.println(newBet.getBet());
                    System.out.println();

                    if (newBet.getBetType() == BetType.FOLD) {
                        game.getPlayerCombination().remove(player);
                        game.getPlayerLinkedList().remove(player);
                    }
                    if (newBet.getBetType() == BetType.ALL_IN) {
                        game.getSidePots().put(player, newBet.getBet());
                        game.getPlayerLinkedList().remove(player);
                        if (game.getPlayerLinkedList().size() == 1) break;
                    }
                }
                if (i < game.getPlayerLinkedList().size() - 1) {
                    i++;
                } else i = 0;
            }
            actionService.setCountCheck(0);
            if (game.getPlayerLinkedList().size() <= 1) {
                break;
            }
        }
        for (Map.Entry<Player, Bet> p : game.getActionsPlayers().entrySet()) {
            if (p.getValue().getBetType() == BetType.ALL_IN) {
                game.getPlayerLinkedList().add(p.getKey());
            }
        }
        //вскрытие
        Player winner = getWinner(game.getPlayerCombination(), game.getCardsPlayers());
        if (game.getSidePots().containsKey(winner)) {
            winner.setPot(game.getBank() - game.getActionsPlayers().size() * game.getSidePots().get(winner));
            //доделать про all-in
        } else winner.setPot(winner.getPot() + game.getBank());
        game.setBank(0.0);
        System.out.println("Winner: " + winner.getName());
        System.out.println(winner.getPot());
        System.out.println(game.getPlayerCombination().get(winner).getType());
        System.out.println(game.getPlayerCombination().get(winner).getCardList());
        //после добавить опять всех игроков в список, проверив их на банкротство и игра еще раз
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

    private void getPlayerCombination(StepType s) {
        for(Player p: game.getPlayerLinkedList()){
            List<Card> unionCard = new LinkedList<>();
            if (s != StepType.PRE_FLOP) {
                unionCard.addAll(game.getCardsStep().get(s));
            }
            unionCard.addAll(game.getCardsPlayers().get(p));
            game.getPlayerCombination().put(p, calcCombination(unionCard));
        }
    }

    public Player getWinner(Map<Player, Combination> finalCombination, Map<Player, List<Card>> cardsPlayers) {
        Player winner = game.getPlayerLinkedList().getFirst();
        for (Map.Entry<Player, Combination> p: finalCombination.entrySet()){
            if (p.getValue().compareTo(finalCombination.get(winner)) > 0) {
                winner = p.getKey();
            }
            if (p.getValue().compareTo(finalCombination.get(winner)) == 0 && p.getKey().hashCode() != winner.hashCode()) {
                if (calcHighKicker(cardsPlayers.get(p.getKey())).compareTo(calcHighKicker(cardsPlayers.get(winner))) > 0) {
                    winner = p.getKey();
                }
            }
        }
        return winner;
    }

    private void getStepCards() {
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

    private double[] makeBlinds(LinkedList<Player> players) {
        double smallBlind = players.get(0).getPot() * 0.05;
        players.get(0).setPot(players.get(0).getPot() - smallBlind);
        double bigBlind = smallBlind * 2;
        for (int i = 1; i < players.size(); i++) {
            if (bigBlind < players.get(i).getPot()) {
                players.get(i).setPot(players.get(i).getPot() - bigBlind);
                return new double[]{smallBlind, bigBlind};
            }
        }
        return null;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
