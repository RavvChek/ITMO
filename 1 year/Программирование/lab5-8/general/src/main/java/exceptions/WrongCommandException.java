package exceptions;

public class WrongCommandException extends Exception {
    public WrongCommandException(String message) {
        super(message);
    }

    public WrongCommandException() {

    }
}
