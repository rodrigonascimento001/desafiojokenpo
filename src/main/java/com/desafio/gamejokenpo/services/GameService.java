package com.desafio.gamejokenpo.services;

import com.desafio.gamejokenpo.enums.MoveEnum;
import com.desafio.gamejokenpo.enums.StatusEnum;
import com.desafio.gamejokenpo.model.Game;
import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.services.exception.GameException;
import com.desafio.gamejokenpo.services.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    private static Map<Long, Game> games = new HashMap<Long, Game>();
    private static Long idIndex = 0L;

    public List<Game> findAll(){
        return new ArrayList<Game>(games.values());
    }

    public Game insert(Game game){
        incIndex();
        if(!game.getStatus().equals(StatusEnum.NEW)){
            throw  new GameException("Status não permitido para novo jogo");
        }
        game.setId(getIdIndex());
        games.put(getIdIndex(), game);
        return game;
    }

    public Game findById(Long id) {
        Optional<Game> obj = Optional.ofNullable(games.get(id));
        return obj.orElseThrow(() -> new ObjectNotFoundException("Game não encontrado"));
    }

    public List<Move> findMovesById(Long id) {
        Optional<Game> obj = Optional.ofNullable(games.get(id));
        return obj.map(x -> x.getMoves()).orElseThrow(() -> new ObjectNotFoundException("Game não encontrado"));
    }

    public Game update(Long id, Game game) {
        game.setId(id);
        games.put(id, game);
        return game;
    }

    public void delete(Long id) {
      Game g = findById(id);
      games.remove(g.getId());
    }

    public static void incIndex(){
        GameService.idIndex ++;
    }

    public static Long getIdIndex() {
        return idIndex;
    }

    public String getResultGame(Long id){

        List<Move> moveList = findMovesById(id);

        if(moveList.stream().count() <= 1){
            throw  new GameException("Não pode ser exibido o resultado sem ao menos 2 jogadas no jogo");
        }
        boolean pedraIsPresent = moveList.stream().filter(m -> m.getValueMove().equals(MoveEnum.PEDRA)).findAny().isPresent();
        boolean tesouraIsPresent = moveList.stream().filter(m -> m.getValueMove().equals(MoveEnum.TESOURA)).findAny().isPresent();
        boolean papelIsPresent = moveList.stream().filter(m -> m.getValueMove().equals(MoveEnum.PAPEL)).findAny().isPresent();
        return  printResultGame(papelIsPresent,tesouraIsPresent,pedraIsPresent,moveList);
    }

    public String printResultGame(boolean papel, boolean tesoura,boolean pedra, List<Move> moveList){
        if(pedra && !papel){
            return buildResultMessage(moveList,MoveEnum.PEDRA);
        }else if(papel && !tesoura){
           return buildResultMessage(moveList,MoveEnum.PAPEL);
        }else if(tesoura && !pedra){
            return buildResultMessage(moveList,MoveEnum.TESOURA);
        }else {
            return "não houve vencedor";
        }
    }

    private String buildResultMessage(List<Move> moveList, MoveEnum filter) {
        String result = "";

        List<Move> movesList = moveList
                .stream()
                .filter(m -> m.getValueMove().equals(filter))
                .collect(Collectors.toList());

        if(movesList.stream().count() > 1){
            result = "Houve um empate, iniciar novo jogo até encontrar o vencedor";
        }else  {
            Optional<Move> moveOptional = movesList.stream().findFirst();
            result = moveOptional.map(m-> "O vencedor foi: "+ m.getPlayer().getName()).orElse("Erro");
        }
        return result;
    }

}
