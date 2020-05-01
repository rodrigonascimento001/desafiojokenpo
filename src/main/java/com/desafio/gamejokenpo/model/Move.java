package com.desafio.gamejokenpo.model;

import com.desafio.gamejokenpo.enums.MoveEnum;
import javax.validation.constraints.NotNull;

public class Move {
    private Long id;
    @NotNull(message="Preenchimento obrigatório jogador")
    private Player player;
    @NotNull(message="Preenchimento obrigatório jogo")
    private Game game;
    @NotNull(message="Preenchimento obrigatório tipo jogada")
    private MoveEnum valueMove;

    public Move( ) { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public MoveEnum getValueMove() {
        return valueMove;
    }

    public void setValueMove(MoveEnum valueMove) {
        this.valueMove = valueMove;
    }
}
