package ch.comem.archidep.floodit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public abstract class AbstractControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  protected ResultActions perform(MockHttpServletRequestBuilder builder)
    throws Exception {
    return this.mockMvc.perform(
        builder.contentType(MediaType.APPLICATION_JSON)
      );
  }

  protected String serialize(Object value) throws JsonProcessingException {
    return this.objectMapper.writeValueAsString(value);
  }
}
