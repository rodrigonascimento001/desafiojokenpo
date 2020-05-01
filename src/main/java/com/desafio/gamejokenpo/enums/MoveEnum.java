package com.desafio.gamejokenpo.enums;

public enum MoveEnum {
    PEDRA(1,"Pedra"),
    PAPEL(2,"Papel"),
    TESOURA(3,"Tesoura");

    private int cod;
    private String description;

    MoveEnum(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static MoveEnum toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(MoveEnum x : MoveEnum.values()) {
            if(cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id Inválido: "+ cod);
    }

    public static MoveEnum toEnum(String move) {
        if(move == null) {
            return null;
        }

        for(MoveEnum x : MoveEnum.values()) {
            if(move.equals(x.getDescription())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Jogada Inválida: "+ move);
    }
}
