package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import entities.Boarding;
import entities.VesselClass;
import entities.Vessel;
import entities.NavalInspector;
import entities.TeamMember;
import entities.InspectionReport;
import services.DatabaseService;

public class Main {

    // Método chamado pelo Spring Boot (CommandLineRunner)
    public static void executarConsola(String[] args) {
        // Inicializa o banco de dados e as tabelas SQLite
        DatabaseService.initializeDatabase();

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== NAVAL INSPECTION SYSTEM - BRAZILIAN NAVY ===");
        
        // Data principal de referência do relatório
        System.out.print("Main Operation Date (Data Principal - ex: DD/MM/AAAA ou Enter para hoje): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate operationDate = LocalDate.now();
        if (!dateStr.isEmpty()) {
            try {
                operationDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Formato inválido! Usando a data de hoje como padrão.");
            }
        }

        // Coleta de dados iniciais do relatório
        System.out.print("Enter OMS number (e.g., 332/2026): ");
        String oms = scanner.nextLine();
        
        System.out.print("PIN / Location Area (e.g., LAGO DE FURNAS): ");
        String pin = scanner.nextLine();

        System.out.print("Employed Vehicle (e.g., GMF-7D82): ");
        String vehicle = scanner.nextLine();

        System.out.print("Employed Official Vessel (e.g., ECSR - TENAZ): ");
        String officialVessel = scanner.nextLine();

        System.out.print("Departure Time (Horário de Saída - ex: 09:00): ");
        String depTimeStr = scanner.nextLine();
        LocalTime departureTime = LocalTime.of(9, 0);
        try {
            departureTime = LocalTime.parse(depTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            System.out.println("⚠️ Formato inválido! Usando 09:00 como padrão.");
        }

        System.out.print("Return Time (Horário de Retorno - ex: 17:10): ");
        String retTimeStr = scanner.nextLine();
        LocalTime returnTime = LocalTime.of(17, 10);
        try {
            returnTime = LocalTime.parse(retTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            System.out.println("⚠️ Formato inválido! Usando 17:10 como padrão.");
        }

        System.out.print("Marinas / Ramps / Piers info: ");
        String marinas = scanner.nextLine();
        if(marinas.isEmpty()) marinas = "-";

        System.out.print("Weather Conditions (Condições Meteorológicas): ");
        String weather = scanner.nextLine();
        if(weather.isEmpty()) weather = "Tempo parcialmente nublado.";

        System.out.print("Occurrences description: ");
        String occurrences = scanner.nextLine();

        System.out.print("Other Irregularities (Outras Irregularidades): ");
        String irregularities = scanner.nextLine();
        if(irregularities.isEmpty()) irregularities = "Não houve.";

        System.out.print("Accommodations (Acomodações): ");
        String accommodations = scanner.nextLine();
        if(accommodations.isEmpty()) accommodations = "Não houve.";

        System.out.print("Access Facilities (Facilidades para Acesso): ");
        String accessFacilities = scanner.nextLine();
        if(accessFacilities.isEmpty()) accessFacilities = "Clube Náutico Engenheiro Mauro de Ferraz.";

        System.out.print("Suggestions (Sugestões): ");
        String suggestions = scanner.nextLine();
        if(suggestions.isEmpty()) suggestions = "Não houve.";

        System.out.print("Observations (Outras Observações): ");
        String observations = scanner.nextLine();
        if(observations.isEmpty()) observations = "Não houve.";

        // Pergunta do Inspetor Mais Antigo para assinatura no rodapé
        System.out.println("\n--- ASSINATURA DO INSPETOR NAVAL MAIS ANTIGO ---");
        System.out.print("Senior Inspector Name (e.g., FERNANDO RODRIGUES RIPOL): ");
        String inspectorName = scanner.nextLine();
        
        System.out.print("Senior Inspector Rank (e.g., TERCEIRO SARGENTO (CN)): ");
        String inspectorRank = scanner.nextLine();

        // Instancia o relatório preenchendo todos os campos customizados
        InspectionReport report = new InspectionReport("DELEGACIA FLUVIAL DE FURNAS", operationDate, oms, departureTime, pin, vehicle, officialVessel, occurrences);
        report.setReturnTime(returnTime);
        report.setMarinas(marinas);
        report.setWeather(weather);
        report.setIrregularities(irregularities);
        report.setAccommodations(accommodations);
        report.setAccessFacilities(accessFacilities);
        report.setSuggestions(suggestions);
        report.setObservations(observations);
        
        if (!inspectorName.isEmpty() && !inspectorRank.isEmpty()) {
            report.setSeniorInspector(inspectorName, inspectorRank);
        }

        // Cadastro dinâmico de múltiplos dias na Cronologia
        System.out.println("\n--- REGISTRAR CRONOLOGIA DA OPERAÇÃO (Múltiplos Dias) ---");
        System.out.print("Deseja cadastrar dias específicos na cronologia? (S/N): ");
        String addChronology = scanner.nextLine().trim();
        if (addChronology.equalsIgnoreCase("S")) {
            boolean addingDays = true;
            while (addingDays) {
                System.out.print("Dia da Semana (ex: SÁBADO ou digite 'DONE' para encerrar): ");
                String dayName = scanner.nextLine().trim();
                if (dayName.equalsIgnoreCase("DONE")) {
                    break;
                }

                System.out.print("Data deste dia (DD/MM/AAAA): ");
                String dayDateStr = scanner.nextLine().trim();
                LocalDate dayDate = operationDate;
                try {
                    dayDate = LocalDate.parse(dayDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    System.out.println("⚠️ Data inválida, usando a data principal.");
                }

                System.out.print("Local / PIN (ex: LAGO DE FURNAS / PONTO 2): ");
                String dayPin = scanner.nextLine().trim();

                System.out.print("Observação para este dia (ou '-' para nenhuma): ");
                String dayObs = scanner.nextLine().trim();

                report.addChronologyDay(dayName, dayDate, dayPin, dayObs);
                System.out.println("✅ Dia adicionado à cronologia!\n");
            }
        }

        // Loop de cadastro dinâmico da equipe militar
        System.out.println("\n--- REGISTER RESPONSIBLE MILITARY STAFF ---");
        boolean addingMilitary = true;
        while (addingMilitary) {
            System.out.print("Military Name (or type 'DONE' to finish): ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("DONE")) {
                break;
            }

            System.out.print("Military Rank (e.g., 2° SG-CN, MN-RM2): ");
            String rank = scanner.nextLine();

            System.out.print("Function / Role (e.g., INSPETOR NAVAL / MOTORISTA, AUXILIAR): ");
            String role = scanner.nextLine();

            if (role.toUpperCase().contains("INSPETOR")) {
                System.out.print("Credential Number: ");
                String cred = scanner.nextLine();
                report.addMilitary(new NavalInspector(name, rank, cred));
            } else {
                report.addMilitary(new TeamMember(name, rank, role));
            }
            System.out.println("✅ Military added successfully!\n");
        }

        int option = -1;
        while (option != 0) {
            System.out.println("\n--- NAVAL INSPECTION MENU ---");
            System.out.println("1 - Add Inspected Vessel");
            System.out.println("2 - Register Fuel Consumption (CLG)");
            System.out.println("3 - Generate and Print Official Report");
            System.out.println("4 - Add / Register More Military Staff");
            System.out.println("5 - Edit Report Details (Editar Campos do Relatório)");
            System.out.println("0 - Exit");
            System.out.print("Choose an option: ");
            
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                continue;
            }

            if (option == 1) {
                System.out.print("Vessel Name: ");
                String vesselName = scanner.nextLine();

                System.out.print("Registration Number: ");
                String registration = scanner.nextLine();

                System.out.println("Choose Vessel Class (Type number or letters, e.g., 1 or MA):");
                System.out.println("1 - M.A (Moto Aquática)");
                System.out.println("2 - E.R (Esporte e Recreio)");
                System.out.println("3 - T.C (Transporte de Carga / Balsa)");
                System.out.println("4 - T.P (Transporte de Passageiros)");
                System.out.println("5 - E.F (Dispositivo Flutuante / Apoio)");
                System.out.print("Option: ");
                
                String inputStr = scanner.nextLine().trim().toUpperCase();

                VesselClass vesselClass = VesselClass.SPORTS_AND_RECREATION;
                if (inputStr.equals("1") || inputStr.contains("MA")) {
                    vesselClass = VesselClass.JET_SKI;
                } else if (inputStr.equals("2") || inputStr.contains("ER")) {
                    vesselClass = VesselClass.SPORTS_AND_RECREATION;
                } else if (inputStr.equals("3") || inputStr.contains("TC")) {
                    vesselClass = VesselClass.BARGE;
                } else if (inputStr.equals("4") || inputStr.contains("TP")) {
                    vesselClass = VesselClass.PASSENGER_TRANSPORT;
                } else if (inputStr.equals("5") || inputStr.contains("EF")) {
                    vesselClass = VesselClass.TOURISM_SUPPORT;
                } else {
                    System.out.println("⚠️ Opção não reconhecida! Usando padrão: Esporte e Recreio (2).");
                }

                Vessel vessel = new Vessel(vesselName, registration, vesselClass);

                System.out.print("Infraction Notice Number (Auto de Infração - A.I ou -): ");
                String infraction = scanner.nextLine();

                System.out.print("Seizure Record Number (Auto de Apreensão - A.A ou -): ");
                String seizure = scanner.nextLine();

                System.out.print("Legal Custodian (Fiel Depositário - F.D ou -): ");
                String custodian = scanner.nextLine();

                System.out.print("Boarding Date (Data da Abordagem - ex: DD/MM/AAAA ou Enter para usar a data principal): ");
                String bDateStr = scanner.nextLine().trim();
                LocalDate boardingDate = operationDate;
                if (!bDateStr.isEmpty()) {
                    try {
                        boardingDate = LocalDate.parse(bDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    } catch (DateTimeParseException e) {
                        System.out.println("⚠️ Data inválida! Usando a data principal da operação.");
                    }
                }

                Boarding boarding = new Boarding(vessel, infraction, seizure, custodian, boardingDate);
                report.addBoarding(boarding);
                
                System.out.println("✅ Vessel added successfully!");

            } else if (option == 2) {
                System.out.print("Gasoline liters spent: ");
                double gas = scanner.nextDouble();
                System.out.print("Diesel liters spent: ");
                double diesel = scanner.nextDouble();
                scanner.nextLine();

                report.setFuel(gas, diesel);
                System.out.println("✅ Fuel consumption registered!");

            } else if (option == 3) {
                report.generateReportFile();

            } else if (option == 4) {
                System.out.println("\n--- ADD NEW MILITARY MEMBER ---");
                System.out.print("Military Name: ");
                String name = scanner.nextLine();

                System.out.print("Military Rank (e.g., 2° SG-CN): ");
                String rank = scanner.nextLine();

                System.out.print("Function / Role: ");
                String role = scanner.nextLine();

                if (role.toUpperCase().contains("INSPETOR")) {
                    System.out.print("Credential Number: ");
                    String cred = scanner.nextLine();
                    report.addMilitary(new NavalInspector(name, rank, cred));
                } else {
                    report.addMilitary(new TeamMember(name, rank, role));
                }
                System.out.println("✅ Military member successfully added to the mission team!");

            } else if (option == 5) {
                System.out.println("\n--- EDIT REPORT FIELDS ---");
                System.out.println("1 - OMS Number");
                System.out.println("2 - PIN / Location Area");
                System.out.println("3 - Employed Vehicle (Viatura)");
                System.out.println("4 - Employed Official Vessel");
                System.out.println("5 - Marinas / Ramps / Piers");
                System.out.println("6 - Weather Conditions");
                System.out.println("7 - Occurrences");
                System.out.println("8 - Irregularities");
                System.out.println("9 - Access Facilities");
                System.out.println("10 - Accommodations");
                System.out.println("11 - Suggestions");
                System.out.println("12 - Observations");
                System.out.println("13 - Departure Time");
                System.out.println("14 - Return Time");
                System.out.println("15 - Senior Inspector Signee (Inspetor Mais Antigo)");
                System.out.print("Choose field to edit: ");
                
                int editOpt = -1;
                try {
                    String editInput = scanner.nextLine();
                    editOpt = Integer.parseInt(editInput);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid input.");
                    continue;
                }

                if (editOpt == 1) {
                    System.out.print("New OMS: ");
                    report.setOms(scanner.nextLine());
                } else if (editOpt == 2) {
                    System.out.print("New PIN / Location: ");
                    report.setOperationLocation(scanner.nextLine());
                } else if (editOpt == 3) {
                    System.out.print("New Vehicle: ");
                    report.setVehicle(scanner.nextLine());
                } else if (editOpt == 4) {
                    System.out.print("New Official Vessel: ");
                    report.setOfficialVessel(scanner.nextLine());
                } else if (editOpt == 5) {
                    System.out.print("New Marinas info: ");
                    report.setMarinas(scanner.nextLine());
                } else if (editOpt == 6) {
                    System.out.print("New Weather info: ");
                    report.setWeather(scanner.nextLine());
                } else if (editOpt == 7) {
                    System.out.print("New Occurrences description: ");
                    report.setOccurrences(scanner.nextLine());
                } else if (editOpt == 8) {
                    System.out.print("New Irregularities description: ");
                    report.setIrregularities(scanner.nextLine());
                } else if (editOpt == 9) {
                    System.out.print("New Access Facilities: ");
                    report.setAccessFacilities(scanner.nextLine());
                } else if (editOpt == 10) {
                    System.out.print("New Accommodations info: ");
                    report.setAccommodations(scanner.nextLine());
                } else if (editOpt == 11) {
                    System.out.print("New Suggestions info: ");
                    report.setSuggestions(scanner.nextLine());
                } else if (editOpt == 12) {
                    System.out.print("New Observations info: ");
                    report.setObservations(scanner.nextLine());
                } else if (editOpt == 13) {
                    System.out.print("New Departure Time (HH:mm): ");
                    try {
                        report.setDepartureTime(LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm")));
                    } catch (Exception e) {
                        System.out.println("⚠️ Formato inválido!");
                    }
                } else if (editOpt == 14) {
                    System.out.print("New Return Time (HH:mm): ");
                    try {
                        report.setReturnTime(LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm")));
                    } catch (Exception e) {
                        System.out.println("⚠️ Formato inválido!");
                    }
                } else if (editOpt == 15) {
                    System.out.print("Senior Inspector Name: ");
                    String sName = scanner.nextLine();
                    System.out.print("Senior Inspector Rank: ");
                    String sRank = scanner.nextLine();
                    report.setSeniorInspector(sName, sRank);
                }
                System.out.println("✅ Report field updated successfully!");
            }
        }
        scanner.close();
        System.out.println("System shutting down. Have a safe return to the OM, military!");
    }
}