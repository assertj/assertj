package org.assertj.core.error;

import org.assertj.core.description.Description;

public class AbstractShouldContainExactly extends BasicErrorMessageFactory {

  protected String diffs;

  public AbstractShouldContainExactly(String format, Object... arguments) {
    super(format, arguments);
  }

  @Override
  public String create(Description d) {
    // we append diffs here as we can't add in super constructor call, see why below.
    //
    // case 1 - append diffs to String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:" + diffs, actual, expected);
    // this leads to a MissingFormatArgumentException if diffs contains a format specifier (like %s) because the String
    // will finally be evaluated with String.format
    //
    // case 2 - add as format arg to the String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:"actual, expected, diffs);
    // this is better than case 1 but the diffs String will be quoted before the class to String.format as all String in
    // AssertJ error message. This is not what we want.
    //
    // The solution is to keep diffs as an attribute and append it after String.format has been applied on the error
    // message.
    return super.create(d) + diffs;
  }
}
