package com.desafio.gamejokenpo.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Player {
    private Long id;
    @NotEmpty(message="Campo nome Preenchimento obrigatório")
    private String name;

    public Player() { }

    public Player(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(@NotEmpty(message = "Campo nome Preenchimento obrigatório") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
