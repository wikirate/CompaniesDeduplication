package tokenization;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicTokenizer breaks given string/text into a set of tokens.
 *
 * @author vasgat
 */
public class BasicTokenizer extends Tokenizer {

    /**
     * Constant for ignoring punctuation in the string
     */
    public static boolean IGNORE_PUNCTUATION = true;

    /**
     * Constant for ignoring case in the string
     */
    public static boolean IGNORE_CASE = true;

    private boolean ignorePunctuation;
    private boolean ignoreCase;

    /**
     * Constructor of BasicTokenizer. The default behavior is to ignore
     * punctuation and casing to the strings that are going to be tokenized
     */
    public BasicTokenizer() {
        ignorePunctuation = IGNORE_PUNCTUATION;
        ignoreCase = IGNORE_PUNCTUATION;
    }

    /**
     * Constructor of BasicTokenizer. Users can define values other than
     * defaults for ignoring punctuation and casing in the strings.
     *
     * @param ignorePunctuation
     * @param ignoreCase
     */
    public BasicTokenizer(boolean ignorePunctuation, boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        this.ignorePunctuation = ignorePunctuation;
    }

    /**
     * Performs tokenization of an input string
     *
     * @param contents input string
     * @return an array of tokens (words)
     */
    @Override
    public String[] tokenize(String contents) {
        List<String> tokens = new ArrayList();
        int cursor = 0;
        while (contents != null && cursor < contents.length()) {
            char ch = contents.charAt(cursor);
            if (Character.isWhitespace(ch)) {
                cursor++;
            } else if (Character.isLetter(ch) || Character.isDigit(ch)) {
                StringBuffer buf = new StringBuffer("");
                while ((cursor < contents.length()) && (Character.isLetter(contents.charAt(cursor)) || Character.isDigit(contents.charAt(cursor)))) {
                    buf.append(contents.charAt(cursor));
                    cursor++;
                }
                tokens.add(transform(buf.toString()));
            } else {
                if (!this.ignorePunctuation) {
                    StringBuffer buf = new StringBuffer("");
                    buf.append(ch);
                    String str = buf.toString();
                    tokens.add(transform(str));
                }
                cursor++;
            }
        }
        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * Transforms a string to lowercase.
     *
     * @param s input text
     * @return the tranformed string
     */
    private String transform(String s) {
        return ignoreCase ? s.toLowerCase() : s;
    }

}
