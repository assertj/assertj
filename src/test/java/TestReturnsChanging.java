import org.junit.*;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

/**
 * Check report of checking_result of the Junit.
 */

public class TestReturnsChanging {

  static class ObjectUnderTest {
    private Optional<String> optString;

    ObjectUnderTest(Optional<String> o) {
      optString = o;
    }

    Optional<String> getOptString() {
      return optString;
    }
  }

  /**
   * This is to show that return() has been overload successfully.
   * we can compare the result of returns(with description) and as()+returns() and only returns().
   * the result is
   * returns with description :  org.opentest4j.AssertionFailedError: [Just like as().]
   * returns without description :  org.opentest4j.AssertionFailedError:
   * as() + returns():  org.opentest4j.AssertionFailedError: [just like as()]
   */
  @Test
  public void test1_returns_overload(){
    // GIVEN
    ObjectUnderTest out = new ObjectUnderTest(Optional.of("test string"));
    // THEN
    assertThat(out).returns("test string",ObjectUnderTest::getOptString,"Just like as().");
    //assertThat(out).returns("test string",ObjectUnderTest::getOptString);
    //assertThat(out).as("just like as()").returns("test string", ObjectUnderTest::getOptString);
  }

  /**
   * Directly show the function's detail.
   */
  @Test
  public void test2_returnsValue(){
    // GIVEN
    ObjectUnderTest out = new ObjectUnderTest(Optional.of("test string"));
    // THEN
    assertThat(out).returnsValue("test string",ObjectUnderTest::getOptString,"Your function is ");
  }

  /**
   * Allow empty function to be input.
   */
  @Test
  public void test3_returnsEmpty(){
    // GIVEN
    ObjectUnderTest out = new ObjectUnderTest(Optional.of("test string"));
    // THEN
    assertThat(out).returnsEmpty("test string",null,"Your function is ");
  }
}
