package entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Mission {
    private Long id;
    private String oms;
    private LocalDate inspectionDate;
    private String polo;
    private LocalTime teamDeparture;
    private LocalTime teamReturn;

    // Construtor padrão / Default constructor
    public Mission() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOms() { return oms; }
    public void setOms(String oms) { this.oms = oms; }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public String getPolo() { return polo; }
    public void setPolo(String polo) { this.polo = polo; }

    public LocalTime getTeamDeparture() { return teamDeparture; }
    public void setTeamDeparture(LocalTime teamDeparture) { this.teamDeparture = teamDeparture; }

    public LocalTime getTeamReturn() { return teamReturn; }
    public void setTeamReturn(LocalTime teamReturn) { this.teamReturn = teamReturn; }
}