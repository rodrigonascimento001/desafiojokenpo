package com.desafio.gamejokenpo.resources;

import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.services.GameService;
import com.desafio.gamejokenpo.services.MoveService;
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
@RequestMapping("api/v1/moves")
public class MoveResource {

    @Autowired
    private MoveService service;

    @Autowired
    private GameService gameService;

    @ApiOperation("Listar todas jogadas")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Move>> list(){
        List<Move> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation("Inserir uma jogada")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid  @RequestBody Move move){
        Move moveNew =  service.insert(move);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(moveNew.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation("localizar uma jogada pelo id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Move> findById(@PathVariable Long id){
        Move move =  service.findById(id);
        return ResponseEntity.ok().body(move);
    }

    @ApiOperation("remover uma jogada")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
