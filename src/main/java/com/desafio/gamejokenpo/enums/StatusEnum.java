package com.desafio.gamejokenpo.enums;

public enum StatusEnum {
    NEW(1,"Novo"),
    IN_PROGRESS(2,"Em andamento"),
    FINISHED(3,"Finalizado");

    private int cod;
    private String description;

    StatusEnum(int cod, String description) {
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

    public static StatusEnum toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(StatusEnum x : StatusEnum.values()) {
            if(cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id Inválido: "+ cod);
    }

    public static StatusEnum toEnum(String move) {
        if(move == null) {
            return null;
        }

        for(StatusEnum x : StatusEnum.values()) {
            if(move.equals(x.getDescription())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Jogada Inválida: "+ move);
    }
}
