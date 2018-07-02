package com.gmail.nossr50.runnables;

import java.util.ArrayList;

import com.gmail.nossr50.MainApplicationWindow;
import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.tools.Exporter;

import io.magicthegathering.javasdk.resource.Card;

public class ExportThread implements Runnable {
    
    private ArrayList<Card> resultList;

    public ExportThread(ArrayList<Card> results) {
        resultList = results;
    }

    @Override
    public void run() {
        MainApplicationWindow.setState(AppState.RUNNING);
        Exporter.exportResults(resultList);
        MainApplicationWindow.setState(AppState.FINISHED);
        MainApplicationWindow.asyncEnableButtons();
        MainApplicationWindow.createDialog("Export finished!");
    }

}
