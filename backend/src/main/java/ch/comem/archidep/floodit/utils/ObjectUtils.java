package ch.comem.archidep.floodit.utils;

import java.util.function.BiConsumer;
import org.apache.commons.lang3.builder.EqualsBuilder;

public final class ObjectUtils {

  @SuppressWarnings("unchecked")
  public static <T> boolean equals(
    T object,
    Object other,
    BiConsumer<EqualsBuilder, T> build
  ) {
    if (other == object) {
      return true;
    } else if (other == null || other.getClass() != object.getClass()) {
      return false;
    }

    var builder = new EqualsBuilder();
    build.accept(builder, (T) other);
    return builder.isEquals();
  }

  private ObjectUtils() {}
}
