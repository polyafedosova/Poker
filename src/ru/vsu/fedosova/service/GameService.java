package ru.vsu.fedosova.service;

import ru.vsu.fedosova.model.*;
import ru.vsu.fedosova.service.combination.*;

import java.util.*;

public class GameService {

    //todo вывод, банки (all-in)

    private final Map<CombinationType, ICombinationService> combinationServiceMap;

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

    public Game createGame(ArrayList<Player> players) {
        Game game = new Game();
        game.setPlayerList(players);

        DeckService deckService = new DeckService();
        deckService.createDeck(game.getDeck());

        playGame(game);

        return game;
    }

    public void playGame(Game game) {
        //начальная ставка
        double[] blinds = makeBlinds(game);
        if (blinds == null) {
            System.out.println("GAME OVER");
            return;
        }

        //раздача карт
        for(Player p: game.getPlayerLinkedList()) {
            List<Card> cardsForPlayer = new ArrayList<>();
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            cardsForPlayer.add(getFromDeck(game.getDeck()));
            game.getCardsPlayers().put(p, cardsForPlayer);
        }

        for (Player p : game.getPlayerLinkedList()) {
            if (!game.getActionsPlayers().containsKey(p)) {
                game.getActionsPlayers().put(p, new Bet(BetType.CHECK, 0.0));
                game.getPots().put(p, 0.0);
            }
        }

        //step & bet
        ActionService actionService = new ActionService(blinds[1]);
        for (StepType s: StepType.values()) {
            System.out.println(s);
            if(s != StepType.PRE_FLOP) {
                getStepCards(game);
                System.out.println(game.getCardsStep().get(s));
            }
            System.out.println();
            //высчитывает комбинации каждого игрока
            getPlayerCombination(s, game);
            int i = 0;
            //if (s == StepType.PRE_FLOP) i = 2;
            while (actionService.getCountCheck() != game.getPlayerLinkedList().size() & game.getPlayerCombination().size() > 1) {
                if(!game.getActionsPlayers().get(game.getPlayerLinkedList().get(i)).getBetType().equals(BetType.ALL_IN)) {
                    Player player = game.getPlayerLinkedList().get(i);
                    Player prevPlayer = game.getPlayerLinkedList().get((i == 0) ? (game.getPlayerLinkedList().size() - 1) : (i - 1));
                    Bet newBet = actionService.makeBet(player, game.getPlayerCombination().get(player).getType(), game.getActionsPlayers().get(player),
                            game.getActionsPlayers().get(prevPlayer));
                    game.getActionsPlayers().put(player, newBet);
                    player.setPot(player.getPot() - newBet.getBet());
                    game.getPots().put(player, newBet.getBet() + game.getPots().get(player));

                    printStep(game, player, newBet);

                    if (newBet.getBetType() == BetType.FOLD) {
                        game.getPlayerCombination().remove(player);
                        game.getPlayerLinkedList().remove(player);
                    }
                    if (newBet.getBetType() == BetType.ALL_IN) {
                        game.getPots().put(player, newBet.getBet());
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
        Player winner = getWinner(game.getPlayerCombination(), game.getCardsPlayers(), game.getPlayerLinkedList());
        for (Map.Entry<Player, Double> p : game.getPots().entrySet()) {
            if (p.getValue() <= game.getPots().get(winner)) {
                winner.setPot(winner.getPot() + p.getValue());
            } else {
                winner.setPot(game.getPots().get(winner));
                p.getKey().setPot(p.getValue() - game.getPots().get(winner));
            }
        }
        System.out.println("Winner: " + winner.getName());
        System.out.println(winner.getPot());
        System.out.println(game.getPlayerCombination().get(winner).getType());
        System.out.println(game.getPlayerCombination().get(winner).getCardList());
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

    private void getPlayerCombination(StepType s, Game game) {
        for(Player p: game.getPlayerLinkedList()){
            List<Card> unionCard = new LinkedList<>();
            if (s != StepType.PRE_FLOP) {
                unionCard.addAll(game.getCardsStep().get(s));
            }
            unionCard.addAll(game.getCardsPlayers().get(p));
            game.getPlayerCombination().put(p, calcCombination(unionCard));
        }
    }

    public Player getWinner(Map<Player, Combination> finalCombination, Map<Player, List<Card>> cardsPlayers, ArrayList<Player> playerLinkedList) {
        Player winner = playerLinkedList.get(0);
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

    private double[] makeBlinds(Game game) {
        double smallBlind = game.getPlayerLinkedList().get(0).getPot() * 0.05;
        Player first = game.getPlayerLinkedList().get(0);
        first.setPot(game.getPlayerLinkedList().get(0).getPot() - smallBlind);
        Bet small = new Bet(BetType.SMALL_BLIND, smallBlind);
        game.getActionsPlayers().put(first, small);
        game.getPots().put(first, smallBlind);
        printStep(game, first, small);
        double bigBlind = smallBlind * 2;
        for (int i = 1; i < game.getPlayerLinkedList().size(); i++) {
            if (bigBlind < game.getPlayerLinkedList().get(i).getPot()) {
                game.getPlayerLinkedList().get(i).setPot(game.getPlayerLinkedList().get(i).getPot() - bigBlind);
                Bet big = new Bet(BetType.BIG_BLIND, bigBlind);
                game.getActionsPlayers().put(game.getPlayerLinkedList().get(i), big);
                printStep(game, game.getPlayerLinkedList().get(i), big);
                game.getPots().put(game.getPlayerLinkedList().get(i), bigBlind);
                return new double[]{smallBlind, bigBlind};
            } else {
                System.out.println(game.getPlayerLinkedList().get(i) + "can`t call big blind");
                game.getPlayerLinkedList().remove(game.getPlayerLinkedList().get(i));
                i--;
            }
        }
        return null;
    }

    private void printStep(Game game, Player player, Bet newBet) {
        System.out.println(player);
        if (game.getPlayerCombination().containsKey(player))
        System.out.println(game.getPlayerCombination().get(player));
        System.out.println(newBet);
    }
}
