package entities;

/**
 * NAVAL INSPECTOR SUBCLASS
 * Represents a certified naval inspector with official credential documentation,
 * extending the base Military class with specialized inspection credentials.
 */
public class NavalInspector extends Military {
    private String credentialNumber;

    /**
     * CONSTRUCTOR
     * Initializes the naval inspector with name, rank, and official credential number.
     */
    public NavalInspector(String name, String rank, String credentialNumber) {
        super(name, rank, "NAVAL INSPECTOR");
        this.credentialNumber = (credentialNumber != null && !credentialNumber.trim().isEmpty()) ? credentialNumber.trim() : "";
    }

    public String getCredentialNumber() { 
        return credentialNumber; 
    }

    @Override
    public String getOfficialIdentification() {
        return "NAVAL INSPECTOR (º " + credentialNumber + "): " + getRank().toUpperCase() + " - " + getName().toUpperCase();
    }
}