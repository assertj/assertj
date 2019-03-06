#!/bin/bash

function usage() {
  echo
  echo "NAME"
  echo "convert-junit5-assertions-to-assertj.sh - Converts most of JUnit 5 assertions to AssertJ assertions"
  echo
  echo "AUTHOR"
  echo "this script is based on JUnit 4 script found at http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html
  The changes in regexps are mostly due to the fact the order arguments changed
  in JUnit 5 (i.e. message moved to the last position). Kudos to whoever wrote the original script!"
  echo
  echo "It is difficult to convert ALL JUnit assertions (e.g. the ones that are multiline) but it should work for most of them."
  echo
  echo "SYNOPSIS"
  echo "convert-junit5-assertions-to-assertj.sh [Pattern]"
  echo
  echo "OPTIONS"
  echo " -h --help    this help"
  echo " [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern"
  echo "              don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely"
  echo
  echo "EXAMPLE"
  echo " convert-junit5-assertions-to-assertj.sh \"*IT.java\""
  exit 0
}

if [ "$1" == "-h" ] || [ "$1" == "--help" ] ;
then
  usage
fi

# Handle the different ways of running `sed` without generating a backup file based on OS
# - GNU sed (Linux) uses `-i`
# - BSD sed (macOS) uses `-i ''`
SED_OPTIONS=(-i -e)
case "$(uname)" in
  Darwin*) SED_OPTIONS=(-i "" -e)
esac

FILES_PATTERN=${1:-*Test.java}

# what file do we want to convert ?
MATCHED_FILES=$(find . -name "$FILES_PATTERN")

function replace-in-all-files() {
  for file in ${MATCHED_FILES}; do
    sed "${SED_OPTIONS[@]}" "$1" "$file"
  done
}

echo ''
echo "Converting JUnit 5 assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN"
echo ''
echo ' 1 - Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
replace-in-all-files 's/assertEquals([[:blank:]]*0,[[:blank:]]*\(.*\).size(),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isEmpty()/g'
replace-in-all-files 's/assertEquals([[:blank:]]*0,[[:blank:]]*\(.*\).size())/assertThat(\1).isEmpty()/g'

echo ' 2 - Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
replace-in-all-files 's/assertEquals([[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size(),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).hasSize(\1)/g'
replace-in-all-files 's/assertEquals([[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size())/assertThat(\2).hasSize(\1)/g'

echo ' 3 - Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))'
replace-in-all-files 's/assertEquals(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\4).isCloseTo(\1, within(\3))/g'
# must be done before assertEquals("description", expected, actual) -> assertThat(actual).as("description").isEqualTo(expected)
# will only replace triplets without double quote to avoid matching : assertEquals("description", expected, actual)
replace-in-all-files 's/assertEquals([[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\))/assertThat(\2).isCloseTo(\1, within(\3))/g'

echo ' 4 - Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)'
replace-in-all-files 's/assertEquals(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isEqualTo(\1)/g'
replace-in-all-files 's/assertEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g'


echo ' 4B - Replacing : assertNotEquals(expected, actual) ................. by : assertThat(actual).isNotEqualTo(expected)'
replace-in-all-files 's/assertNotEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).as(\3).isNotEqualTo(\1)/g'
replace-in-all-files 's/assertNotEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isNotEqualTo(\1)/g'

echo ' 5 - Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)'
replace-in-all-files 's/assertArrayEquals(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isEqualTo(\1)/g'
replace-in-all-files 's/assertArrayEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g'

echo ' 6 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()'
replace-in-all-files 's/assertNull(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isNull()/g'
replace-in-all-files 's/assertNull([[:blank:]]*\(.*\))/assertThat(\1).isNull()/g'

echo ' 7 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()'
replace-in-all-files 's/assertNotNull(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isNotNull()/g'
replace-in-all-files 's/assertNotNull([[:blank:]]*\(.*\))/assertThat(\1).isNotNull()/g'

echo ' 8 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()'
replace-in-all-files 's/assertTrue(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isTrue()/g'
replace-in-all-files 's/assertTrue([[:blank:]]*\(.*\))/assertThat(\1).isTrue()/g'

echo ' 9 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()'
replace-in-all-files 's/assertFalse(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isFalse()/g'
replace-in-all-files 's/assertFalse([[:blank:]]*\(.*\))/assertThat(\1).isFalse()/g'

echo '10 - Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)'
replace-in-all-files 's/assertSame(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isSameAs(\1)/g'
replace-in-all-files 's/assertSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isSameAs(\1)/g'

echo '11 - Replacing : assertNotSame(expected, actual) ................ by : assertThat(actual).isNotSameAs(expected)'
replace-in-all-files 's/assertNotSame(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isNotSameAs(\1)/g'
replace-in-all-files 's/assertNotSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isNotSameAs(\1)/g'

echo ''
echo '12 - Replacing JUnit 5 static imports by AssertJ ones, at this point you will probably need to :'
echo '12 --- optimize imports with your IDE to remove unused imports'
echo '12 --- add "import static org.assertj.core.api.Assertions.within;" if you were using JUnit 5 number assertions with deltas'
replace-in-all-files 's/import static org.junit.jupiter.api.Assertions.fail;/import static org.assertj.core.api.Assertions.fail;/g'
replace-in-all-files 's/import static org.junit.jupiter.api.Assertions.\*;/import static org.assertj.core.api.Assertions.*;/g'
echo ''
