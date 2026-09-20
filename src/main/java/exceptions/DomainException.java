package exceptions;

/**
 * CUSTOM DOMAIN EXCEPTION CLASS
 * 
 * Extends RuntimeException (unchecked exception).
 * Used to handle business rule violations in the Naval Inspection system
 * (e.g., return time prior to departure time, null military staff or boardings).
 */
public class DomainException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * CONSTRUCTOR
     * @param msg Descriptive error message thrown to the user
     */
    public DomainException(String msg) {
        super(msg); // Passes the message to the RuntimeException superclass
    }
}
