package app.darts;

public class MissingEntityException extends RuntimeException{
    public MissingEntityException(String message) {
        super(message);
    }
}
