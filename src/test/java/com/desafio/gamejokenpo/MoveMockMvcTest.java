package com.desafio.gamejokenpo;

import com.desafio.gamejokenpo.enums.MoveEnum;
import com.desafio.gamejokenpo.enums.StatusEnum;
import com.desafio.gamejokenpo.model.Game;
import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.model.Player;
import com.desafio.gamejokenpo.services.MoveService;
import com.desafio.gamejokenpo.services.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MoveMockMvcTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MoveService mockService;
 
	@Autowired
	ApplicationContext context;

	@Before
	public void init() {
		Move move = createMoveMock();
		when(mockService.findById(1L)).thenReturn(move);
	}

	private Move createMoveMock() {
		Move move = new Move();
		Game game = new Game(1L, StatusEnum.FINISHED);
		Player player = new Player(1L,"PlayerTest");
		move.setId(1L);
		move.setPlayer(player);
		move.setGame(game);
		move.setValueMove(MoveEnum.PAPEL);
		return move;
	}

	private Move createMoveMock2() {
		Move move = new Move();
		Game game = new Game(2L, StatusEnum.FINISHED);
		Player player = new Player(2L,"PlayerTest");
		move.setId(2L);
		move.setPlayer(player);
		move.setGame(game);
		move.setValueMove(MoveEnum.TESOURA);
		return move;
	}

	@Test
	public void find_move_Id_OK() throws Exception {

		mockMvc.perform(get("/api/v1/moves/1")).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.valueMove", is(MoveEnum.PAPEL.toString())));

		verify(mockService, times(1)).findById(1L);

	}

	@Test
	public void save_move_CREATED() throws Exception {
		Move moveNew = createMoveMock();
		when(mockService.insert(any(Move.class))).thenReturn(moveNew);

		mockMvc.perform(post("/api/v1/moves")
				.content(om.writeValueAsString(moveNew))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(redirectedUrl("http://localhost/api/v1/moves/1"));

		verify(mockService, times(1)).insert(any(Move.class));

	}

	@Test
	public void find_all_moves_OK() throws Exception {

		List<Move> moves = Arrays.asList(
				createMoveMock(),
				createMoveMock2() );


		when(mockService.findAll()).thenReturn(moves);
 
		mockMvc.perform(get("/api/v1/moves"))
			.andExpect(content()
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].valueMove", is(MoveEnum.PAPEL.toString())))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].valueMove", is(MoveEnum.TESOURA.toString())));
		
		verify(mockService, times(1)).findAll();
	}


	@Test
    public void delete_move_NOCONTENT() throws Exception {
		Move move = createMoveMock();

	   doNothing().doThrow(new RuntimeException()).when(mockService).delete(move.getId());

        mockMvc.perform(delete("/api/v1/moves/1"))
                .andExpect(status().isNoContent());

        verify(mockService, times(1)).delete(1L);
    }

}