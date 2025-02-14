package org.example.core;

// tag::user_guide[]
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.TolkienCharacter;

public class CustomRepresentation extends StandardRepresentation { // <1>

  // override needed to hook non predefined type formatting
  @Override
  public String fallbackToStringOf(Object object) { // <2>
    if (object instanceof TolkienCharacter) {
      TolkienCharacter tolkienCharacter = (TolkienCharacter) object;
      return "TolkienCharacter " + tolkienCharacter.getName();
    }
    // fallback to default formatting.
    return super.fallbackToStringOf(object);
  }

  // override a predefined type formatting : String
  @Override
  protected String toStringOf(String str) { // <3>
    return "$" + str + "$";
  }
}
// end::user_guide[]
