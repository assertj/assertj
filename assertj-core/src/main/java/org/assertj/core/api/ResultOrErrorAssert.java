package org.assertj.core.api;

import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;

import java.util.function.Predicate;

import org.assertj.core.error.ShouldBeInstance;
import org.assertj.core.error.ShouldMatch;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.ResultOrError;
import org.assertj.core.presentation.PredicateDescription;

/**
 * Implementation of {@link AbstractObjectAssert} that is aimed to work with {@link ResultOrError} ADT.
 *
 * @author Mikhail Polivakha
 */
public class ResultOrErrorAssert<R, E extends Throwable>
    extends AbstractObjectAssert<ResultOrErrorAssert<R, E>, ResultOrError<R, E>> {

  public ResultOrErrorAssert(ResultOrError<R, E> reResultOrError) {
    super(reResultOrError, ResultOrErrorAssert.class);
  }

  public ResultOrErrorAssert<R, E> doesNotThrowAnyException() {
    doesNotThrowAnyExceptionInternal();
    return myself;
  }

  public ResultOrErrorAssert<R, E> resultsInValueSatisfying(Predicate<R> predicate) {
    doesNotThrowAnyExceptionInternal();
    if (!predicate.test(actual.getResult())) {
      throw Failures.instance().failure(info, ShouldMatch.shouldMatch(actual.getResult(), predicate, PredicateDescription.GIVEN));
    }
    return myself;
  }

  public ResultOrErrorAssert<R, E> raisesThrowableOfType(Class<? extends Throwable> throwableType) {
    if (actual.isSuccessfulResult()) {
      throw Failures.instance().failure(info, ShouldHaveThrown.shouldHaveThrown(throwableType));
    }

    if (!throwableType.isAssignableFrom(actual.getError().getClass())) {
      throw Failures.instance().failure(info, ShouldBeInstance.shouldBeInstance(actual.getError(), throwableType));
    }
    return myself;
  }

  private void doesNotThrowAnyExceptionInternal() {
    if (actual.isError()) {
      throw Failures.instance().failure(info, shouldNotHaveThrown(actual.getError()));
    }
  }
}
