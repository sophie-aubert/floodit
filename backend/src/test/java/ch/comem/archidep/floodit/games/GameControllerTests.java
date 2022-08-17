package ch.comem.archidep.floodit.games;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.comem.archidep.floodit.FloodItRoutes;
import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.utils.AbstractControllerTests;
import ch.comem.archidep.floodit.utils.Generate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(GameController.class)
class GameControllerTests extends AbstractControllerTests {

  @MockBean
  private GameService gameService;

  @AfterEach
  void teardown() {
    verifyNoMoreInteractions(this.gameService);
  }

  @Test
  void create_a_game() throws Exception {
    var requestBody = GameFixtures.createGameDto();

    var result = GameFixtures.createdGameDto();
    when(this.gameService.createGame(any(CreateGameDto.class)))
      .thenReturn(result);

    perform(createGameRequest(requestBody))
      .andExpect(status().isCreated())
      .andExpect(content().json(serialize(result)));

    verify(this.gameService, times(1)).createGame(any(CreateGameDto.class));
  }

  @Test
  void list_recent_games() throws Exception {
    var result = Generate.randomListOf(GameFixtures::gameDto, 3);
    when(this.gameService.listRecentGames()).thenReturn(result);

    perform(listRecentGamesRequest())
      .andExpect(status().isOk())
      .andExpect(content().json(serialize(result)));

    verify(this.gameService, times(1)).listRecentGames();
  }

  @Test
  void get_a_game() throws Exception {
    var result = GameFixtures.gameDto();
    when(this.gameService.getGame(result.getId())).thenReturn(result);

    perform(getGameRequest(result.getId()))
      .andExpect(status().isOk())
      .andExpect(content().json(serialize(result)));

    verify(this.gameService, times(1)).getGame(result.getId());
  }

  private MockHttpServletRequestBuilder createGameRequest(
    CreateGameDto requestBody
  ) throws Exception {
    return post(FloodItRoutes.GAMES).content(serialize(requestBody));
  }

  private MockHttpServletRequestBuilder listRecentGamesRequest()
    throws Exception {
    return get(FloodItRoutes.GAMES);
  }

  private MockHttpServletRequestBuilder getGameRequest(long gameId)
    throws Exception {
    return get(FloodItRoutes.GAME, gameId);
  }
}
