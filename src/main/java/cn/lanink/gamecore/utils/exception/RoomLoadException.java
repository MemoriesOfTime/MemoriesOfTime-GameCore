package cn.lanink.gamecore.utils.exception;

/**
 * @author lt_name
 */
public class RoomLoadException extends Exception {

    public RoomLoadException() {
        super();
    }

    public RoomLoadException(String message) {
        super(message);
    }

    public RoomLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomLoadException(Throwable cause) {
        super(cause);
    }

}
