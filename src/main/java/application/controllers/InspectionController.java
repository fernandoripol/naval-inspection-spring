package application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import entities.InspectionReport;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    // Endpoint POST: O telemóvel do inspetor envia os dados e o servidor gera os relatórios
    @PostMapping
    public ResponseEntity<String> createInspectionReport(
            @RequestParam(required = false, defaultValue = "EMBARCAÇÃO PADRÃO") String vesselName, 
            @RequestParam(required = false, defaultValue = "INSPETOR") String inspectorName) {
        
        // Instancia o relatório com os dados da operação e dispara a geração (PDF + CSV + TXT)
        InspectionReport report = new InspectionReport(
            "DELEGACIA FLUVIAL DE FURNAS", 
            LocalDate.now(), 
            "332/2026", 
            null, 
            "LAGO DE FURNAS", 
            "GMF-7D82", 
            "ECSR - TENAZ", 
            "Equipe realizou inspeção naval no Lago de Furnas."
        );
        
        report.generateReportFile();
        
        return ResponseEntity.ok("✅ Inspeção da embarcação '" + vesselName + "' registada e relatórios oficiais gerados com sucesso na Delegação!");
    }
}