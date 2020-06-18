package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestReadInt {
    private final List<String> IMACodeGenPrintExpected = new ArrayList<>();
    private final List<String> IMACodeGenInstExpected = new ArrayList<>();

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        IMACodeGenPrintExpected.add("RINT");
        IMACodeGenPrintExpected.add("BOV read_error_int; Overflow check for previous operation");
        IMACodeGenPrintExpected.add("WINT");
        IMACodeGenInstExpected.add("RINT");
        IMACodeGenInstExpected.add("BOV read_error_int; Overflow check for previous operation");
        IMACodeGenInstExpected.add("LOAD R1, R0");
    }

    
  
}
