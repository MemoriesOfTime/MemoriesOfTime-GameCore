package cn.lanink.gamecore.utils.exception;

/**
 * @author lt_name
 */
public class GameListenerInitException extends Exception {

    public GameListenerInitException() {

    }

    public GameListenerInitException(String message) {
        super(message);
    }

    public GameListenerInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameListenerInitException(Throwable cause) {
        super(cause);
    }

}
