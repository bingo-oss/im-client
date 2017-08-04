package net.bingosoft.oss.imclient.exception;

/**
 * @author kael.
 */
@SuppressWarnings("serial")
public class SendMessageFailException extends RuntimeException {
	
    public SendMessageFailException(String message) {
        super(message);
    }

    public SendMessageFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendMessageFailException(Throwable cause) {
        super(cause);
    }
}