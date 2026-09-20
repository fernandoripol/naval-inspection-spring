package entities;

/**
 * BASE MILITARY CLASS
 * 
 * Represents a military staff member involved in naval inspection operations,
 * providing core attributes like name, rank, role, and official identification format.
 */
public class Military {

    private String name;
    private String rank;
    private String role;

    /**
     * CONSTRUCTOR
     * Initializes military details with validation against empty or null values.
     */
    public Military(String name, String rank, String role) {
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "MILITARY";
        this.rank = (rank != null && !rank.trim().isEmpty()) ? rank.trim() : "-";
        this.role = (role != null && !role.trim().isEmpty()) ? role.trim() : "STAFF";
    }

    public String getName() { return name; }
    public String getRank() { return rank; }
    public String getRole() { return role; }

    /**
     * Generates the formatted official string identification for reporting.
     */
    public String getOfficialIdentification() {
        return role.toUpperCase() + ": " + rank.toUpperCase() + " " + name.toUpperCase();
    }
}
