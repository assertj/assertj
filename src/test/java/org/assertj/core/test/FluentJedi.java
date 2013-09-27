package org.assertj.core.test;

import org.assertj.core.api.AbstractIterableAssert;

/**
 * Helper class for testing <code>{@link AbstractIterableAssert#extractingResultOf(String)}</code>.
 * 
 * @author Michał Piotrkowski
 *
 */
public class FluentJedi {

  private final Name name;
  private final int age;
  private final boolean darkSide;
  
  public FluentJedi(Name name, int years, boolean darkSide) {
    this.name = name;
    this.age = years;
    this.darkSide = darkSide;
  }

  public Name name(){
    return name;
  }
  
  public boolean darkSide(){
    return darkSide;
  }
  
  public int age(){
    return age;
  }
  
  @Override
  public String toString() {
    return name.getFirst();
  }
}
