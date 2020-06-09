#include <fenv.h>
#include <stdio.h>

/*
 * Wrappers C autour de fesetround. Ça serai plus joli d'importer
 * directement fesetround via un pragma import en Ada, mais ça
 * ne permet pas d'importer la valeur des constantes FE_* (constantes
 * pré-processeurs, pas symboles dans libm.so ...). Les wrappers
 * définis ici sont appelables avec juste leur nom, pas besoin de
 * constante.
 */

#define MAKE_WRAPPER(mode) \
	int fesetround_ ## mode() { \
		return fesetround(mode); \
	}

MAKE_WRAPPER(FE_TONEAREST)
MAKE_WRAPPER(FE_UPWARD)
MAKE_WRAPPER(FE_DOWNWARD)
MAKE_WRAPPER(FE_TOWARDZERO)

void float_to_hex_string(float f, char *dest) {
	sprintf(dest, "%a", f);
}
