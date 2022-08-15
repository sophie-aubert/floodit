package ch.comem.archidep.floodit.games;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.comem.archidep.floodit.FloodItRoutes;
import ch.comem.archidep.floodit.games.data.PlayDto;
import ch.comem.archidep.floodit.utils.AbstractControllerTests;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(MoveController.class)
class MoveControllerTests extends AbstractControllerTests {

  @MockBean
  private GameRepository gameRepository;

  @MockBean
  private GameService gameService;

  @AfterEach
  void teardown() {
    verifyNoMoreInteractions(this.gameService);
  }

  @Test
  void play_a_move() throws Exception {
    var requestBody = GameFixtures.playDto();

    var result = GameFixtures.moveDto();
    when(this.gameService.play(any(PlayDto.class))).thenReturn(result);

    var game = GameFixtures.newGame(builder ->
      builder.withId(requestBody.gameId())
    );
    when(this.gameRepository.findById(requestBody.gameId()))
      .thenReturn(Optional.of(game));

    perform(playMoveRequest(requestBody))
      .andExpect(status().isCreated())
      .andExpect(content().json(serialize(result)));

    verify(this.gameService, times(1)).play(any(PlayDto.class));
  }

  private MockHttpServletRequestBuilder playMoveRequest(PlayDto requestBody)
    throws Exception {
    return post(FloodItRoutes.MOVES).content(serialize(requestBody));
  }
}
