
package services; // Defines the folder package where the file is stored

import  entities.Mission;
import java.util.ArrayList;
import  java.util.List;


public  class MissionService {

private  List<Mission> missions = new ArrayList<>(); // Creates a private list to store missions

public void addMission(Mission mission){
missions.add(mission);

}

public List<Mission> listAllMissions() {
        return missions; // Retorna a lista completa de missões / Returns the complete list of missions
    }

}