"""Turn on Paparazzi (JVM screenshots of Compose, no emulator) for the scene previews, in the CI checkout only.

The app's own build never loads Paparazzi: the previews workflow runs this script on its fresh checkout, then
copies the preview test into app/src/test and records the snapshots. Nothing here is committed back.

    python3 tools/previews/enable_paparazzi.py [paparazzi-version]
"""
import sys

VERSION = sys.argv[1] if len(sys.argv) > 1 else "2.0.0-alpha02"


def patch(path, old, new):
    s = open(path, encoding="utf-8").read()
    if new in s:
        return
    if s.count(old) != 1:
        sys.exit(f"{path}: expected exactly one «{old}»")
    open(path, "w", encoding="utf-8").write(s.replace(old, new))


patch("build.gradle.kts",
      'id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false',
      'id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false\n'
      f'    id("app.cash.paparazzi") version "{VERSION}" apply false')
patch("app/build.gradle.kts",
      'id("org.jetbrains.kotlin.plugin.compose")\n}',
      'id("org.jetbrains.kotlin.plugin.compose")\n    id("app.cash.paparazzi")\n}')
patch("app/build.gradle.kts",
      'implementation("androidx.media3:media3-session:1.6.1")',
      'implementation("androidx.media3:media3-session:1.6.1")\n    testImplementation("junit:junit:4.13.2")')
print(f"Paparazzi {VERSION} enabled for this checkout")
