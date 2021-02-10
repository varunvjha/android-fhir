# Android FHIR SDK (WIP) ![master](https://github.com/google/android-fhir/workflows/CI/badge.svg?branch=master)

Android FHIR SDK is an Android library that manages FHIR resources natively on Android. It also
supports the evaluation of CQL using [cql-engine](https://github.com/DBCG/cql_engine).

**This is a prototype for developers ONLY and is NOT production-ready. Do NOT use in production.**

## Usage

Requires min SDK version 21 (Lollipop).

## Developement

### Requirements

Android Studio 4.0 is required for [Java 8 library desugaring](https://developer.android.com/studio/preview/features#j8-desugar)

### Spotless

We use [Spotless](https://github.com/diffplug/spotless/tree/master/plugin-gradle) to maintain the
Java/Kotlin coding style in the codebase. Run the following command to check the codebase:

```
./gradlew spotlessCheck
```

and run the following command to apply fixes to the violations:

```
./gradlew spotlessApply
```

### License Headers

Spotless maintains the license headers for Java and Kotlin files. Use
[addlicense](https://github.com/google/addlicense) to maintain license headers in other files:

```
addlicense -c "Google LLC" -l apache .
```

## Disclaimer

This is not an officially supported Google product.

This product is not intended to be a medical device.
