# Makefile pour ima

USES =

OBJECTS = ima test_arrondis test_affichage
EXTRA_OBJ = flottants_c.o fma_c.o
EXTRA_LINK_OPTS = -lm
STATIC = -static

LEXICO = ma_lexico.l
LEXFLAGS =

TESTS = test_arrondis test_arrondis.sh \
	test_affichage.sh test_fma.sh \
	test_floatx.sh test_wfloatx.sh \
	test_overflow.sh test_io.sh \
	test_shl.sh test_shr.sh test_utf8.sh test_clocks.sh

SYNTAX = ma_syntax.y
YACCFLAGS =

GL_GLOBAL = $(PWD)/../..
include $(GL_GLOBAL)/init.make

# Use "gcc" to compile C by default. If "gcc" is not found, try using
# gcc-<version> with <version> being extracted from gnatmake's version.
GCC=$(shell if command -v gcc >/dev/null; then echo gcc; \
	else gnatmake --version | head -n 1 | sed 's/^[^0-9]*\([0-9][0-9]*\.[0-9][0-9]*\).*/gcc-\1/'; \
	fi)

../../Obj/flottants_c.o: flottants_c.c
	@mkdir -p ../../Obj/
	$(GCC) $< -c -o $@

../../Obj/fma_c.o: fma_c.c
	@mkdir -p ../../Obj/
	$(GCC) $< -c -o $@
