package entities;

/**
 * VESSEL CLASS ENUM
 * 
 * Defines the standard legal and operational categories of vessels 
 * subject to naval inspection under Brazilian maritime regulations.
 */
public enum VesselClass {
    JET_SKI("M.A", "Moto Aquática"),
    SPORTS_AND_RECREATION("E.R", "Esporte e Recreio"),
    BARGE("T.C", "Transporte de Carga"),
    PASSENGER_TRANSPORT("T.P", "Transporte de Passageiros"),
    TOURISM_SUPPORT("E.F", "Dispositivo Flutuante / Apoio ao Turismo");

    private final String code;
    private final String description;

    VesselClass(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}