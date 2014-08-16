package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;

import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#flatExtracting(Extractor)}</code>
 * 
 * @author Mateusz Haligowski
 */
public class IterableAssert_flatExtracting_test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

  private final Extractor<CartoonCharacter, List<CartoonCharacter>> children = new Extractor<CartoonCharacter, List<CartoonCharacter>>() {
    @Override
    public List<CartoonCharacter> extract(CartoonCharacter input) {
      return input.getChildren();
    }
  };

  @Before
  public void setUp() {
    bart = new CartoonCharacter("Bart Simpson");
    lisa = new CartoonCharacter("Lisa Simpson");
    maggie = new CartoonCharacter("Maggie Simpson");

    homer = new CartoonCharacter("Homer Simpson");
    homer.getChildren().add(bart);
    homer.getChildren().add(lisa);
    homer.getChildren().add(maggie);

    pebbles = new CartoonCharacter("Pebbles Flintstone");
    fred = new CartoonCharacter("Fred Flintstone");
    fred.getChildren().add(pebbles);
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children() {
    assertThat(newArrayList(homer, fred)).flatExtracting(children).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_empty_result_lists() throws Exception {
    assertThat(newArrayList(bart, lisa, maggie)).flatExtracting(children).isEmpty();
  }

  @Test
  public void should_throw_null_pointer_exception_when_extracting_from_null() throws Exception {
    thrown.expect(NullPointerException.class);
    assertThat(newArrayList(homer, null)).flatExtracting(children);
  }
}
