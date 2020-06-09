# Les vieux gnatmake n'ont pas d'option -gnat95, mais les gnatmake
# recents sont en Ada2005 par défaut. On s'adapte ...
GNAT_VERSION := $(shell gnatmake -h 2>&1 | grep -q gnat95 && printf '%s' -gnat95)

GNATMAKE_OPTS = -gnato -fstack-check $(GNAT_VERSION)

all: gnaflex gnayacc

gnaflex: aflex/src/aflex
	cp $< $@

gnayacc: ayacc/src/ayacc
	cp $< $@

aflex/src/aflex: force
	cd aflex/src && gnatmake $(GNATMAKE_OPTS) aflex

ayacc/src/ayacc: force
	cd ayacc/src && gnatmake $(GNATMAKE_OPTS) ayacc

clean:
	-$(RM) aflex/src/*.o aflex/src/*.ali
	-$(RM) ayacc/src/*.o ayacc/src/*.ali
	-$(RM) gnaflex aflex/src/aflex
	-$(RM) gnayacc ayacc/src/ayacc

force:
