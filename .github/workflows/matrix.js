// The script generates a random subset of valid jdk, os, timezone, and other axes.
// You can preview the results by running "node matrix.js"
// See https://github.com/vlsi/github-actions-random-matrix
let {MatrixBuilder} = require('./matrix_builder');
const matrix = new MatrixBuilder();

// Some of the filter conditions might become unsatisfiable, and by default
// the matrix would ignore that.
// For instance, SCRAM requires PostgreSQL 10+, so if you ask
// matrix.generateRow({pg_version: '9.0'}), then it won't generate a row
// That behaviour is useful for PR testing. For instance, if you want to test only SCRAM=yes
// cases, then just comment out "value: no" in SCRAM axis, and the matrix would yield the matching
// parameters.
// However, if you add new testing parameters, you might want un-comment the following line
// to notice if you accidentally introduce unsatisfiable conditions.
// matrix.failOnUnsatisfiableFilters(true);

matrix.addAxis({
  name: 'java_distribution',
  values: [
    'zulu',
    'temurin',
    'liberica',
    'microsoft',
    'oracle',
  ]
});

// TODO: support different JITs (see https://github.com/actions/setup-java/issues/279)
matrix.addAxis({name: 'jit', title: '', values: ['hotspot']});

// Currently assertj requires Java 17+ for compilation
// In future we'll add one more set of Java versions for running tests (e.g. build with Java 20, test with 11)
matrix.addAxis({
  name: 'java_version',
  title: x => 'Java ' + x,
  // Strings allow versions like 18-ea
  values: [
    // allow slightly more room for 17
    '17',
    '17',
    '18',
    '19',
    '20',
  ]
});

// Microsoft Java has no distribution for 8
matrix.exclude({java_distribution: 'microsoft', java_version: '8'});
// Java 18..20 are available in Oracle distribution only
matrix.include(function(row) {
  console.log([row.java_distribution === 'oracle', parseInt(row.java_version) <= 17, !!(row.java_distribution === 'oracle' ^ parseInt(row.java_version) <= 17), row]);
  // a ^ b yields 0 or 1, and we want boolean instead, so !!
  return !!(row.java_distribution === 'oracle' ^ parseInt(row.java_version) <= 17);
});

matrix.addAxis({
  name: 'tz',
  values: [
    'America/New_York',
    'Pacific/Chatham',
    'UTC'
  ]
});

matrix.addAxis({
  name: 'os',
  title: x => x.replace('-latest', ''),
  values: [
    'ubuntu-latest',
    'macos-latest',
    'windows-latest',
  ]
});

// Test cases when Object#hashCode produces the same results
// It allows capturing cases when the code uses hashCode as a unique identifier
matrix.addAxis({
  name: 'hash',
  values: [
    {value: 'regular', title: '', weight: 42},
    {value: 'same', title: 'same hashcode', weight: 1}
  ]
});
matrix.addAxis({
  name: 'locale',
  title: x => x.language + '_' + x.country,
  values: [
    {language: 'de', country: 'DE'},
    {language: 'fr', country: 'FR'},
    {language: 'ru', country: 'RU'},
    {language: 'tr', country: 'TR'},
  ]
});

function isLessThan(pg_version, minVersion){
    return Number(pg_version) < Number(minVersion);
}

matrix.setNamePattern([
    'java_version', 'java_distribution', 'hash', 'os',
    'tz', 'locale',
]);

// TODO: it is known that javac 17 fails to compile AssertJ code with -XX:hashCode=2, so ignore that combo for now
//       It would be great to re-introduce "same hashcode" verification when there are two different Java versions for
//       build and for test purposes.
matrix.exclude({hash: {value: 'same'}, java_version: ['17', '18', '19']})

// The most rare features should be generated the first
// Ensure at least one job with "same" hashcode exists
matrix.generateRow({hash: {value: 'same'}});
//Ensure at least one job with "simple" query_mode exists
matrix.generateRow({query_mode: {value: 'simple'}});
// Ensure there will be at least one job with minimal supported Java
matrix.generateRow({java_version: matrix.axisByName.java_version.values[0]});
// Ensure there will be at least one job with the latest Java
matrix.generateRow({java_version: matrix.axisByName.java_version.values.slice(-1)[0]});
// Ensure at least one Windows and at least one Linux job is present (macOS is almost the same as Linux)
// matrix.generateRow({os: 'windows-latest'});
matrix.generateRow({os: 'ubuntu-latest'});
const include = matrix.generateRows(process.env.MATRIX_JOBS || 5);
if (include.length === 0) {
  throw new Error('Matrix list is empty');
}
include.sort((a, b) => a.name.localeCompare(b.name, undefined, {numeric: true}));
include.forEach(v => {
  let jvmArgs = [];

  let includeTestTags = [];
  // See https://junit.org/junit5/docs/current/user-guide/#running-tests-tag-expressions
  includeTestTags.push('none()'); // untagged tests

  // if (v.slow_tests === 'yes') {
  //     includeTestTags.push('slow_tests');
  // }

  v.includeTestTags = includeTestTags.join(' | ');

  if (v.gss === 'yes' || v.check_anorm_sbt === 'yes') {
      v.deploy_to_maven_local = true
  }
  if (v.hash.value === 'same') {
    jvmArgs.push('-XX:+UnlockExperimentalVMOptions', '-XX:hashCode=2');
  }
  // Gradle does not work in tr_TR locale, so pass locale to test only: https://github.com/gradle/gradle/issues/17361
  jvmArgs.push(`-Duser.country=${v.locale.country}`);
  jvmArgs.push(`-Duser.language=${v.locale.language}`);
  if (v.jit === 'hotspot' && Math.random() > 0.5) {
    // The following options randomize instruction selection in JIT compiler
    // so it might reveal missing synchronization in TestNG code
    v.name += ', stress JIT';
    jvmArgs.push('-XX:+UnlockDiagnosticVMOptions');
    if (v.java_version >= 8) {
      // Randomize instruction scheduling in GCM
      // share/opto/c2_globals.hpp
      jvmArgs.push('-XX:+StressGCM');
      // Randomize instruction scheduling in LCM
      // share/opto/c2_globals.hpp
      jvmArgs.push('-XX:+StressLCM');
    }
    if (v.java_version >= 16) {
      // Randomize worklist traversal in IGVN
      // share/opto/c2_globals.hpp
      jvmArgs.push('-XX:+StressIGVN');
    }
    if (v.java_version >= 17) {
      // Randomize worklist traversal in CCP
      // share/opto/c2_globals.hpp
      jvmArgs.push('-XX:+StressCCP');
    }
  }
  v.testExtraJvmArgs = jvmArgs.join(' ');
  delete v.hash;
});

console.log(include);
console.log('::set-output name=matrix::' + JSON.stringify({include}));
