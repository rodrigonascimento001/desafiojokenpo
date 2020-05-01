package com.desafio.gamejokenpo.resources;

import com.desafio.gamejokenpo.model.Player;
import com.desafio.gamejokenpo.services.PlayerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/players")
public class PlayerResource {

    @Autowired
    private PlayerService service;

    @ApiOperation("Listar todos jogadores")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Player>> list(){
        List<Player> playerList = service.findAll();
        return ResponseEntity.ok().body(playerList);
    }

    @ApiOperation("criar novo jogador")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid  @RequestBody Player player){
        Player playerNew =  service.insert(player);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(playerNew.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation("localizar jogador pelo Id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Player> findById(@PathVariable Long id){
        Player player =  service.findById(id);
        return ResponseEntity.ok().body(player);
    }

    @ApiOperation("Atualizar um jogador")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @PathVariable Long id, @RequestBody Player player){
        service.update(id, player);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("remover um jogador")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
