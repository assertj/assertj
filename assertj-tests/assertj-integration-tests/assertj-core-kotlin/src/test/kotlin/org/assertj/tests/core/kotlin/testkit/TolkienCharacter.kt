package org.assertj.tests.core.kotlin.testkit

data class TolkienCharacter(
  val name: String,
  val age: Int,
  val race: Race) {

  enum class Race {
    HOBBIT, MAIA, ELF, DWARF, MAN, DRAGON
  }
}
