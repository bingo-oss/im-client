package net.bingosoft.oss.imclient.exception;

/**
 * @author kael.
 */
public class HttpRequestException extends RuntimeException {
    
    private int status;
    
    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
