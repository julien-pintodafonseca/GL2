SHELL = /bin/sh
all:
.PHONY: all

# init.make : Fichier inclus dans les Makefile

# Si GCOV est défini, 
#    - ajout des options -fprofile-arcs -ftest-coverage
#    - Crée les objets dans Obj-gcov/

# Les cibles utilisables sont :
#
# all (le defaut)
#    construit les executables du repertoire courant
#    (utilise les macros OBJECTS et USES, ainsi que les macros LEXICO et
#    SYNTAX du repertoire courant et de ceux de USES pour appeler Aflex
#    et Ayacc le cas echeant)
#
# un_nom_d_executable
#    construit l'executable en question
#
# lex
#    appelle Aflex sur les fichiers de la macro LEXICO
#
# yacc
#    appelle Ayacc sur les fichiers de la macro SYNTAX
#
# clean (ou cleanobj)
#    supprime les .o et .ali de Obj
#
# cleanexec
#    supprime les executables du repertoire courant
#    (utilise la macro OBJECTS)
#
# cleanlex
#    supprime les fichiers produits par Aflex du repertoire courant
#    (utilise la macro LEXICO)
#
# cleanyacc
#    supprime les fichiers produits par Ayacc du repertoire courant
#    (utilise la macro SYNTAX)
#
# realclean
#    <=> clean cleanlex cleanyacc cleanexec
#        dans le repertoire courant et les repertoires utilises
#        suppression des b~... de Obj
#    (utilise la macro USES)
#
# check
#    Lance les executables contenus dans la macro TESTS
#    On rajoute ../../Scripts/ et ../../Exec au début du PATH avant de les lancer.
#
# gcov
#    comme "make all", mais active gcov.
#
# gcovclean
#    pour faire du ménage dans les fichiers générés par Gcov
#
# gcovreinit
#    Effacer les fichiers générés à l'exécution par gcov, mais garder
#    ceux générés à la compilation.
#

.PHONY: gcov
gcov:
	$(AT)${MAKE} GCOV=gcov all

# Old gnatmake don't have a -gnat05 option, but recent gnatmake use
# ada2005 by default => we use -gnat05 when it's there.
GNAT_VERSION := $(shell gnatmake -h 2>&1 | grep -q gnat05 && printf '%s' -gnat05)

# We can't rely on make -j for parallel builds, because running
# several instances of gnatmake in the same directory is unsafe (they
# could try generating the same .o/.ali files in parallel), so we
# detect the presence of -j in flags and pass it to gnatmake instead.
PARALLEL_BUILD := $(shell printf '%s' "$$MAKEFLAGS" | grep -q -e '-j\>' && printf '%s' '-j4')

# Coding guidelines usually advise lines shorter than 80 characters.
# We're a bit more permissive in the warnings: -gnatyM115 complains
# for lines longer than 115 characters. Students usually ignore too
# strict warnings ;-).
COMPILE_OPTS = -gnatf -gnatyM115 -gnatwl -g -gnato -fstack-check $(GNAT_VERSION)
LINK_OPTS = -g -fstack-check

# enable one to locate more precisely the origin of exceptions.
# Must come at the end of command line.
LINK_OPTS_POST = -bargs -E

ifdef GCOV
GCOV_OPTS = -fprofile-arcs -ftest-coverage
BEFORE_COMPILE = echo "Cleaning up before compiling with gcov ..." && $(MAKE) cleanexec && \
		 echo "Finish complilation ..."
AFTER_COMPILE = echo "Creating symbolic links needed for gcov ..."  && $(MAKE) GCOV=$(GCOV) gcov-symlinks
OBJDIR_SUFFIX = -gcov
else
OBJDIR_SUFFIX =
GCOV_OPTS =
BEFORE_COMPILE =
AFTER_COMPILE =
endif

ifdef DEBUG
DEVNULL =
DDEVNULL =
AT =
else
DEVNULL = 1>/dev/null
DDEVNULL = 2>/dev/null
AT = @
endif

# COUR = current directory (above Src/)
COUR = ${shell basename `cd .. ; pwd`}

ROOT = ../..
ifndef OBJ
OBJ = ${ROOT}/Obj${OBJDIR_SUFFIX}
endif
ifndef EXEC
EXEC = ${ROOT}/Exec${EXECDIR_SUFFIX}
endif
ifndef OBJGCOV
OBJGCOV = ${ROOT}/Obj-gcov
endif
ABSROOT = ${shell cd ${ROOT}; pwd}
ABSEXEC = ${shell cd ${EXEC}; pwd}

ifndef STATIC
STATIC =
endif

PROGS = ${OBJECTS:%=${EXEC}/%}
EXTRA_OBJ_FULL=$(EXTRA_OBJ:%=$(OBJ)/%)
# Dependency between the executable and the object, but the rule to
# generate objects in EXTRA_OBJ should be added elsewhere (in the
# user's Makefile).
$(PROGS): $(EXTRA_OBJ_FULL)

GNATMAKE_OPTS = $(PARALLEL_BUILD) $(COMPILE_OPTS) $(LINK_OPTS) $(GCOV_OPTS) $(LINK_OPTS_POST) $(STATIC) -largs $(EXTRA_OBJ_FULL) $(EXTRA_LINK_OPTS)

# COMMON = directory containing utilities provided by teachers
COMMON = ${GL_GLOBAL}/Commun
SRC_COMM = ${COMMON}/Src
OBJ_COMM = ${COMMON}/Obj${OBJDIR_SUFFIX}

LEX_ADB = ${LEXICO:%.l=%.adb}
SYN_ADB = ${SYNTAX:%.y=%.adb}

INCLUDES = -aI. ${USES:%=-aI${ROOT}/%/Src} -aI${SRC_COMM}
OBJPATH = -aO${OBJ_COMM}

# mutants (useless for students)
MUTANTS = ${wildcard *.mut}
MUTANTS_CIBLE = ${MUTANTS:%.mut=%}
%:: %.mut
	-@$(RM) -f $@
	gnatprep -r -c $< $@
# avoid accidental modifications of generated .adb files.
	@chmod ugo-w $@

########################################################################
# Verifications, to be able to provide explicit error messages
########################################################################
.PHONY: check-macros check-lex check-yacc check-uses check-objects
check-macros: check-lex check-yacc check-uses check-objects

check-uses:
	$(AT)for u in ${USES}; do \
		if ! [ -d ${ROOT}/$${u} ]; then \
			echo "ERROR: $${u} used in USES, but directory does not exist"; \
			exit 1; \
		fi; \
	done

check-yacc:
	$(AT)for s in ${SYNTAX}; do \
		${MAKE} --no-print-directory $${s} ${DDEVNULL} ${DEVNULL} ; \
		if ! [ -f ./$${s} ]; then \
			echo "ERROR: $${s} used in SYNTAX, but file $${s} does not exist"; \
			exit 1; \
		fi; \
	done

check-lex:
	$(AT)for l in ${LEXICO}; do \
		${MAKE} --no-print-directory $${l} ${DDEVNULL} ${DEVNULL} ; \
		if ! [ -f ./$${l} ]; then \
			echo "ERROR: $${l} used in LEXICO, but file $${l} does not exist"; \
			exit 1; \
		fi; \
	done

check-objects: ${MUTANTS_CIBLE}
	$(AT)for p in ${OBJECTS}; do \
		if ! [ -f ./$${p}.adb ]; then \
			echo "ERROR: $${p} used in OBJECTS, but file $${p}.adb does not exist"; \
			exit 1; \
		fi; \
	done

########################################################################
# Compilation
########################################################################

all: check-uses force $(MUTANTS_CIBLE)
	$(AT)for use in ${USES} ${COUR}; do \
           cd ${ROOT}/$${use}/Src && ${MAKE} OBJECTS= prepare-ada-src || exit 1; \
        done
	$(AT)${MAKE} --no-print-directory check-objects
	@echo "Ada sources regenerated in ${COUR}, now compiling ..."
	$(AT)${BEFORE_COMPILE}
# Launching several instances of gnatmake in parallel doesn't work, so
# we force sequentiality with a for loop instead of using make's
# standard dependencies. This works if PROGS is empty too.
	$(AT)for prog in ${PROGS}; do \
		${MAKE} --no-print-directory $${prog} || exit 1; \
	done
	$(AT)${AFTER_COMPILE}

.PHONY: gcov-symlinks gcov-symlinks-obj-to-current gcov-symlinks-current-to-others
gcov-symlinks: gcov-symlinks-obj-to-current gcov-symlinks-current-to-others
# For the user to be able to use "gcov *.adb", we want to have *.gcno
# and *.gcda in the source directory. Unfortunately:
#
# - When using gnatmake -D to generate files in ${OBJ} different
#   versions of GCC generate *.gcno in different places (GCC 4.2
#   generates it in the current directory, while GCC 4.5 generates it
#   in the object directory). To be portable accross GCC versions, we
#   let the files be generated GCC puts them, and add a symlink in the
#   source directory for both *.gcda and *.gcno files. See
#   gcov-symlinks-obj-to-current.
#
# - When a directory contains files that are not used in an executable
#   in the same directory (all files in Gencode/), the *.gcno and
#   *.gcda files are created in the directory where the executable is
#   generated (Gencode/Src/*.adb get their *.gcno in Decac/Src/). We
#   also solve this with symbolic links. For example, after
#   Decac/Src/gencode.gcno is created with COUR=Decac, we look for
#   files in $USES looking like gencode.*, and if we find one (we do
#   in Gencode/Src/), we create the symbolic link
#   Gencode/Src/gencode.gcno
#
# We create the *.gcda symlink even if the file does not exist: it
# will be created when the executable will be executed (to collect
# gcov data).

# See above for why this is needed.
# Hack to avoid giving spurious error messages => call "ln" only if
# targets don't exist. symlink can be broken, so [ -f ... ] isn't
# sufficient.
gcov-symlinks-obj-to-current:
	-@for f in ${OBJGCOV}/*.gcno; do				\
	    if [ -f "$$f" ]; then					\
		b=$${f##*/} && 					\
		bb=$${b%.gcno} &&					\
		if ! [ -f "$$bb".gcno -o -h "$$bb".gcno ]; then		\
			ln -vs ${OBJGCOV}/"$$bb".gcno "$$bb".gcno ;	\
		fi &&							\
		if ! [ -f "$$bb".gcda -o -h "$$bb".gcda ]; then		\
			ln -vs ${OBJGCOV}/"$$bb".gcda "$$bb".gcda ;	\
		fi							\
	    fi								\
	done

debug:
	echo !=? "$$(echo ../../Gencode/Src/gen_inst.*)"

# See above for why this is needed.
# little hack to check if a wildcard is expanded:
# [ "$x" != "$(echo $x)" ] is true if $x has been expanded.
gcov-symlinks-current-to-others:
	-@for d in ${USES}; do							\
	    for f in *.gcno ; do						\
		if [ -f "$$f" ]; then						\
	            b=$${f##*/} && 						\
	            bb=$${b%.gcno} &&						\
	            if [ "${ROOT}/$$d/Src/$$bb.*" != "$$(printf '%s' ${ROOT}/$$d/Src/$$bb.*)" ] ; then		\
	                if ! [ -f "${ROOT}/$$d/Src/$$bb".gcno -o -h "${ROOT}/$$d/Src/$$bb".gcno ]; then		\
	                    ln -vs "${ROOT}/${COUR}/Src/$$bb".gcno "${ROOT}/$$d/Src/$$bb".gcno ;	\
	                fi &&							\
	                if ! [ -f "${ROOT}/$$d/Src/$$bb".gcda -o -h "${ROOT}/$$d/Src/$$bb".gcda ]; then		\
	                    ln -vs "${ROOT}/${COUR}/Src/$$bb".gcda "${ROOT}/$$d/Src/$$bb".gcda ;	\
	                fi ;							\
	            fi ;							\
	        fi ;								\
	    done ;								\
	done

# Compilation+link
${EXEC}/%: prepare-ada-src force
	mkdir -p ${EXEC}
	mkdir -p ${OBJ}
	gnatmake $*.adb -D ${OBJ} -o $@ ${INCLUDES} ${OBJPATH} ${GNATMAKE_OPTS}
	$(AT)mv -f b~*.adb b~*.ads b~*.ali b~*.o ${OBJ} ${DDEVNULL} || true

# allow "make some-executable" to build ${EXEC}/some-executable
${OBJECTS}: force
	@${MAKE} all OBJECTS="$@"

.PHONY: prepare-ada-src lex yacc
prepare-ada-src: ${MUTANTS_CIBLE} check-lex check-yacc lex yacc

lex: ${LEX_ADB}

yacc: ${SYN_ADB}

########################################################################
# Cleanup
########################################################################
.PHONY: clean cleanobj realclean clean-lex-yacc-exec gcovclean cleanexec gcovreinit
clean: cleanobj

realclean: cleanobj gcovclean
	$(AT)for use in ${USES} ${COUR} ; do \
           cd ${ROOT}/$${use}/Src && \
           ${MAKE} clean-lex-yacc-exec ; \
        done
# *.ali files shouldn't be generated in Src/ directories, but
# when it happens, it confuses gnatmake. A user usually
# expects "make realclean; make" to remove what could possibly
# confuse the build system ...
	$(RM) *.ali

clean-lex-yacc-exec: cleanlex cleanyacc cleanexec

gcovclean: force
# delete the *.o files whenever we delete the *.gcno. This way,
# make gcovclean; make gcov; regenerates everything.
	$(MAKE) GCOV=gcov cleanobj
	$(RM) *.gcda *.gcno *.gcov ../../Obj-gcov/*.gcda ../../Obj-gcov/*.gcno

# Keep *.gcno files (reset the recorded data only, not the compile-time one)
# Don't delete symbolic links (if [ -f ... ])
gcovreinit: force
	@echo "Deleting gcda and gcov files ..."
	@for f in *.gcda *.gcov ../../Obj-gcov/*.gcda; do \
		if [ -f $$f ]; then $(RM) "$$f"; fi ;\
	done

cleanobj: force
	$(RM) ${OBJ}/*.o ${OBJ}/*.ali ${OBJ}/b~*.ad?

cleanexec: force
	$(RM) ${PROGS}

gcovreport: gcovreport.txt
	@cat $<

# Used by gl-eval-project.sh
# Last line must be the covered line count.
gcovreport.txt: gcov-symlinks force
	$(RM) $@
	$(RM) *.gcov
	gcov *.adb >> $@ 2>&1
	-gcov *.mut >> $@ 2>&1
	@echo >> $@
	@echo "Total number of lines _not_ executed in ${COUR}:" >> $@
	grep '^[ 	]*####*:' *.gcov | wc -l >> $@
	@echo "Total number of lines executed in ${COUR}:" >> $@
	grep '^[ 	]*[0-9][0-9]*:' *.gcov | wc -l >> $@


########################################################################
# Aflex files management
########################################################################

%.adb: %.l
	gnaflex ${LEXFLAGS} $<

# cleanup
.PHONY: cleanlex

ifeq (${LEXICO},)
cleanlex:
	@echo "Nothing to do to for $@"
else
cleanlex:
	$(RM) ${LEXICO:%.l=%.adb} \
		${LEXICO:%.l=%_dfa.adb} ${LEXICO:%.l=%_dfa.ads} \
		${LEXICO:%.l=%_io.adb} ${LEXICO:%.l=%_io.ads}
endif

########################################################################
# Ayacc files management
########################################################################

%.adb: %.y
	gnayacc $< ${YACCFLAGS}

# cleanup
.PHONY: cleanyacc

ifeq (${SYNTAX},)
cleanyacc:
	@echo "Nothing to do to for $@"
else
cleanyacc:
	$(RM) ${SYNTAX:%.y=%.adb} ${SYNTAX:%.y=%_tokens.ads} \
		${SYNTAX:%.y=%_shift_reduce.ads} ${SYNTAX:%.y=%_goto.ads}
endif

########################################################################
# Mini automatic test-suite
########################################################################

# GL_ADDITIONAL_PATH is useless in principle, but can be if we want to test
# a compiler other than the one in ${EXEC}. If set, GL_ADDITIONAL_PATH
# must end with a :.
check:
	$(AT)PATH=$${GL_ADDITIONAL_PATH}${ABSROOT}/Scripts/:${ABSEXEC}/:${GL_GLOBAL}/bin:$$PATH; \
	quelquechose=no ; \
        echec="" ; \
	for t in $(TESTS) ; do \
		echo "Launching test $$t"; \
		if $$t; then \
			echo "Successful termination of $$t"; \
		else \
			echo "Failure for $$t "; \
			echec="$${echec} $$t" ; \
		fi ; \
		quelquechose=yes ; \
	done ; \
	if [ "$$quelquechose" = "yes" ] ; then \
		if [ "$${echec}XXX" = "XXX" ] ; then \
			echo "********************************************************" ; \
			echo "*     All tests passed successfully                    *" ; \
			echo "********************************************************" ; \
		else \
			echo "########################################################" ; \
			echo "###   Failure for the following tests:" ; \
			for t in $$echec ; do \
				echo "###   - $$t" ; \
			done ; \
			echo "########################################################" ; \
			exit 1 ; \
		fi ; \
	else echo "No test to run."; fi

# we use "foo: force" to say "foo is a file, but always try rebuilding it
# and ".PHONY: bar" to say "bar isn't a file".
.PHONY: force
force:

debug-makefile:
	@echo '$$GNATMAKE_OPTS =' "$(GNATMAKE_OPTS)"
	@echo '$$INCLUDES =' "$(INCLUDES)"
	@echo '$$ROOT =' "$(ROOT)"
	@echo '$$OBJ_COMM =' "$(OBJ_COMM)"
	@echo '$$MAKEFLAGS =' $(MAKEFLAGS)
	@echo '$$PARALLEL_BUILD =' $(PARALLEL_BUILD)

## Local Variables:
## mode: makefile
## End:
