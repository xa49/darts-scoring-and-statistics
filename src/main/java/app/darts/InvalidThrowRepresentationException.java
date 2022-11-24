package app.darts;

public class InvalidThrowRepresentationException extends RuntimeException {

    private static final String FORMATTING_GUIDANCE_URL = "http:TODO";

    public InvalidThrowRepresentationException(String reason, String input) {
        super(reason + " Input: " + input + ". Read more about formatting rules at " + FORMATTING_GUIDANCE_URL);
    }
}
