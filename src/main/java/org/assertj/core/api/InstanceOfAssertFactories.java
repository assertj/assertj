package org.assertj.core.api;

/**
 * Static {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#instanceOf(InstanceOfAssertFactory)}.
 *
 * @since 3.13.0
 */
public final class InstanceOfAssertFactories {

  public static final InstanceOfAssertFactory<String, StringAssert> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                                           StringAssert::new);

  public static final InstanceOfAssertFactory<Integer, IntegerAssert> INTEGER = new InstanceOfAssertFactory<>(Integer.class,
                                                                                                              IntegerAssert::new);

  public static final InstanceOfAssertFactory<Class, ClassAssert> CLASS = new InstanceOfAssertFactory<>(Class.class,
                                                                                                        ClassAssert::new);

}
