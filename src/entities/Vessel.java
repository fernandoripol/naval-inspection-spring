package entities;

/**
 * VESSEL ENTITY
 * 
 * Represents a watercraft inspected during naval operations,
 * encapsulating its name, registration number, and vessel category classification[cite: 3].
 */
public class Vessel {

    private String name;
    private String registration;
    private VesselClass vesselClass;

    /**
     * CONSTRUCTOR
     * Initializes the vessel with name, registration, and class type, applying defensive validation[cite: 3].
     */
    public Vessel(String name, String registration, VesselClass vesselClass) {
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "UNNAMED";
        this.registration = (registration != null && !registration.trim().isEmpty()) ? registration.trim() : "UNREGISTERED";
        this.vesselClass = (vesselClass != null) ? vesselClass : VesselClass.SPORTS_AND_RECREATION;
    }

    public String getName() { return name; }
    public String getRegistration() { return registration; }
    public VesselClass getVesselClassEnum() { return vesselClass; }
    public String getVesselClass() { return vesselClass.getDescription(); }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) this.name = name.trim();
    }

    public void setRegistration(String registration) {
        if (registration != null && !registration.trim().isEmpty()) this.registration = registration.trim();
    }

    public void setVesselClass(VesselClass vesselClass) {
        if (vesselClass != null) this.vesselClass = vesselClass;
    }
}
