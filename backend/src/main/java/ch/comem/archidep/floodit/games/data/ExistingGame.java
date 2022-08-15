package ch.comem.archidep.floodit.games.data;

import ch.comem.archidep.floodit.games.GameRepository;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingGame.Validator.class)
public @interface ExistingGame {
  String message() default "Game not found";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  public static class Validator
    implements ConstraintValidator<ExistingGame, Long> {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public boolean isValid(Long gameId, ConstraintValidatorContext context) {
      return gameId == null || this.gameRepository.findById(gameId).isPresent();
    }
  }
}
