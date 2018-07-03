package com.gmail.nossr50.tools;

import java.util.TimerTask;

import org.eclipse.swt.widgets.Display;

import com.gmail.nossr50.MainApplicationWindow;

public class UpdateTimerTask extends TimerTask
{
  @Override
  public void run()
  {
      MainApplicationWindow.asyncUpdateTimer(); 
  }
}
