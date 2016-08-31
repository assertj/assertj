#!/bin/bash

function usage() {
  echo
  echo "NAME"
  echo "convert-junit-assertions-to-assertj.sh - Converts most of JUnit assertions to AssertJ assertions"
  echo
  echo "It is difficult to convert ALL JUnit assertions (e.g. the ones that are multiline) but it should work for most of them."
  echo
  echo "SYNOPSIS"
  echo "convert-junit-assertions-to-assertj.sh [Pattern]"
  echo
  echo "OPTIONS"
  echo " -h --help    this help"
  echo " [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern"
  echo "              don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely"
  echo
  echo "EXAMPLE"
  echo " convert-junit-assertions-to-assertj.sh \"*IT.java\""
  exit 0
}

if [ "$1" == "-h" -o "$1" == "--help" ] ;
then
 usage
fi

SED_OPTIONS=-i
FILES_PATTERN=${1:-*Test.java}
# echo "SED_OPTIONS = ${SED_OPTIONS}"

echo ''
echo "Converting JUnit assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN"
echo ''
echo ' 1 - Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*0,[[:blank:]]*\(.*\).size())/assertThat(\2).as(\1).isEmpty()/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*0,[[:blank:]]*\(.*\).size())/assertThat(\1).isEmpty()/g' '{}' \;

echo ' 2 - Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size())/assertThat(\3).as(\1).hasSize(\2)/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size())/assertThat(\2).hasSize(\1)/g' '{}' \;

echo ' 3 - Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isCloseTo(\2, within(\4))/g' '{}' \;
# must be done before assertEquals("description", expected, actual) -> assertThat(actual).as("description").isEqualTo(expected) 
# will only replace triplets without double quote to avoid matching : assertEquals("description", expected, actual)
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\))/assertThat(\2).isCloseTo(\1, within(\3))/g' '{}' \;

echo ' 4 - Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isEqualTo(\2)/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g' '{}' \;

echo ' 5 - Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertArrayEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isEqualTo(\2)/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertArrayEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g' '{}' \;

echo ' 6 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNull(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isNull()/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNull([[:blank:]]*\(.*\))/assertThat(\1).isNull()/g' '{}' \;

echo ' 7 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotNull(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isNotNull()/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotNull([[:blank:]]*\(.*\))/assertThat(\1).isNotNull()/g' '{}' \;

echo ' 8 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertTrue(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isTrue()/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertTrue([[:blank:]]*\(.*\))/assertThat(\1).isTrue()/g' '{}' \;

echo ' 9 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertFalse(\(\".*\"\),[[:blank:]]*\(.*\))/assertThat(\2).as(\1).isFalse()/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertFalse([[:blank:]]*\(.*\))/assertThat(\1).isFalse()/g' '{}' \;

echo '10 - Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertSame(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isSameAs(\2)/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isSameAs(\1)/g' '{}' \;

echo '11 - Replacing : assertNotSame(expected, actual) ................ by : assertThat(actual).isNotSameAs(expected)'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotSame(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\3).as(\1).isNotSameAs(\2)/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/assertNotSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isNotSameAs(\1)/g' '{}' \;

echo ''
echo '12 - Replacing JUnit static imports by AssertJ ones, at this point you will probably need to :'
echo '12 --- optimize imports with your IDE to remove unused imports'
echo '12 --- add "import static org.assertj.core.api.Assertions.within;" if you were using JUnit number assertions with deltas'
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.junit.Assert.assertEquals;/import static org.assertj.core.api.Assertions.assertThat;/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.junit.Assert.fail;/import static org.assertj.core.api.Assertions.fail;/g' '{}' \;
find . -name "$FILES_PATTERN" -exec sed ${SED_OPTIONS} 's/import static org.junit.Assert.\*;/import static org.assertj.core.api.Assertions.*;/g' '{}' \;
echo ''
