package org.fest.assertions.error;

import static org.fest.util.SystemProperties.LINE_SEPARATOR;

import java.util.List;

import org.fest.assertions.description.Description;

/**
 * 
 * Base class for text content error.
 * 
 * @author Joel Costigliola
 * 
 */
public class AbstractShouldHaveTextContent extends BasicErrorMessageFactory {

  protected String diffs;

  public AbstractShouldHaveTextContent(String format, Object... arguments) {
    super(format, arguments);
  }

  @Override
  public String create(Description d) {
    // we append diffs here as we can't add in super constructor call, see why below.
    //
    // case 1 - append diffs to String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:" + diffs, actual, expected);
    // this leads to a MissingFormatArgumentException if diffs contains a format specifier (like %s) because the String will
    // finally be evaluated with String.format
    //
    // case 2 - add as format arg to the String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:"actual, expected, diffs);
    // this is better than case 1 but the diffs String will be quoted before the class to String.format as all String in Fest
    // error message. This is not what we want
    //
    // The solution is to keep diffs as an attribute and append it after String.format has been applied on the error message.
    return super.create(d) + diffs;
  }

  protected static String diffsAsString(List<String> diffsList) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String diff : diffsList)
      stringBuilder.append(LINE_SEPARATOR).append(diff);
    return stringBuilder.toString();
  }

}