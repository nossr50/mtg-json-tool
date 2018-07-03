package com.gmail.nossr50.runnables;

import java.util.ArrayList;

import com.gmail.nossr50.MainApplicationWindow;
import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.enums.ExportType;
import com.gmail.nossr50.enums.Flags;
import com.gmail.nossr50.tools.Exporter;

import io.magicthegathering.javasdk.resource.Card;

public class ExportThread implements Runnable {
    
    private ArrayList<Card> resultList;
    private ExportType exportType;

    public ExportThread(ArrayList<Card> results, ExportType et) {
        resultList = results;
        exportType = et;
    }

    @Override
    public void run() {
        MainApplicationWindow.setState(AppState.RUNNING);
        Exporter.exportResults(resultList, Flags.NOFLAGS, exportType);
        MainApplicationWindow.setState(AppState.FINISHED);
        MainApplicationWindow.createDialog("Export finished!");
    }

}
