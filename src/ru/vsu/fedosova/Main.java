package ru.vsu.fedosova;

import ru.vsu.fedosova.service.GameService;

import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player();
        Player player2 = new Player();
        Queue<Player> players = new LinkedList<>();
        players.add(player1);
        players.add(player2);
        GameService gameService = new GameService();
        Game game = gameService.createGame(players);
    }
}
