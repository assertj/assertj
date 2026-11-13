package example.core;

import static org.assertj.core.presentation.BinaryRepresentation.BINARY_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.assertj.core.configuration.Configuration;
import org.assertj.core.presentation.Representation;

class CustomConfiguration extends Configuration {

  private static final SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("yyyy_MM_dd");
  private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy|MM|dd");

  // we keep the default behavior for extractingPrivateFieldsEnabled since it is not overridden

  @Override
  public Representation representation() {
    return BINARY_REPRESENTATION;
  }

  @Override
  public boolean bareNamePropertyExtractionEnabled() {
    return false;
  }

  @Override
  public boolean comparingPrivateFieldsEnabled() {
    return false;
  }

  @Override
  public boolean lenientDateParsingEnabled() {
    return true;
  }

  @Override
  public List<DateFormat> additionalDateFormats() {
    return list(DATE_FORMAT1, DATE_FORMAT2);
  }

  @Override
  public int maxElementsForPrinting() {
    return 2000;
  }

  @Override
  public int maxLengthForSingleLineDescription() {
    return 150;
  }
}