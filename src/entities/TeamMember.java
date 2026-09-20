package entities;

/**
 * TEAM MEMBER SUBCLASS
 * 
 * Represents a standard team member participating in naval inspection operations,
 * extending the base Military class and implementing specialized official identification[cite: 4].
 */
public class TeamMember extends Military {

    /**
     * CONSTRUCTOR
     * Initializes the team member with name, rank, and assigned operational role[cite: 4].
     */
    public TeamMember(String name, String rank, String role) {
        super(name, rank, role);
    }

    @Override
    public String getOfficialIdentification() {
        return getRole().toUpperCase() + ": " + getRank().toUpperCase() + " " + getName().toUpperCase();
    }
}
