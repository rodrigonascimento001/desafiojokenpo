package com.desafio.gamejokenpo.model;

import com.desafio.gamejokenpo.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private Long id;
    @NotNull(message="Campo Status o Preenchimento é obrigatório")
    private StatusEnum status;
    @JsonIgnore
    private List<Move> moves = new ArrayList<Move>();

    public Game( @NotNull(message = "Campo Status o Preenchimento é obrigatório") StatusEnum status) {
        this.status = status;
    }

    public Game(Long id, @NotNull(message = "Campo Status o Preenchimento é obrigatório") StatusEnum status) {
        this.id = id;
        this.status = status;
    }

    public Game(){}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }
}
