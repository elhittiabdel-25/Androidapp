"""espeak-ng's own phonemes for many texts at once: the same library call, voice and data as the app's
voice engine (sherpa-onnx / piper-phonemize). Used to copy espeak's stress marks onto our phonemes.

Needs the small `espeak_phon` program (espeak_phon.c) built against espeak-ng (rhasspy fork, as Piper):
    git clone --depth 1 https://github.com/rhasspy/espeak-ng && cd espeak-ng
    cmake -B build -DUSE_ASYNC=OFF -DBUILD_SHARED_LIBS=ON -DUSE_MBROLA=OFF -DUSE_LIBSONIC=OFF \
          -DUSE_LIBPCAUDIO=OFF -DUSE_KLATT=OFF -DUSE_SPEECHPLAYER=OFF && cmake --build build -j
    gcc -O2 -o espeak_phon tools/tts/espeak_phon.c -Iespeak-ng/src/include -Lespeak-ng/build/src/libespeak-ng \
        -lespeak-ng -Wl,-rpath,$PWD/espeak-ng/build/src/libespeak-ng
and the Kareem voice's espeak-ng-data folder (identical to the one this build makes).
    ESPEAK_PHON=path/to/espeak_phon  TTS_TOOLS=folder with vits-piper-ar_JO-kareem-medium
"""
import os, subprocess, unicodedata

HERE = os.path.dirname(os.path.abspath(__file__))
PHON = os.environ.get("ESPEAK_PHON", "/home/claude/src/phon/phon")
DATA = os.path.join(os.environ.get("TTS_TOOLS", "/home/claude/tts-tools"), "vits-piper-ar_JO-kareem-medium", "espeak-ng-data")


def phonemize(lines):
    """For each text: [(clause terminator, IPA phonemes)], as piper-phonemize gets them."""
    lines = [l.replace("\n", " ") for l in lines]
    r = subprocess.run([PHON, DATA, "ar"], input="\n".join(lines) + "\n", capture_output=True, text=True, check=True)
    out = []
    for block in r.stdout.split("\n\n")[:len(lines)]:
        clauses = []
        for row in block.strip("\n").split("\n"):
            if row:
                term, ph = row.split("\t", 1)
                clauses.append((int(term, 16), unicodedata.normalize("NFD", ph)))
        out.append(clauses)
    return out
