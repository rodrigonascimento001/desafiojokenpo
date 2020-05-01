package com.desafio.gamejokenpo;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.desafio.gamejokenpo.enums.MoveEnum;
import com.desafio.gamejokenpo.enums.StatusEnum;
import com.desafio.gamejokenpo.model.Game;
import com.desafio.gamejokenpo.model.Move;
import com.desafio.gamejokenpo.model.Player;
import com.desafio.gamejokenpo.services.GameService;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GameMockMvcTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GameService mockService;
 
	@Autowired
	ApplicationContext context;

	@Before
	public void init() {
		Game gameNew = new Game(1L, StatusEnum.NEW);
		when(mockService.findById(1L)).thenReturn(gameNew);
	}

	@Test
	public void find_game_Id_OK() throws Exception {

		mockMvc.perform(get("/api/v1/games/1")).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.status", is(StatusEnum.NEW.toString())));

		verify(mockService, times(1)).findById(1L);

	}

	@Test
	public void save_game_CREATED() throws Exception {
		Game gameNew = new Game(1L, StatusEnum.NEW);
		when(mockService.insert(any(Game.class))).thenReturn(gameNew);

		mockMvc.perform(post("/api/v1/games")
				.content(om.writeValueAsString(gameNew))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(redirectedUrl("http://localhost/api/v1/games/1"));

		verify(mockService, times(1)).insert(any(Game.class));

	}

	@Test
	public void find_all_game_OK() throws Exception {

		List<Game> games = Arrays.asList(
				new Game(1L,StatusEnum.NEW),
				new Game(2L,StatusEnum.IN_PROGRESS) );


		when(mockService.findAll()).thenReturn(games);
 
		mockMvc.perform(get("/api/v1/games"))
			.andExpect(content()
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].status", is(StatusEnum.NEW.toString())))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].status", is(StatusEnum.IN_PROGRESS.toString())));
		
		verify(mockService, times(1)).findAll();
	}

	@Test
    public void update_game_NoContent() throws Exception {
		Game gameUpdate = new Game(1L, StatusEnum.FINISHED);

		when(mockService.insert(any(Game.class))).thenReturn(gameUpdate);

        mockMvc.perform(put("/api/v1/games/1")
                .content(om.writeValueAsString(gameUpdate))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)) 
        		.andDo(print())
                .andExpect(status().isNoContent());
	}

	@Test
    public void delete_exame_NOCONTENT() throws Exception {
		Game game = new Game(1L, StatusEnum.FINISHED);
		when(mockService.insert(any(Game.class))).thenReturn(game);

	   doNothing().doThrow(new RuntimeException()).when(mockService).delete(game.getId());

        mockMvc.perform(delete("/api/v1/games/1"))
                .andExpect(status().isNoContent());

        verify(mockService, times(1)).delete(1L);
    }

	@Test
	public void getResultGame_OK() throws Exception {
		Game game = new Game(1L,StatusEnum.NEW);
		Move move = createMoveMock();
		move.setGame(game);
		Move move2 = createMoveMock2();
		move2.setGame(game);

		game.getMoves().addAll(Arrays.asList(move,move2));

		when(mockService.getResultGame(1L)).thenReturn("O vencedor foi: PlayerTest2");
		mockMvc.perform(get("/api/v1/games/1/result")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("O vencedor foi: PlayerTest2"));

		verify(mockService, times(1)).getResultGame(1L);

	}

	private Move createMoveMock() {
		Move move = new Move();
		Player player = new Player(1L,"PlayerTest");
		move.setId(1L);
		move.setPlayer(player);
		move.setValueMove(MoveEnum.PAPEL);
		return move;
	}

	private Move createMoveMock2() {
		Move move = new Move();
		Player player = new Player(2L,"PlayerTest2");
		move.setId(2L);
		move.setPlayer(player);
		move.setValueMove(MoveEnum.TESOURA);
		return move;
	}

}