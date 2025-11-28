package org.assertj.tests.core.kotlin.api.list

import org.assertj.core.api.Assertions.assertThat
import org.assertj.tests.core.kotlin.testkit.TolkienCharacter
import org.assertj.tests.core.kotlin.testkit.TolkienCharacter.Race.HOBBIT
import org.junit.jupiter.api.Test

class ListAssert_extracting_Test {

  // https://github.com/assertj/assertj/issues/1499
  @Test
  fun `should compile with List`() {
    val actual: List<TolkienCharacter> = listOf(TolkienCharacter("Frodo", 33, HOBBIT))
    assertThat(actual).extracting(TolkienCharacter::name).contains("Frodo").doesNotContain("Sauron")
    assertThat(actual).extracting { it.name }.contains("Frodo").doesNotContain("Sauron")
  }

}
