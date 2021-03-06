package ru.vsu.fedosova;

import ru.vsu.fedosova.model.Game;
import ru.vsu.fedosova.model.Player;
import ru.vsu.fedosova.service.GameService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("Polya", 200.0);
        Player player2 = new Player("Elena", 700.0);
        Player player3 = new Player("Dima", 300.0);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        GameService gameService = new GameService();
        Game game = gameService.createGame(players);
    }
}
