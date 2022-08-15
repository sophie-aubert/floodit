package ch.comem.archidep.floodit.utils;

import com.github.javafaker.Faker;
import java.util.Random;

public abstract class AbstractFixtures {

  protected static final Random RANDOM;
  protected static final Faker FAKER;

  static {
    RANDOM = new Random();
    FAKER = new Faker(RANDOM);
  }

  public static final long databaseId() {
    return FAKER.number().numberBetween(1L, Long.MAX_VALUE);
  }
}
