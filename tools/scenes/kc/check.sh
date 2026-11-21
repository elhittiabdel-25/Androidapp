#!/bin/bash
# check.sh FILE.kt...: type-check new ui files against the Compose and project stubs
KC=/tmp/claude-0/-home-claude/d708ba9f-c3ae-50d1-bb80-371d1a649380/scratchpad/kc
L=/opt/gradle-8.14.3/lib
CP=$L/kotlin-compiler-embeddable-2.0.21.jar:$L/kotlin-stdlib-2.0.21.jar:$L/kotlin-reflect-2.0.21.jar:$L/kotlin-script-runtime-2.0.21.jar:$L/kotlin-daemon-embeddable-2.0.21.jar:$L/annotations-24.0.1.jar:$L/trove4j-1.0.20200330.jar:$L/kotlinx-coroutines-core-jvm-1.6.4.jar
python3 $KC/gen_stubs.py "$@" > $KC/stubs/Project.kt
rm -rf $KC/out
java -cp "$CP" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler -cp $L/kotlin-stdlib-2.0.21.jar -no-reflect -nowarn -d $KC/out $KC/stubs/*.kt "$@" 2>&1 | grep -v "^Picked up JAVA_TOOL_OPTIONS"
