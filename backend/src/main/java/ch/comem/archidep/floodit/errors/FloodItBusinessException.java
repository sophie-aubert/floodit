package ch.comem.archidep.floodit.errors;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class FloodItBusinessException extends FloodItException {

  private final String code;
  private final Map<String, Serializable> data;

  public FloodItBusinessException(String code, Map<String, Serializable> data) {
    super("Business exception " + code);
    this.code = Objects.requireNonNull(code, "Code is required");
    this.data = Objects.requireNonNull(data, "Data is required");
  }

  public String getCode() {
    return code;
  }

  public Map<String, Serializable> getData() {
    return data;
  }
}
