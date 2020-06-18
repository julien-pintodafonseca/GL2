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
public class TestReadFloat {
    private final List<String> IMACodeGenPrintExpected = new ArrayList<>();
    private final List<String> IMACodeGenInstExpected = new ArrayList<>();

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        IMACodeGenPrintExpected.add("RFLOAT");
        IMACodeGenPrintExpected.add("BOV read_error_float; Overflow check for previous operation");
        IMACodeGenPrintExpected.add("WFLOAT");
        IMACodeGenInstExpected.add("RFLOAT");
        IMACodeGenInstExpected.add("BOV read_error_float; Overflow check for previous operation");
        IMACodeGenInstExpected.add("LOAD R1, R0");
    }

   
}
