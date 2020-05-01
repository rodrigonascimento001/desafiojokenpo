package com.desafio.gamejokenpo.resources;

import com.desafio.gamejokenpo.model.Game;
import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.services.GameService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/games")
public class GameResource {

    @Autowired
    private GameService service;

    @ApiOperation("Listar todos Games")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Game>> list(){
        List<Game> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation("Criar novo game")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid  @RequestBody Game game){
        Game gameNew =  service.insert(game);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(gameNew.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation("buscar game pelo id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Game> findById(@PathVariable Long id){
        Game game =  service.findById(id);
        return ResponseEntity.ok().body(game);
    }

    @ApiOperation("Listar jogadas pelo id game")
    @RequestMapping(value = "/{id}/moves", method = RequestMethod.GET)
    public ResponseEntity<List<Move>> findMovesById(@PathVariable Long id){
        List<Move> list =  service.findMovesById(id);
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation("Editar game")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @PathVariable Long id, @RequestBody Game game){
        service.update(id, game);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Remover um game")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Exibir resultado do jogo")
    @RequestMapping(value ="/{id}/result",method = RequestMethod.GET)
    public ResponseEntity<String> getGame(@PathVariable Long id){
        String result = service.getResultGame(id);
        return ResponseEntity.ok().body(result);
    }


}
