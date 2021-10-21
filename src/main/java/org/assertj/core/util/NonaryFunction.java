package org.assertj.core.util;

/**
 * <p>A functional interface that describes a function with zero parameters 
 * and no return value. A side-effect function, if you will.</p>
 * 
 * <p>To be used in places where such a function is needed as a method argument, 
 * but you don't want to call it a {@link Runnable}.</p>
 * @author bzt
 *
 */
@FunctionalInterface
public interface NonaryFunction {

  public void apply(); 
  
}
