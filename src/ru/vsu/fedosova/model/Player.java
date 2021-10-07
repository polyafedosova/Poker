package ru.vsu.fedosova.model;

public class Player {
    private final String name;
    private Integer pot;

    public Player(String name, Integer pot) {
        this.name = name;
        this.pot = pot;
    }

    public String getName() {
        return name;
    }

    public Integer getPot() {
        return pot;
    }

    public void setPot(Integer pot) {
        this.pot = pot;
    }
}
