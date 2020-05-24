// Generated from /home/xu84u/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OBRACE=1, CBRACE=2, SEMI=3, OPARENT=4, CPARENT=5, IF=6, LT=7, GT=8, LEQ=9, 
		GEQ=10, EXCLAM=11, EQUALS=12, EQEQ=13, NEQ=14, AND=15, OR=16, COMMA=17, 
		ELSE=18, WHILE=19, SPACES=20, PRINT=21, PRINTLN=22, READINT=23, IDENT=24, 
		FLOAT=25, INT=26, STRING=27, COMMENT=28, DUMMY_TOKEN=29;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"OBRACE", "CBRACE", "SEMI", "OPARENT", "CPARENT", "IF", "LT", "GT", "LEQ", 
			"GEQ", "EXCLAM", "EQUALS", "EQEQ", "NEQ", "AND", "OR", "COMMA", "ELSE", 
			"WHILE", "SPACES", "PRINT", "PRINTLN", "READINT", "LETTER", "DIGIT", 
			"POSITIVE_DIGIT", "IDENT", "NUM", "SIGN", "EXP", "DEC", "FLOATDEC", "DIGITHEX", 
			"NUMHEX", "FLOATHEX", "FLOAT", "INT", "STRING_CAR", "STRING", "MONOCOMMENT", 
			"MULTICOMMENT", "COMMENT", "DUMMY_TOKEN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "';'", "'('", "')'", "'if'", "'<'", "'>'", "'<='", 
			"'>='", "'!'", "'='", "'=='", "'!='", "'&&'", "'||'", "','", "'else'", 
			"'while'", null, "'print'", "'println'", "'readInt'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OBRACE", "CBRACE", "SEMI", "OPARENT", "CPARENT", "IF", "LT", "GT", 
			"LEQ", "GEQ", "EXCLAM", "EQUALS", "EQEQ", "NEQ", "AND", "OR", "COMMA", 
			"ELSE", "WHILE", "SPACES", "PRINT", "PRINTLN", "READINT", "IDENT", "FLOAT", 
			"INT", "STRING", "COMMENT", "DUMMY_TOKEN"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 19:
			SPACES_action((RuleContext)_localctx, actionIndex);
			break;
		case 41:
			COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void SPACES_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 skip(); 
			break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 skip(); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\37\u0123\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\5\34\u00af"+
		"\n\34\3\34\3\34\3\34\7\34\u00b4\n\34\f\34\16\34\u00b7\13\34\3\35\6\35"+
		"\u00ba\n\35\r\35\16\35\u00bb\3\36\5\36\u00bf\n\36\3\37\3\37\3\37\3\37"+
		"\3 \3 \3 \3 \3!\3!\3!\3!\5!\u00cd\n!\3!\5!\u00d0\n!\3\"\3\"\5\"\u00d4"+
		"\n\"\3#\6#\u00d7\n#\r#\16#\u00d8\3$\3$\3$\3$\5$\u00df\n$\3$\3$\3$\3$\3"+
		"$\3$\3$\5$\u00e8\n$\3%\3%\5%\u00ec\n%\3&\3&\3&\7&\u00f1\n&\f&\16&\u00f4"+
		"\13&\5&\u00f6\n&\3\'\3\'\3(\3(\3(\3(\3(\3(\7(\u0100\n(\f(\16(\u0103\13"+
		"(\3(\3(\3)\3)\3)\3)\7)\u010b\n)\f)\16)\u010e\13)\3*\3*\3*\3*\7*\u0114"+
		"\n*\f*\16*\u0117\13*\3*\3*\3*\3+\3+\5+\u011e\n+\3+\3+\3,\3,\3\u0115\2"+
		"-\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\2\63\2\65\2\67\329\2;\2=\2"+
		"?\2A\2C\2E\2G\2I\33K\34M\2O\35Q\2S\2U\36W\37\3\2\13\5\2\13\f\17\17\"\""+
		"\4\2C\\c|\4\2&&aa\4\2--//\4\2GGgg\4\2HHhh\4\2RRrr\5\2\f\f$$^^\4\2\f\f"+
		"\17\17\2\u0129\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\2/\3\2\2\2\2\67\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2O\3\2\2\2\2U\3\2\2"+
		"\2\2W\3\2\2\2\3Y\3\2\2\2\5[\3\2\2\2\7]\3\2\2\2\t_\3\2\2\2\13a\3\2\2\2"+
		"\rc\3\2\2\2\17f\3\2\2\2\21h\3\2\2\2\23j\3\2\2\2\25m\3\2\2\2\27p\3\2\2"+
		"\2\31r\3\2\2\2\33t\3\2\2\2\35w\3\2\2\2\37z\3\2\2\2!}\3\2\2\2#\u0080\3"+
		"\2\2\2%\u0082\3\2\2\2\'\u0087\3\2\2\2)\u008d\3\2\2\2+\u0090\3\2\2\2-\u0096"+
		"\3\2\2\2/\u009e\3\2\2\2\61\u00a6\3\2\2\2\63\u00a8\3\2\2\2\65\u00aa\3\2"+
		"\2\2\67\u00ae\3\2\2\29\u00b9\3\2\2\2;\u00be\3\2\2\2=\u00c0\3\2\2\2?\u00c4"+
		"\3\2\2\2A\u00cc\3\2\2\2C\u00d3\3\2\2\2E\u00d6\3\2\2\2G\u00de\3\2\2\2I"+
		"\u00eb\3\2\2\2K\u00f5\3\2\2\2M\u00f7\3\2\2\2O\u00f9\3\2\2\2Q\u0106\3\2"+
		"\2\2S\u010f\3\2\2\2U\u011d\3\2\2\2W\u0121\3\2\2\2YZ\7}\2\2Z\4\3\2\2\2"+
		"[\\\7\177\2\2\\\6\3\2\2\2]^\7=\2\2^\b\3\2\2\2_`\7*\2\2`\n\3\2\2\2ab\7"+
		"+\2\2b\f\3\2\2\2cd\7k\2\2de\7h\2\2e\16\3\2\2\2fg\7>\2\2g\20\3\2\2\2hi"+
		"\7@\2\2i\22\3\2\2\2jk\7>\2\2kl\7?\2\2l\24\3\2\2\2mn\7@\2\2no\7?\2\2o\26"+
		"\3\2\2\2pq\7#\2\2q\30\3\2\2\2rs\7?\2\2s\32\3\2\2\2tu\7?\2\2uv\7?\2\2v"+
		"\34\3\2\2\2wx\7#\2\2xy\7?\2\2y\36\3\2\2\2z{\7(\2\2{|\7(\2\2| \3\2\2\2"+
		"}~\7~\2\2~\177\7~\2\2\177\"\3\2\2\2\u0080\u0081\7.\2\2\u0081$\3\2\2\2"+
		"\u0082\u0083\7g\2\2\u0083\u0084\7n\2\2\u0084\u0085\7u\2\2\u0085\u0086"+
		"\7g\2\2\u0086&\3\2\2\2\u0087\u0088\7y\2\2\u0088\u0089\7j\2\2\u0089\u008a"+
		"\7k\2\2\u008a\u008b\7n\2\2\u008b\u008c\7g\2\2\u008c(\3\2\2\2\u008d\u008e"+
		"\t\2\2\2\u008e\u008f\b\25\2\2\u008f*\3\2\2\2\u0090\u0091\7r\2\2\u0091"+
		"\u0092\7t\2\2\u0092\u0093\7k\2\2\u0093\u0094\7p\2\2\u0094\u0095\7v\2\2"+
		"\u0095,\3\2\2\2\u0096\u0097\7r\2\2\u0097\u0098\7t\2\2\u0098\u0099\7k\2"+
		"\2\u0099\u009a\7p\2\2\u009a\u009b\7v\2\2\u009b\u009c\7n\2\2\u009c\u009d"+
		"\7p\2\2\u009d.\3\2\2\2\u009e\u009f\7t\2\2\u009f\u00a0\7g\2\2\u00a0\u00a1"+
		"\7c\2\2\u00a1\u00a2\7f\2\2\u00a2\u00a3\7K\2\2\u00a3\u00a4\7p\2\2\u00a4"+
		"\u00a5\7v\2\2\u00a5\60\3\2\2\2\u00a6\u00a7\t\3\2\2\u00a7\62\3\2\2\2\u00a8"+
		"\u00a9\4\62;\2\u00a9\64\3\2\2\2\u00aa\u00ab\4\63;\2\u00ab\66\3\2\2\2\u00ac"+
		"\u00af\5\61\31\2\u00ad\u00af\t\4\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00ad\3"+
		"\2\2\2\u00af\u00b5\3\2\2\2\u00b0\u00b4\5\61\31\2\u00b1\u00b4\5\63\32\2"+
		"\u00b2\u00b4\t\4\2\2\u00b3\u00b0\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b2"+
		"\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6"+
		"8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00ba\5\63\32\2\u00b9\u00b8\3\2\2"+
		"\2\u00ba\u00bb\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc:"+
		"\3\2\2\2\u00bd\u00bf\t\5\2\2\u00be\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf"+
		"<\3\2\2\2\u00c0\u00c1\t\6\2\2\u00c1\u00c2\5;\36\2\u00c2\u00c3\59\35\2"+
		"\u00c3>\3\2\2\2\u00c4\u00c5\59\35\2\u00c5\u00c6\7\60\2\2\u00c6\u00c7\5"+
		"9\35\2\u00c7@\3\2\2\2\u00c8\u00cd\5? \2\u00c9\u00ca\5? \2\u00ca\u00cb"+
		"\5=\37\2\u00cb\u00cd\3\2\2\2\u00cc\u00c8\3\2\2\2\u00cc\u00c9\3\2\2\2\u00cd"+
		"\u00cf\3\2\2\2\u00ce\u00d0\t\7\2\2\u00cf\u00ce\3\2\2\2\u00cf\u00d0\3\2"+
		"\2\2\u00d0B\3\2\2\2\u00d1\u00d4\5\63\32\2\u00d2\u00d4\5\61\31\2\u00d3"+
		"\u00d1\3\2\2\2\u00d3\u00d2\3\2\2\2\u00d4D\3\2\2\2\u00d5\u00d7\5C\"\2\u00d6"+
		"\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2"+
		"\2\2\u00d9F\3\2\2\2\u00da\u00db\7\62\2\2\u00db\u00df\7z\2\2\u00dc\u00dd"+
		"\7\62\2\2\u00dd\u00df\7Z\2\2\u00de\u00da\3\2\2\2\u00de\u00dc\3\2\2\2\u00df"+
		"\u00e0\3\2\2\2\u00e0\u00e1\5E#\2\u00e1\u00e2\7\60\2\2\u00e2\u00e3\5E#"+
		"\2\u00e3\u00e4\t\b\2\2\u00e4\u00e5\5;\36\2\u00e5\u00e7\59\35\2\u00e6\u00e8"+
		"\t\7\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8H\3\2\2\2\u00e9"+
		"\u00ec\5A!\2\u00ea\u00ec\5G$\2\u00eb\u00e9\3\2\2\2\u00eb\u00ea\3\2\2\2"+
		"\u00ecJ\3\2\2\2\u00ed\u00f6\7\62\2\2\u00ee\u00f2\5\65\33\2\u00ef\u00f1"+
		"\5\63\32\2\u00f0\u00ef\3\2\2\2\u00f1\u00f4\3\2\2\2\u00f2\u00f0\3\2\2\2"+
		"\u00f2\u00f3\3\2\2\2\u00f3\u00f6\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f5\u00ed"+
		"\3\2\2\2\u00f5\u00ee\3\2\2\2\u00f6L\3\2\2\2\u00f7\u00f8\n\t\2\2\u00f8"+
		"N\3\2\2\2\u00f9\u0101\7$\2\2\u00fa\u0100\5M\'\2\u00fb\u00fc\7^\2\2\u00fc"+
		"\u0100\7$\2\2\u00fd\u00fe\7^\2\2\u00fe\u0100\7^\2\2\u00ff\u00fa\3\2\2"+
		"\2\u00ff\u00fb\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u0103\3\2\2\2\u0101\u00ff"+
		"\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0104\3\2\2\2\u0103\u0101\3\2\2\2\u0104"+
		"\u0105\7$\2\2\u0105P\3\2\2\2\u0106\u0107\7\61\2\2\u0107\u0108\7\61\2\2"+
		"\u0108\u010c\3\2\2\2\u0109\u010b\n\n\2\2\u010a\u0109\3\2\2\2\u010b\u010e"+
		"\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010dR\3\2\2\2\u010e"+
		"\u010c\3\2\2\2\u010f\u0110\7\61\2\2\u0110\u0111\7,\2\2\u0111\u0115\3\2"+
		"\2\2\u0112\u0114\13\2\2\2\u0113\u0112\3\2\2\2\u0114\u0117\3\2\2\2\u0115"+
		"\u0116\3\2\2\2\u0115\u0113\3\2\2\2\u0116\u0118\3\2\2\2\u0117\u0115\3\2"+
		"\2\2\u0118\u0119\7,\2\2\u0119\u011a\7\61\2\2\u011aT\3\2\2\2\u011b\u011e"+
		"\5Q)\2\u011c\u011e\5S*\2\u011d\u011b\3\2\2\2\u011d\u011c\3\2\2\2\u011e"+
		"\u011f\3\2\2\2\u011f\u0120\b+\3\2\u0120V\3\2\2\2\u0121\u0122\13\2\2\2"+
		"\u0122X\3\2\2\2\26\2\u00ae\u00b3\u00b5\u00bb\u00be\u00cc\u00cf\u00d3\u00d8"+
		"\u00de\u00e7\u00eb\u00f2\u00f5\u00ff\u0101\u010c\u0115\u011d\4\3\25\2"+
		"\3+\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}