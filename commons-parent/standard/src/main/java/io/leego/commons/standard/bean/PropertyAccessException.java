package io.leego.commons.standard.bean;

/**
 * Abstract superclass for all exceptions thrown in the beans package
 * and subpackages.
 *
 * <p>Note that this is a runtime (unchecked) exception. Beans exceptions
 * are usually fatal; there is no reason for them to be checked.
 * @author Leego Yih
 */
public class PropertyAccessException extends RuntimeException {

    /**
     * Create a new BeansException with the specified message.
     * @param msg the detail message
     */
    public PropertyAccessException(String msg) {
        super(msg);
    }

    /**
     * Create a new BeansException with the root cause.
     * @param cause the root cause
     */
    public PropertyAccessException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new BeansException with the specified message
     * and root cause.
     * @param msg   the detail message
     * @param cause the root cause
     */
    public PropertyAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
