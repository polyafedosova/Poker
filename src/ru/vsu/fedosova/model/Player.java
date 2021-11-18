package ru.vsu.fedosova.model;

public class Player {
    private final String name;
    private Double pot;

    public Player(String name, Double pot) {
        this.name = name;
        this.pot = Math.ceil(pot * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public String getName() {
        return name;
    }

    public Double getPot() {
        return pot;
    }

    public void setPot(Double pot) {
        this.pot = Math.ceil(pot * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    @Override
    public String toString() {
        if(pot != null)
            return name + '\n' + "Pot: " + pot;
        else return name;
    }
}
