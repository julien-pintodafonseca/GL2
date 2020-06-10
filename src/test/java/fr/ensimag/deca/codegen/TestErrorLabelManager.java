package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;

public class TestErrorLabelManager extends TestCase {
    private final DecacFatalError expectedLabelTypeEx = new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_LABEL_MANAGER_UNKNOWN_LABEL_TYPE);
    private final String expectedErrorLabelName1 = "arithmetic_overflow";
    private final String expectedErrorLabelName2 = "read_error";

    @Test
    public void testErrorLabelType() {
        // Tous les labels nécessaires sont présents dans l'enum "ErrorLabelType"
        assertNotNull(ErrorLabelType.valueOf("LB_ARITHMETIC_OVERFLOW"));
        assertNotNull(ErrorLabelType.valueOf("LB_READINT_BAD_ENTRY"));
        assertNotNull(ErrorLabelType.valueOf("LB_READFLOAT_BAD_ENTRY"));
    }

    @Test
    public void testAddErrorAndVerifyLabelType() throws DecacFatalError {
        ErrorLabelManager elm = new ErrorLabelManager();

        // On vérifie addError
        for (ErrorLabelType elt : ErrorLabelType.values()) {
            assertFalse(elm.myLabels.get(elt));
        }

        elm.addError(ErrorLabelType.LB_ARITHMETIC_OVERFLOW);
        elm.addError(ErrorLabelType.LB_READINT_BAD_ENTRY);
        assertTrue(elm.myLabels.get(ErrorLabelType.LB_ARITHMETIC_OVERFLOW));
        assertTrue(elm.myLabels.get(ErrorLabelType.LB_READINT_BAD_ENTRY));
        assertFalse(elm.myLabels.get(ErrorLabelType.LB_READFLOAT_BAD_ENTRY));

        elm.addError(ErrorLabelType.LB_READFLOAT_BAD_ENTRY);
        assertTrue(elm.myLabels.get(ErrorLabelType.LB_READFLOAT_BAD_ENTRY));

        // On vérifie le cas où le errorLabelType est incorrect
        DecacFatalError result = assertThrows(DecacFatalError.class, () -> {elm.addError(null);});
        assertThat(result.getMessage(), is(expectedLabelTypeEx.getMessage()));
    }

    @Test
    public void testErrorLabelName() {
        ErrorLabelManager elm = new ErrorLabelManager();

        String result1 = elm.errorLabelName(ErrorLabelType.LB_ARITHMETIC_OVERFLOW);
        assertThat(result1, is(expectedErrorLabelName1));

        String result2 = elm.errorLabelName(ErrorLabelType.LB_READFLOAT_BAD_ENTRY);
        String result3 = elm.errorLabelName(ErrorLabelType.LB_READINT_BAD_ENTRY);
        assertThat(result2, is(expectedErrorLabelName2));
        assertThat(result3, is(expectedErrorLabelName2));
    }

    @Test
    public void testPrintErrors() {
        // TODO
    }
}
