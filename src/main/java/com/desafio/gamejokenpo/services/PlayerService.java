package com.desafio.gamejokenpo.services;

import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.model.Player;
import com.desafio.gamejokenpo.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService{
    @Autowired
    private MoveService moveService;
    private static Map<Long, Player> players = new HashMap<Long, Player>();
    private static Long idIndex = 0L;

    public List<Player> findAll(){
        return new ArrayList<Player>(players.values());
    }

    public Player insert(Player player){
        incIndex();
        player.setId(getIdIndex());
        players.put(getIdIndex(), player);
        return player;
    }

    public Player findById(Long id) {
        Optional<Player> obj = Optional.ofNullable(players.get(id));
        return obj.orElseThrow(() -> new ObjectNotFoundException("Player não encontrado"));
    }

    public Player update(Long id, Player player) {
        player.setId(id);
        players.put(id, player);
        return player;
    }

    public void delete(Long id) {
        List<Move> moveList = moveService.findMoveByPlayer(id);
        moveList.forEach(m -> moveService.delete(m.getId()) );
        Player p = findById(id);
        players.remove(p.getId());
    }

    public static void incIndex(){
        idIndex ++;
    }

    public static Long getIdIndex() {
        return idIndex;
    }
}
