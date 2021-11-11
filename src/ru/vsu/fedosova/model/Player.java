package ru.vsu.fedosova.model;

public class Player {
    private final String name;
    private Double pot;

    public Player(String name, Double pot) {
        this.name = name;
        this.pot = pot;
    }

    public String getName() {
        return name;
    }

    public Double getPot() {
        return pot;
    }

    public void setPot(Double pot) {
        this.pot = pot;
    }

    @Override
    public String toString() {
        if(pot != null)
            return name + '\n' + "Pot: " + pot;
        else return name;
    }
}
