/* Print espeak-ng's IPA phonemes for each stdin line, clause by clause, exactly as piper-phonemize asks for them.
   Output per input line: one line "<terminator-hex>\t<phonemes>" per clause, then a blank line. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <espeak-ng/speak_lib.h>
int main(int argc, char **argv) {
    if (espeak_Initialize(AUDIO_OUTPUT_SYNCHRONOUS, 0, argv[1], 0) < 0) { fprintf(stderr, "init failed\n"); return 1; }
    if (espeak_SetVoiceByName(argc > 2 ? argv[2] : "ar") != EE_OK) { fprintf(stderr, "voice failed\n"); return 1; }
    static char line[1 << 16];
    while (fgets(line, sizeof line, stdin)) {
        line[strcspn(line, "\n")] = 0;
        const char *p = line;
        int term = 0;
        while (p != NULL) {
            const char *ph = espeak_TextToPhonemesWithTerminator((const void **)&p, espeakCHARS_AUTO, 0x02, &term);
            printf("%x\t%s\n", term, ph ? ph : "");
        }
        printf("\n");
        fflush(stdout);
    }
    return 0;
}
