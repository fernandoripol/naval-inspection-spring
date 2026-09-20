package entities;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import exceptions.DomainException;
import services.Exportable;

/**
 * INSPECTION REPORT ENTITY
 * 
 * Manages the complete naval inspection report, consolidating operational data,
 * team staff, vessel boardings, statistics, fuel consumption, and official output generation.
 */
public class InspectionReport implements Exportable {

    private String delegation;
    private LocalDate operationDate;
    private String oms;
    private LocalTime departureTime;
    private LocalTime returnTime;
    private String operationLocation;
    private String vehicle;
    private String officialVessel;
    
    // Official fields / Campos oficiais
    private String marinas;
    private String weather;
    private String occurrences;
    private String irregularities;
    private String accommodations;
    private String accessFacilities;
    private String suggestions;
    private String observations;
    
    // Inspetor Mais Antigo (Assinatura)
    private String seniorInspectorName = "MATHEUS GORZA DA CUNHA MONNERAT";
    private String seniorInspectorRank = "Segundo-Sargento (CN)";
    
    private double fuelGasoline;
    private double fuelDiesel;

    private List<Military> militaryTeam;
    private List<Boarding> boardings;
    
    // Lista para suportar múltiplos dias na cronologia
    private List<String[]> chronologyDays;

    private static final DateTimeFormatter FMT_EXTENDED_DATE = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
    private static final DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern("HH:mm");

    public InspectionReport(String delegation, LocalDate operationDate, String oms, LocalTime departureTime, 
                            String operationLocation, String vehicle, String officialVessel, 
                            String occurrences) {
        
        this.delegation = (delegation != null) ? delegation : "DELEGACIA FLUVIAL DE FURNAS";
        this.operationDate = (operationDate != null) ? operationDate : LocalDate.now();
        this.oms = (oms != null) ? oms : "332/2026";
        this.departureTime = (departureTime != null) ? departureTime : LocalTime.of(9, 0);
        this.returnTime = LocalTime.of(17, 10);
        this.operationLocation = (operationLocation != null) ? operationLocation : "LAGO DE FURNAS";
        this.vehicle = (vehicle != null) ? vehicle : "GMF-7D82";
        this.officialVessel = (officialVessel != null) ? officialVessel : "ECSR - TENAZ";
        
        this.marinas = "-";
        this.weather = "Tempo parcialmente nublado.";
        this.occurrences = (occurrences != null) ? occurrences : "Equipe realizou inspeção naval no Lago de Furnas.";
        this.irregularities = "Não houve.";
        this.accommodations = "Não houve.";
        this.accessFacilities = "Clube Náutico Engenheiro Mauro de Ferraz.";
        this.suggestions = "Não houve.";
        this.observations = "Não houve.";

        this.militaryTeam = new ArrayList<>();
        this.boardings = new ArrayList<>();
        this.chronologyDays = new ArrayList<>();
    }

    // Setters and Getters / Métodos de alteração e consulta
    public void setOms(String oms) { this.oms = oms; }
    public void setOperationLocation(String operationLocation) { this.operationLocation = operationLocation; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }
    public void setOfficialVessel(String officialVessel) { this.officialVessel = officialVessel; }
    public void setMarinas(String marinas) { this.marinas = marinas; }
    public void setWeather(String weather) { this.weather = weather; }
    public void setOccurrences(String occurrences) { this.occurrences = occurrences; }
    public void setIrregularities(String irregularities) { this.irregularities = irregularities; }
    public void setAccommodations(String accommodations) { this.accommodations = accommodations; }
    public void setAccessFacilities(String accessFacilities) { this.accessFacilities = accessFacilities; }
    public void setSuggestions(String suggestions) { this.suggestions = suggestions; }
    public void setObservations(String observations) { this.observations = observations; }
    public void setDepartureTime(LocalTime time) { if (time != null) this.departureTime = time; }
    public void setReturnTime(LocalTime time) { if (time != null) this.returnTime = time; }
    
    public LocalDate getOperationDate() { return this.operationDate; }
    
    public void setOperationPeriod(LocalDate start, LocalDate end) {
        if (start != null) this.operationDate = start;
    }
    
    public void addChronologyDay(String dayName, LocalDate date, String pin, String obs) {
        this.chronologyDays.add(new String[] { dayName, date.format(DateTimeFormatter.ofPattern("dd/MM/yy")), pin, obs });
    }
    
    public void clearChronology() {
        this.chronologyDays.clear();
    }
    
    public int getChronologyCount() {
        return this.chronologyDays.size();
    }
    
    public void setSeniorInspector(String name, String rank) {
        if (name != null && !name.isEmpty()) this.seniorInspectorName = name.toUpperCase();
        if (rank != null && !rank.isEmpty()) this.seniorInspectorRank = rank;
    }

    public void addMilitary(Military m) {
        if (m == null) throw new DomainException("Null military member cannot be added.");
        this.militaryTeam.add(m);
    }

    public void addBoarding(Boarding b) {
        if (b == null) throw new DomainException("Null boarding record cannot be added.");
        this.boardings.add(b);
    }

    public void setFuel(double gasoline, double diesel) {
        this.fuelGasoline = gasoline;
        this.fuelDiesel = diesel;
    }

    @Override
    public void generateReportFile() {
        String dateStr = this.operationDate.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String dateFormatted = this.operationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String depTimeStr = this.departureTime.format(FMT_TIME);
        String retTimeStr = (this.returnTime == null) ? "17:10" : this.returnTime.format(FMT_TIME);

        String basePath = System.getProperty("user.dir");

        // 1. Gerar PDF Oficial com Tabelas idênticas ao modelo da Marinha
        String pdfFileName = "relatorio_inspecao_" + dateStr + ".pdf";
        File pdfFile = new File(basePath, pdfFileName);
        
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new java.io.FileOutputStream(pdfFile));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.BLACK);

            Paragraph header = new Paragraph("MARINHA DO BRASIL\n" + this.delegation.toUpperCase() + "\nRELATÓRIO DE INSPEÇÃO NAVAL", titleFont);
            header.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------", normalFont));

            document.add(new Paragraph("01) TAREFA ATRIBUÍDA: FISCALIZAÇÃO DO TRÁFEGO AQUAVIÁRIO", boldFont));
            document.add(new Paragraph("02) DATA/PERÍODO: " + dateFormatted + ". OMS: " + this.oms + "\n    SAÍDA DA EQUIPE: " + depTimeStr + " | RETORNO DA EQUIPE: " + retTimeStr, normalFont));

            document.add(new Paragraph("\n03) CRONOLOGIA", boldFont));
            PdfPTable cronTable = new PdfPTable(new float[]{3f, 2f, 3f, 4f});
            cronTable.setWidthPercentage(100);
            cronTable.addCell(new PdfPCell(new Phrase("DIA", boldFont)));
            cronTable.addCell(new PdfPCell(new Phrase("DATA", boldFont)));
            cronTable.addCell(new PdfPCell(new Phrase("PIN", boldFont)));
            cronTable.addCell(new PdfPCell(new Phrase("OBSERVAÇÃO", boldFont)));

            if (chronologyDays.isEmpty()) {
                cronTable.addCell(new PdfPCell(new Phrase("OPERAÇÃO", normalFont)));
                cronTable.addCell(new PdfPCell(new Phrase(dateFormatted, normalFont)));
                cronTable.addCell(new PdfPCell(new Phrase(this.operationLocation, normalFont)));
                cronTable.addCell(new PdfPCell(new Phrase("-", normalFont)));
            } else {
                for (String[] day : chronologyDays) {
                    cronTable.addCell(new PdfPCell(new Phrase(day[0], normalFont)));
                    cronTable.addCell(new PdfPCell(new Phrase(day[1], normalFont)));
                    cronTable.addCell(new PdfPCell(new Phrase(day[2], normalFont)));
                    cronTable.addCell(new PdfPCell(new Phrase(day[3], normalFont)));
                }
            }
            document.add(cronTable);

            document.add(new Paragraph("\n04) EMBARCAÇÕES FISCALIZADAS", boldFont));
            PdfPTable embarcTable = new PdfPTable(new float[]{3f, 2.5f, 1f, 1f, 1f, 1.5f, 2f});
            embarcTable.setWidthPercentage(100);
            String[] headersEmb = {"EMBARCAÇÃO", "Nº INSCRIÇÃO", "A.I.", "A.A", "F.D.", "CLASSE", "DATA"};
            for(String h : headersEmb) {
                embarcTable.addCell(new PdfPCell(new Phrase(h, boldFont)));
            }

            int maAb = 0, erAb = 0, tcAb = 0, tpAb = 0, efAb = 0;
            int maNot = 0, erNot = 0, tcNot = 0, tpNot = 0, efNot = 0;
            int maSei = 0, erSei = 0, tcSei = 0, tpSei = 0, efSei = 0;

            for (Boarding b : boardings) {
                String classCode = b.getVessel().getVesselClass(); 
                String upperClass = classCode.toUpperCase();

                embarcTable.addCell(new PdfPCell(new Phrase(b.getVessel().getName(), normalFont)));
                embarcTable.addCell(new PdfPCell(new Phrase(b.getVessel().getRegistration(), normalFont)));
                embarcTable.addCell(new PdfPCell(new Phrase(b.getInfractionNotice(), normalFont)));
                embarcTable.addCell(new PdfPCell(new Phrase(b.getSeizureRecord(), normalFont)));
                embarcTable.addCell(new PdfPCell(new Phrase(b.getLegalCustodian(), normalFont)));
                embarcTable.addCell(new PdfPCell(new Phrase(classCode, normalFont)));
                embarcTable.addCell(new PdfPCell(new Phrase(b.getFormattedDate(), normalFont)));

                if (upperClass.contains("M.A") || upperClass.contains("MOTO") || upperClass.contains("JET")) { 
                    maAb++; if(b.hasNotification()) maNot++; if(b.hasSeizure()) maSei++; 
                } else if (upperClass.contains("E.R") || upperClass.contains("ESPORTE") || upperClass.contains("RECREIO")) { 
                    erAb++; if(b.hasNotification()) erNot++; if(b.hasSeizure()) erSei++; 
                } else if (upperClass.contains("T.C") || upperClass.contains("CARGA") || upperClass.contains("BALSA")) { 
                    tcAb++; if(b.hasNotification()) tcNot++; if(b.hasSeizure()) tcSei++; 
                } else if (upperClass.contains("T.P") || upperClass.contains("PASSAGEIRO")) { 
                    tpAb++; if(b.hasNotification()) tpNot++; if(b.hasSeizure()) tpSei++; 
                } else { 
                    efAb++; if(b.hasNotification()) efNot++; if(b.hasSeizure()) efSei++; 
                }
            }
            document.add(embarcTable);
            document.add(new Paragraph("*MA (Moto aquática), E/R (Esporte e Recreio), T/C (Transporte de Carga), T/P (Passageiros)", normalFont));

            PdfPTable totalTable = new PdfPTable(new float[]{3f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
            totalTable.setWidthPercentage(100);
            String[] headersTot = {"TOTAL", "MOTO-AQUÁTICA", "ESPORTE E RECREIO", "BALSA", "PASSAGEIROS", "TURISMO"};
            for(String h : headersTot) {
                totalTable.addCell(new PdfPCell(new Phrase(h, boldFont)));
            }
            totalTable.addCell(new PdfPCell(new Phrase("ABORDAGENS", boldFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(maAb), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(erAb), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(tcAb), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(tpAb), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(efAb), normalFont)));

            totalTable.addCell(new PdfPCell(new Phrase("NOTIFICAÇÕES", boldFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(maNot), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(erNot), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(tcNot), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(tpNot), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(efNot), normalFont)));

            totalTable.addCell(new PdfPCell(new Phrase("APREENSÕES", boldFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(maSei), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(erSei), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(tcSei), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(tpSei), normalFont)));
            totalTable.addCell(new PdfPCell(new Phrase(String.valueOf(efSei), normalFont)));
            document.add(totalTable);

            document.add(new Paragraph("\n05) MEIOS EMPREGADOS: VIATURA: " + this.vehicle + " | EMBARCAÇÃO: " + this.officialVessel, normalFont));
            
            StringBuilder militaryStr = new StringBuilder("06) MILITARES RESPONSÁVEIS:\n");
            for (Military m : militaryTeam) {
                militaryStr.append("    - ").append(m.getOfficialIdentification()).append("\n");
            }
            document.add(new Paragraph(militaryStr.toString(), normalFont));

            document.add(new Paragraph("07) MARINAS/RAMPAS E PÍER: " + this.marinas, normalFont));
            document.add(new Paragraph("08) CONDIÇÕES METEOROLÓGICAS: " + this.weather, normalFont));
            document.add(new Paragraph("09) OCORRÊNCIAS: " + this.occurrences, normalFont));
            document.add(new Paragraph("10) OUTRAS IRREGULARIDADES: " + this.irregularities, normalFont));
            document.add(new Paragraph("11) ACOMODAÇÕES: " + this.accommodations, normalFont));
            document.add(new Paragraph("12) FACILIDADES PARA ACESSO: " + this.accessFacilities, normalFont));
            document.add(new Paragraph("13) SUGESTÕES: " + this.suggestions, normalFont));
            document.add(new Paragraph("14) OUTRAS OBSERVAÇÕES: " + this.observations, normalFont));
            document.add(new Paragraph("15) CLG - Gasolina: " + (int)this.fuelGasoline + "lts. | Diesel: " + (int)this.fuelDiesel + "lts.", boldFont));

            document.add(new Paragraph("\n\n----------------------------------------------------------------------------------------------------------------------------------", normalFont));
            Paragraph ass = new Paragraph("RUBENS IKEUTI                         | ANDRE RAIMUNDO DA SILVA\nCapitão de Corveta                    | Primeiro-Tenente (AA)\nDelegado                              | Encarregado da Divisão STA\n\n" + this.seniorInspectorName.toUpperCase() + "\n" + this.seniorInspectorRank + "\nInspetor Naval", boldFont);
            document.add(ass);

            System.out.println("✅ PDF oficial estruturado com tabelas gerado com sucesso em: " + pdfFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace(); // Imprime o erro detalhado na consola para diagnóstico
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        // 2. Gerar Ficheiro Editável (CSV)
        String csvFileName = "relatorio_editavel_" + dateStr + ".csv";
        File csvFile = new File(basePath, csvFileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            bw.write("MARINHA DO BRASIL - DELEGACIA FLUVIAL DE FURNAS");
            bw.newLine();
            bw.write("Secao;Detalhe");
            bw.newLine();
            bw.write("01) Tarefa;Fiscalizacao do Trafego Aquaviario");
            bw.newLine();
            bw.write("02) Data;" + dateFormatted);
            bw.newLine();
            bw.write("05) Veiculo;" + this.vehicle);
            bw.newLine();
            bw.write("15) CLG;Gasolina: " + (int)this.fuelGasoline + "lts. Diesel: " + (int)this.fuelDiesel + "lts.");
            System.out.println("✅ Ficheiro editável gerado com sucesso em: " + csvFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Erro ao gerar CSV: " + e.getMessage());
            e.printStackTrace(); // Imprime o erro detalhado do CSV se houver
        }

        // 3. Gerar Ficheiro de Texto (TXT) na raiz
        String txtFileName = "relatorio_inspecao.txt";
        File txtFile = new File(basePath, txtFileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile))) {
            bw.write("MARINHA DO BRASIL - DELEGACIA FLUVIAL DE FURNAS\n");
            bw.write("Relatorio gerado em: " + dateFormatted + "\n");
            bw.write("Veiculo: " + this.vehicle + " | Embarcacao Oficial: " + this.officialVessel + "\n");
            bw.write("Ocorrencias: " + this.occurrences + "\n");
            System.out.println("✅ Ficheiro TXT gerado com sucesso em: " + txtFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Erro ao gerar TXT: " + e.getMessage());
            e.printStackTrace();
        }

        printOfficialReport();
    }

    public void printOfficialReport() {
        String depTimeStr = this.departureTime.format(FMT_TIME);
        String retTimeStr = (this.returnTime == null) ? "17:10" : this.returnTime.format(FMT_TIME);

        System.out.println("\n=========================================================================================");
        System.out.println("                                MARINHA DO BRASIL");
        System.out.println("                         " + this.delegation.toUpperCase());
        System.out.println("                        RELATÓRIO DE INSPEÇÃO NAVAL");
        System.out.println("=========================================================================================");
        
        System.out.println("01) TAREFA ATRIBUÍDA: FISCALIZAÇÃO DO TRÁFEGO AQUAVIÁRIO");
        System.out.println("02) DATA/PERÍODO: " + this.operationDate.format(FMT_EXTENDED_DATE) + ". OMS: " + this.oms);
        System.out.println("    SAÍDA DA EQUIPE: " + depTimeStr + " | RETORNO DA EQUIPE: " + retTimeStr);
        
        System.out.println("\n03) CRONOLOGIA");
        System.out.println("    DIA | DATA | PIN | OBSERVAÇÃO");
        if (chronologyDays.isEmpty()) {
            System.out.println("    OPERAÇÃO | " + this.operationDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")) + " | " + this.operationLocation + " | -");
        } else {
            for (String[] day : chronologyDays) {
                System.out.println("    " + day[0] + " | " + day[1] + " | " + day[2] + " | " + day[3]);
            }
        }
        
        System.out.println("\n04) EMBARCAÇÕES FISCALIZADAS");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-18s | %-16s | %-4s | %-4s | %-4s | %-6s | %-5s\n", 
                          "EMBARCAÇÃO", "Nº DE INSCRIÇÃO", "A.I", "A.A", "F.D", "CLASSE", "DATA");
        System.out.println("-----------------------------------------------------------------------------------------");
        
        int maAb = 0, erAb = 0, tcAb = 0, tpAb = 0, efAb = 0;
        int maNot = 0, erNot = 0, tcNot = 0, tpNot = 0, efNot = 0;
        int maSei = 0, erSei = 0, tcSei = 0, tpSei = 0, efSei = 0;

        for (Boarding b : boardings) {
            String classCode = b.getVessel().getVesselClass(); 
            String upperClass = classCode.toUpperCase();

            System.out.printf("%-18s | %-16s | %-4s | %-4s | %-4s | %-6s | %-5s\n",
                    b.getVessel().getName(),
                    b.getVessel().getRegistration(),
                    b.getInfractionNotice(),
                    b.getSeizureRecord(),
                    b.getLegalCustodian(),
                    classCode,
                    b.getFormattedDate());

            if (upperClass.contains("M.A") || upperClass.contains("MOTO") || upperClass.contains("JET")) { 
                maAb++; 
                if(b.hasNotification()) maNot++; 
                if(b.hasSeizure()) maSei++; 
            }
            else if (upperClass.contains("E.R") || upperClass.contains("ESPORTE") || upperClass.contains("RECREIO")) { 
                erAb++; 
                if(b.hasNotification()) erNot++; 
                if(b.hasSeizure()) erSei++; 
            }
            else if (upperClass.contains("T.C") || upperClass.contains("CARGA") || upperClass.contains("BALSA")) { 
                tcAb++; 
                if(b.hasNotification()) tcNot++; 
                if(b.hasSeizure()) tcSei++; 
            }
            else if (upperClass.contains("T.P") || upperClass.contains("PASSAGEIRO")) { 
                tpAb++; 
                if(b.hasNotification()) tpNot++; 
                if(b.hasSeizure()) tpSei++; 
            }
            else { 
                efAb++; 
                if(b.hasNotification()) efNot++; 
                if(b.hasSeizure()) efSei++; 
            }
        }

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("*MA (Moto aquática), E/R (Esporte e Recreio), T/C (Transporte de Carga), T/P (Passageiros)");
        
        System.out.println("\nTOTAL        | MOTO-AQUÁTICA | ESPORTE E RECREIO | BALSA | TRANSPORTE PASSAGEIROS | DISPOSITIVO");
        System.out.printf("ABORDAGENS   | %-13d | %-17d | %-5d | %-22d | %-11d\n", maAb, erAb, tcAb, tpAb, efAb);
        System.out.printf("NOTIFICAÇÕES | %-13d | %-17d | %-5d | %-22d | %-11d\n", maNot, erNot, tcNot, tpNot, efNot);
        System.out.printf("APREENSÕES   | %-13d | %-17d | %-5d | %-22d | %-11d\n", maSei, erSei, tcSei, tpSei, efSei);

        System.out.println("\n05) MEIOS EMPREGADOS:");
        System.out.println("    VIATURA: " + this.vehicle + " | EMBARCAÇÃO: " + this.officialVessel);

        System.out.println("\n06) MILITARES RESPONSÁVEIS:");
        for (Military m : militaryTeam) {
            System.out.println("    " + m.getOfficialIdentification());
        }

        System.out.println("\n07) MARINAS/RAMPAS E PÍER:\n    " + this.marinas);
        System.out.println("08) CONDIÇÕES METEOROLÓGICAS:\n    " + this.weather);
        System.out.println("09) OCORRÊNCIAS:\n    " + this.occurrences);
        System.out.println("10) OUTRAS IRREGULARIDADES:\n    " + this.irregularities);
        System.out.println("11) ACOMODAÇÕES:\n    " + this.accommodations);
        System.out.println("12) FACILIDADES PARA ACESSO:\n    " + this.accessFacilities);
        System.out.println("13) SUGESTÕES:\n    " + this.suggestions);
        System.out.println("14) OUTRAS OBSERVAÇÕES:\n    " + this.observations);

        System.out.println("\n15) CLG:");
        System.out.println("    Diesel: " + (int)this.fuelDiesel + "lts | Gasolina: " + (int)this.fuelGasoline + "lts");
        
        System.out.println("\n-----------------------------------------------------------------------------------------");
        System.out.println("RUBENS IKEUTI                          | ANDRE RAIMUNDO DA SILVA");
        System.out.println("Capitão de Corveta                     | Primeiro-Tenente (AA)");
        System.out.println("Delegado                               | Encarregado da Divisão STA");
        System.out.println("\n" + this.seniorInspectorName);
        System.out.println(this.seniorInspectorRank);
        System.out.println("Inspetor Naval");
        System.out.println("=========================================================================================\n");
    }
}