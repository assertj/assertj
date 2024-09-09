package org.assertj.core.internal;

import java.util.Objects;

import org.assertj.core.api.ThrowingCallable;

/**
 * Custom Algebraic Data Type (ADT). Can hold either the result (see {@link ResultOrError#result(Object)}),
 * or the error (see {@link ResultOrError#error(Throwable)})
 *
 * @author Mikhail Polivakha
 */
public interface ResultOrError<R, E extends Throwable> {

  @SuppressWarnings("unchecked")
  static <R, E extends Throwable> ResultOrError<R, E> from(ThrowingCallable<R, E> t) {
    try {
      return ResultOrError.result(t.call());
    } catch (Throwable any) {
      return ResultOrError.error((E) any);
    }
  }

  class Result<R, E extends Throwable> implements ResultOrError<R, E> {

    private final R result;

    public Result(R result) {
      this.result = result;
    }

    @Override
    public boolean isError() {
      return false;
    }

    @Override
    public boolean isSuccessfulResult() {
      return true;
    }

    @Override
    public R getResult() {
      return result;
    }

    @Override
    public E getError() {
      throw new UnsupportedOperationException("GetError on Result");
    }
  }

  class Error<R, E extends Throwable> implements ResultOrError<R, E> {

    private final E throwable;

    public Error(E throwable) {
      Objects.requireNonNull(throwable, "Throwable cannot be null in ResultOnError.Error");
      this.throwable = throwable;
    }

    @Override
    public boolean isError() {
      return true;
    }

    @Override
    public boolean isSuccessfulResult() {
      return false;
    }

    @Override
    public R getResult() {
      throw new UnsupportedOperationException("GetResult on Error");
    }

    @Override
    public E getError() {
      return throwable;
    }
  }

  boolean isError();

  boolean isSuccessfulResult();

  R getResult();

  E getError();

  static <R, E extends Throwable> ResultOrError<R, E> result(R result) {
    return new Result<>(result);
  }

  static <R, E extends Throwable> ResultOrError<R, E> error(E error) {
    return new Error<>(error);
  }
}
