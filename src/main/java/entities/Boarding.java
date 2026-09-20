package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * BOARDING ENTITY
 * 
 * Represents a specific naval boarding action on a vessel, tracking official
 * infraction notices, seizure records, designated custodians, and operation dates[cite: 2].
 */
public class Boarding {

    private Vessel vessel;
    private String infractionNotice;
    private String seizureRecord;
    private String legalCustodian;
    private LocalDate boardingDate;

    private static final DateTimeFormatter FMT_DDMM = DateTimeFormatter.ofPattern("dd/MM");

    /**
     * CONSTRUCTOR
     * Initializes a boarding record with defensive validation for legal documents and dates[cite: 2].
     */
    public Boarding(Vessel vessel, String infractionNotice, String seizureRecord, String legalCustodian, LocalDate boardingDate) {
        this.vessel = vessel;
        this.setInfractionNotice(infractionNotice);
        this.setSeizureRecord(seizureRecord);
        this.setLegalCustodian(legalCustodian);
        this.boardingDate = (boardingDate != null) ? boardingDate : LocalDate.now();
    }

    public Boarding(Vessel vessel, LocalDate boardingDate) {
        this(vessel, "-", "-", "-", boardingDate);
    }

    public Vessel getVessel() { return vessel; }
    public String getInfractionNotice() { return infractionNotice; }
    public String getSeizureRecord() { return seizureRecord; }
    public String getLegalCustodian() { return legalCustodian; }
    public LocalDate getBoardingDate() { return boardingDate; }
    public String getFormattedDate() { return boardingDate.format(FMT_DDMM); }

    public boolean hasNotification() { return !this.infractionNotice.equals("-"); }
    public boolean hasSeizure() { return !this.seizureRecord.equals("-"); }

    public void setInfractionNotice(String ai) {
        this.infractionNotice = (ai == null || ai.trim().isEmpty() || ai.equalsIgnoreCase("n")) ? "-" : ai.trim();
    }

    public void setSeizureRecord(String aa) {
        this.seizureRecord = (aa == null || aa.trim().isEmpty() || aa.equalsIgnoreCase("n")) ? "-" : aa.trim();
    }

    public void setLegalCustodian(String fd) {
        this.legalCustodian = (fd == null || fd.trim().isEmpty() || fd.equalsIgnoreCase("n")) ? "-" : fd.trim();
    }

    public void setBoardingDate(LocalDate boardingDate) {
        if (boardingDate != null) this.boardingDate = boardingDate;
    }
}
