package org.assertj.core.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Static {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#instanceOf(InstanceOfAssertFactory)}.
 *
 * @since 3.13.0
 */
public final class InstanceOfAssertFactories {

  public static final InstanceOfAssertFactory<Predicate, PredicateAssert<Object>> PREDICATE = new InstanceOfAssertFactory<>(Predicate.class,
                                                                                                                            PredicateAssert::new);

  @SuppressWarnings("unchecked")
  public static <T> InstanceOfAssertFactory<Predicate, PredicateAssert<T>> predicate(Class<T> type) {
    return new InstanceOfAssertFactory<>(Predicate.class, predicate -> new PredicateAssert<>(predicate));
  }

  public static final InstanceOfAssertFactory<IntPredicate, IntPredicateAssert> INT_PREDICATE = new InstanceOfAssertFactory<>(IntPredicate.class,
                                                                                                                              IntPredicateAssert::new);

  public static final InstanceOfAssertFactory<LongPredicate, LongPredicateAssert> LONG_PREDICATE = new InstanceOfAssertFactory<>(LongPredicate.class,
                                                                                                                                 LongPredicateAssert::new);

  public static final InstanceOfAssertFactory<DoublePredicate, DoublePredicateAssert> DOUBLE_PREDICATE = new InstanceOfAssertFactory<>(DoublePredicate.class,
                                                                                                                                       DoublePredicateAssert::new);

  public static final InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> COMPLETABLE_FUTURE = new InstanceOfAssertFactory<>(CompletableFuture.class,
                                                                                                                                                     CompletableFutureAssert::new);

  @SuppressWarnings("unchecked")
  public static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class,
                                         completableFuture -> new CompletableFutureAssert<>(completableFuture));
  }

  public static final InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> COMPLETION_STAGE = new InstanceOfAssertFactory<>(CompletionStage.class,
                                                                                                                                                 CompletableFutureAssert::new);

  @SuppressWarnings("unchecked")
  public static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class,
                                         completionStage -> new CompletableFutureAssert<>(completionStage));
  }

  public static final InstanceOfAssertFactory<Integer, IntegerAssert> INTEGER = new InstanceOfAssertFactory<>(Integer.class,
                                                                                                              IntegerAssert::new);

  public static final InstanceOfAssertFactory<String, StringAssert> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                                           StringAssert::new);

  public static final InstanceOfAssertFactory<Class, ClassAssert> CLASS = new InstanceOfAssertFactory<>(Class.class,
                                                                                                        ClassAssert::new);

  public static final InstanceOfAssertFactory<List, ListAssert<Object>> LIST = new InstanceOfAssertFactory<>(List.class,
                                                                                                             ListAssert::new);

  @SuppressWarnings("unchecked")
  public static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, list -> new ListAssert(list));
  }

}
