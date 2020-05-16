package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * Portion of IMA assembly code to be dumped verbatim into the
 * generated code.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class InlinePortion extends AbstractLine {
    private final String asmCode;
    
    public InlinePortion(String asmCode) {
        super();
        this.asmCode = asmCode;
    }
    
    @Override
    void display(PrintStream s) {
        s.println(asmCode);
    }

}
