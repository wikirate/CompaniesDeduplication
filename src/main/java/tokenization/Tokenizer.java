package tokenization;

/**
 * An abstract tokenizer. Classes extending Tokenizer need to implement
 * the tokenize (String content) method. Classes that extend Tokenizer class allow
 * to break a string into tokens.
 *
 * @author vasgat
 */
public abstract class Tokenizer {

    /**
     * Breaks a string into tokens
     *
     * @param content string for tokenization
     * @return the array of tokens
     */
    public abstract String[] tokenize(String content);

}