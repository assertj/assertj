package org.assertj.core.util.introspection;

import org.assertj.core.test.Person;

public class SuperHero extends Person {

  private final Person trueIdentity;
  private final Person archenemy;

  public SuperHero(String name, Person trueIdentity, Person archenemy) {
    super(name);
    this.trueIdentity = trueIdentity;
    this.archenemy = archenemy;
  }

  public Person archenemy() {
    return archenemy;
  }
  
  public Person getArchenemy() {
	return archenemy;
  }

  public void saveTheDay() {
    // do some superheroic stuff
  }

  @SuppressWarnings("unused")
  private Person trueIdentity() {
    return trueIdentity;
  }
  
  Person getTrueIdentity() {
	return trueIdentity;
  }
}