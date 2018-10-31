#!/bin/bash

function usage() {
  echo
  echo "NAME"
  echo "convert-junit5-assertions-to-assertj-on-osx.sh - Converts most of JUnit 5 assertions to AssertJ assertions"
  echo
  echo "AUTHOR"
  echo "this script is based on JUnit 4 script found at http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html
  The changes in regexps are mostly due to the fact the order arguments changed
  in JUnit 5 (i.e. message moved to the last position). Kudos to whoever wrote the original script!"
  echo
  echo "It is difficult to convert ALL JUnit assertions (e.g. the ones that are multiline) but it should work for most of them."
  echo
  echo "SYNOPSIS"
  echo "convert-junit5-assertions-to-assertj-on-osx.sh [Pattern]"
  echo
  echo "OPTIONS"
  echo " -h --help    this help"
  echo " [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern"
  echo "              don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely"
  echo
  echo "EXAMPLE"
  echo " convert-junit5-assertions-to-assertj-on-osx.sh \"*IT.java\""
  exit 0
}

if [ "$1" == "-h" -o "$1" == "--help" ] ;
then
 usage
fi

SED_OPTIONS=-e
FILES_PATTERN=${1:-*Test.java}

# what file do we want to convert ?
MATCHED_FILES=`find . -name "$FILES_PATTERN"`

echo ''
echo "Converting JUnit 5 assertions to AssertJ assertions in files matching pattern : $FILES_PATTERN"
echo ''
echo ' 1 - Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*0,[[:blank:]]*\(.*\).size(),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isEmpty()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*0,[[:blank:]]*\(.*\).size())/assertThat(\1).isEmpty()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 2 - Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size(),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).hasSize(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([[:digit:]]*\),[[:blank:]]*\(.*\).size())/assertThat(\2).hasSize(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 3 - Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\4).isCloseTo(\1, within(\3))/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
# must be done before assertEquals("description", expected, actual) -> assertThat(actual).as("description").isEqualTo(expected)
# will only replace triplets without double quote to avoid matching : assertEquals("description", expected, actual)
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\),[[:blank:]]*\([^"]*\))/assertThat(\2).isCloseTo(\1, within(\3))/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 4 - Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isEqualTo(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done


echo ' 4B - Replacing : assertNotEquals(expected, actual) ................. by : assertThat(actual).isNotEqualTo(expected)'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNotEquals(\(\".*\"\),[[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).as(\3).isNotEqualTo(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNotEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isNotEqualTo(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 5 - Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertArrayEquals(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isEqualTo(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertArrayEquals([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isEqualTo(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 6 - Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNull(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isNull()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNull([[:blank:]]*\(.*\))/assertThat(\1).isNull()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 7 - Replacing : assertNotNull(actual) .......................... by : assertThat(actual).isNotNull()'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNotNull(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isNotNull()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNotNull([[:blank:]]*\(.*\))/assertThat(\1).isNotNull()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 8 - Replacing : assertTrue(logicalCondition) ................... by : assertThat(logicalCondition).isTrue()'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertTrue(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isTrue()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertTrue([[:blank:]]*\(.*\))/assertThat(\1).isTrue()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ' 9 - Replacing : assertFalse(logicalCondition) .................. by : assertThat(logicalCondition).isFalse()'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertFalse(\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\1).as(\2).isFalse()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertFalse([[:blank:]]*\(.*\))/assertThat(\1).isFalse()/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo '10 - Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertSame(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isSameAs(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isSameAs(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo '11 - Replacing : assertNotSame(expected, actual) ................ by : assertThat(actual).isNotSameAs(expected)'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNotSame(\(.*\),[[:blank:]]*\(.*\),[[:blank:]]*\(\".*\"\))/assertThat(\2).as(\3).isNotSameAs(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/assertNotSame([[:blank:]]*\(.*\),[[:blank:]]*\(.*\))/assertThat(\2).isNotSameAs(\1)/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done

echo ''
echo '12 - Replacing JUnit 5 static imports by AssertJ ones, at this point you will probably need to :'
echo '12 --- optimize imports with your IDE to remove unused imports'
echo '12 --- add "import static org.assertj.core.api.Assertions.within;" if you were using JUnit 5 number assertions with deltas'
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/import static org.junit.jupiter.api.Assertions.fail;/import static org.assertj.core.api.Assertions.fail;/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
for file in ${MATCHED_FILES}; do
   TMP_FILE=`mktemp /tmp/convert.XXXXXXXXXX`
   sed ${SED_OPTIONS} 's/import static org.junit.jupiter.api.Assertions.\*;/import static org.assertj.core.api.Assertions.*;/g' "$file" > $TMP_FILE
   mv "$TMP_FILE" "$file"
done
echo ''
