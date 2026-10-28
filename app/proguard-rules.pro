# Release builds run R8 (see app/build.gradle.kts). Compose, Media3 and AndroidX bring their own keep rules,
# and the app uses no reflection, so nothing else needs keeping. Keep line numbers for crash reports.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
