package ch.comem.archidep.floodit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility functions for tests.
 */
public final class TestUtils {

  /**
   * Returns the list of names of all the non-static fields of the specified
   * object, including fields from all parent classes.
   *
   * @param object the object to list fields from
   * @return a list of field names
   */
  public static List<String> getFieldNames(Object object) {
    return getFieldNames(object, field -> true);
  }

  /**
   * Returns the list of names of all the non-static fields of the specified
   * object, including fields from all parent classes, filtered with the
   * specified predicate.
   *
   * @param object the object to list fields from
   * @param predicate fields for which this function returns true will be
   * included in the list
   * @return a list of field names
   */
  public static List<String> getFieldNames(
    Object object,
    Function<Field, Boolean> predicate
  ) {
    Objects.requireNonNull(predicate, "Predicate is required");

    return streamFields(object.getClass())
      .filter(field -> !Modifier.isStatic(field.getModifiers()))
      .filter(field -> predicate.apply(field).booleanValue())
      .map(Field::getName)
      .collect(Collectors.toList());
  }

  private static Stream<Field> streamFields(Class<?> currentClass) {
    if (currentClass == null) {
      return Stream.empty();
    }

    return Stream.concat(
      streamFields(currentClass.getSuperclass()),
      Arrays.stream(currentClass.getDeclaredFields())
    );
  }

  private TestUtils() {}
}
