package org.assertj.core.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Static {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#asInstanceOf(InstanceOfAssertFactory)}.
 *
 * @since 3.13.0
 */
public interface InstanceOfAssertFactories {

  InstanceOfAssertFactory<Predicate, PredicateAssert<Object>> PREDICATE = new InstanceOfAssertFactory<>(Predicate.class,
                                                                                                        PredicateAssert::new);

  @SuppressWarnings("unused") // type parameter needed for type inference
  static <T> InstanceOfAssertFactory<Predicate, PredicateAssert<T>> predicate(Class<T> type) {
    return new InstanceOfAssertFactory<>(Predicate.class, PredicateAssert<T>::new);
  }

  InstanceOfAssertFactory<IntPredicate, IntPredicateAssert> INT_PREDICATE = new InstanceOfAssertFactory<>(IntPredicate.class,
                                                                                                          IntPredicateAssert::new);

  InstanceOfAssertFactory<LongPredicate, LongPredicateAssert> LONG_PREDICATE = new InstanceOfAssertFactory<>(LongPredicate.class,
                                                                                                             LongPredicateAssert::new);

  InstanceOfAssertFactory<DoublePredicate, DoublePredicateAssert> DOUBLE_PREDICATE = new InstanceOfAssertFactory<>(DoublePredicate.class,
                                                                                                                   DoublePredicateAssert::new);

  InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> COMPLETABLE_FUTURE = new InstanceOfAssertFactory<>(CompletableFuture.class,
                                                                                                                                 CompletableFutureAssert::new);

  @SuppressWarnings("unused") // type parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class, CompletableFutureAssert<RESULT>::new);
  }

  InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> COMPLETION_STAGE = new InstanceOfAssertFactory<>(CompletionStage.class,
                                                                                                                             CompletableFutureAssert::new);

  @SuppressWarnings("unused") // type parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class, CompletableFutureAssert<RESULT>::new);
  }

  InstanceOfAssertFactory<Integer, IntegerAssert> INTEGER = new InstanceOfAssertFactory<>(Integer.class,
                                                                                          IntegerAssert::new);

  InstanceOfAssertFactory<String, StringAssert> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                       StringAssert::new);

  InstanceOfAssertFactory<Class, ClassAssert> CLASS = new InstanceOfAssertFactory<>(Class.class,
                                                                                    ClassAssert::new);

  InstanceOfAssertFactory<List, ListAssert<Object>> LIST = new InstanceOfAssertFactory<>(List.class,
                                                                                         ListAssert::new);

  @SuppressWarnings("unused") // type parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, ListAssert<ELEMENT>::new);
  }

}
