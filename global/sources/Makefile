IMA=ima-${shell uname -m | sed 's/i.86/iX86/'}-${shell uname -s}
PROGS=$(IMA)
PROGSPATH=${patsubst %,../bin/%, $(PROGS)}

all: $(PROGSPATH)

../bin/$(IMA): Machine_Abstraite/Exec/ima
	cp $< $@

.PHONY: Machine_Abstraite/Exec/ima
Machine_Abstraite/Exec/ima: aflex-ayacc-for-gnat/gnaflex aflex-ayacc-for-gnat/gnayacc
	cd Machine_Abstraite/L_Interp/Src && PATH="$(PWD)/aflex-ayacc-for-gnat:$$PATH" $(MAKE)

aflex-ayacc-for-gnat/%: force
	cd aflex-ayacc-for-gnat/ && $(MAKE) $*

.PHONY: force clean realclean all

clean:
	cd aflex-ayacc-for-gnat/ && $(MAKE) clean
	cd Machine_Abstraite/ && $(MAKE) clean

realclean:
	cd aflex-ayacc-for-gnat/ && $(MAKE) clean
	cd Machine_Abstraite/ && $(MAKE) realclean
	$(RM) $(PROGSPATH)

force:
