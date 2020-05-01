package com.desafio.gamejokenpo;

import com.desafio.gamejokenpo.enums.StatusEnum;
import com.desafio.gamejokenpo.model.Game;
import com.desafio.gamejokenpo.model.Player;
import com.desafio.gamejokenpo.services.GameService;
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
public class PlayerMockMvcTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PlayerService mockService;
 
	@Autowired
	ApplicationContext context;

	@Before
	public void init() {
		Player playerNew = new Player(1L,"PlayerTest");
		when(mockService.findById(1L)).thenReturn(playerNew);
	}

	@Test
	public void find_player_Id_OK() throws Exception {

		mockMvc.perform(get("/api/v1/players/1")).andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("PlayerTest")));

		verify(mockService, times(1)).findById(1L);

	}

	@Test
	public void save_player_CREATED() throws Exception {
		Player playerNew = new Player(1L,"PlayerTest");
		when(mockService.insert(any(Player.class))).thenReturn(playerNew);

		mockMvc.perform(post("/api/v1/players")
				.content(om.writeValueAsString(playerNew))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(redirectedUrl("http://localhost/api/v1/players/1"));

		verify(mockService, times(1)).insert(any(Player.class));

	}

	@Test
	public void find_all_player_OK() throws Exception {

		List<Player> players = Arrays.asList(
				new Player(1L,"Player 1 tst"),
				new Player(2L,"Player 2 tst") );


		when(mockService.findAll()).thenReturn(players);
 
		mockMvc.perform(get("/api/v1/players"))
			.andExpect(content()
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].name", is("Player 1 tst")))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].name", is("Player 2 tst")));
		
		verify(mockService, times(1)).findAll();
	}

	@Test
    public void update_player_NoContent() throws Exception {
		Player playerUpdated = new Player(1L,"PlayerTest");

		when(mockService.insert(any(Player.class))).thenReturn(playerUpdated);

        mockMvc.perform(put("/api/v1/players/1")
                .content(om.writeValueAsString(playerUpdated))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)) 
        		.andDo(print())
                .andExpect(status().isNoContent());
	}

	@Test
    public void delete_player_NOCONTENT() throws Exception {
		Player player = new Player(1L,"PlayerTest");

	   doNothing().doThrow(new RuntimeException()).when(mockService).delete(player.getId());

        mockMvc.perform(delete("/api/v1/players/1"))
                .andExpect(status().isNoContent());

        verify(mockService, times(1)).delete(1L);
    }

}