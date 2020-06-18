package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.StringLiteral;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestStringLiteral {
    private final List<String> IMACodeGenPrintExpectedEmptyString = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedString = new ArrayList<>();

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        IMACodeGenPrintExpectedEmptyString.add("WSTR \"\"");
        IMACodeGenPrintExpectedString.add("WSTR \"hello\"");
    }
   
 
}
