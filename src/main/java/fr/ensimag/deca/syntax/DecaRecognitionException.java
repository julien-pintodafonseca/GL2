package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

import java.io.PrintStream;

/**
 * Base class for exception to be thrown manually within parser
 * actions.
 *
 * @author Equipe GL2
 */
class DecaRecognitionException extends RecognitionException {
    private static final long serialVersionUID = -4104122409998903414L;
    Location location;

    Location getLocation() {
        if (location != null) {
            return location;
        }
        final Token offendingToken = getOffendingToken();
        assert(offendingToken != null);
        return new Location(
                offendingToken.getLine(),
                offendingToken.getCharPositionInLine(),
                offendingToken.getTokenSource().getSourceName()
                );
    }

    public DecaRecognitionException(AbstractDecaLexer recognizer,
                IntStream input) {
        super(recognizer, input, null);
        location = new Location(recognizer.getLine(), recognizer.getCharPositionInLine(), recognizer.getSourceName());
        setOffendingToken(recognizer.getToken());
    }

    public DecaRecognitionException(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, recognizer.getInputStream(), ctx);
        setOffendingToken(ctx.getStart());
    }

    public DecaRecognitionException(DecaParser recognizer, Token offendingToken) {
        super(recognizer, recognizer.getInputStream(), recognizer.getContext());
        setOffendingToken(offendingToken);
    }

    void display(PrintStream err) {
        new LocationException(getMessage(), getLocation()).display(err);
    }
}
