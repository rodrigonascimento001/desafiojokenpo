package com.desafio.gamejokenpo.services;


import com.desafio.gamejokenpo.enums.StatusEnum;
import com.desafio.gamejokenpo.model.Game;
import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.services.exception.GameException;
import com.desafio.gamejokenpo.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoveService {
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    private static Map<Long, Move> playerMoves = new HashMap<Long, Move>();
    private static Long idIndex = 0L;

    public List<Move> findAll(){
        return new ArrayList<Move>(playerMoves.values());
    }

    public Move insert(Move move){

        incIndex();

        Game game = gameService.findById(move.getGame().getId());
        game.setStatus(StatusEnum.IN_PROGRESS);
        move.setGame(game);

        if(findMoveByPlayerAndGame(move.getPlayer().getId(),move.getGame().getId())){
            throw new GameException("Não é permitido o jogador jogar mais de uma vez");
        }

        move.setPlayer(playerService.findById(move.getPlayer().getId()));
        game.getMoves().add(move);
        gameService.update(move.getGame().getId(),game);
        move.setId(getIdIndex());
        playerMoves.put(getIdIndex(), move);

        return move;
    }

    public Move findById(Long id) {
        Optional<Move> obj = Optional.ofNullable(playerMoves.get(id));
        return obj.orElseThrow(() -> new ObjectNotFoundException("Player Move não encontrado"));
    }

    public List<Move> findMoveByPlayer(Long id) {
        List<Move> list = playerMoves.entrySet().stream()
                .filter(m -> m.getValue().getPlayer().getId().equals(id))
                .map(x -> x.getValue())
                .collect(Collectors.toList());
        return list;
    }

    public boolean findMoveByPlayerAndGame(Long idPlayer , Long idGame) {
        boolean playerExist =  playerMoves.entrySet().stream().filter(m -> m.getValue().getPlayer().getId().equals(idPlayer)
                &&  m.getValue().getGame().getId().equals(idGame)).findAny().isPresent();
        return playerExist;
    }

    public void delete(Long id) {
        Move move = findById(id);
        Game game = gameService.findById(move.getGame().getId());
        game.getMoves().remove(move);
        playerMoves.remove(id);
    }

    public void incIndex(){
        idIndex ++;
    }

    public static Long getIdIndex() {
        return idIndex;
    }
}
