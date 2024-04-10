package com.Anomaly.AIO.Helpers.Locations;

import com.Anomaly.AIO.Tasks.Skilling.WoodcuttingTask;
import org.dreambot.api.methods.map.Area;

public class WCLocations {
    public static Area regularTreeArea = new Area(3174, 3448, 3145, 3465);
    public static Area oakTreeArea = new Area(3169, 3422, 3161, 3411);
    public static Area willowTreeArea = new Area(3055, 3258, 3064, 3250);
    public static Area yewTreeArea = new Area(3201, 3506, 3225, 3498);
    public static Area wcDepositBox = new Area(3043, 3236, 3049, 3234);


    public static Area chosenArea;

    public static Area wcArea() {
        if (WoodcuttingTask.wclvl < 15) {
            chosenArea = regularTreeArea;
            return regularTreeArea;
        }
        if (WoodcuttingTask.wclvl >= 15 && WoodcuttingTask.wclvl < 30) {
            chosenArea = oakTreeArea;
            return oakTreeArea;
        }
        else chosenArea = willowTreeArea;
        return willowTreeArea;
    }



}
