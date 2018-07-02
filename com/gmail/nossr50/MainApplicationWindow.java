package com.gmail.nossr50;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.runnables.ExportThread;
import com.gmail.nossr50.runnables.QueryThread;

import com.gmail.nossr50.tools.CardImageManager;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.api.SetAPI;
import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.MtgSet;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;

public class MainApplicationWindow {

    private Shell shell;
    private CardImageManager cardImageManager;
    
    private String appName  = "MTG JSON Tool";
    private String ver      = "v0.00.01";
    private String author   = "nossr50";
    
    private Text ft_cardName;
    private Text ft_types;
    private Text ft_subTypes;
    private Text ft_artist;
    private Text ft_setName;
    private Text ft_colors;
    private Text ft_rarity;
    private Text ft_cmc;
    private Text ft_power;
    private Text ft_toughness;
    private Text rt_setName;
    private Text rt_colors;
    private Text rt_rarity;
    private Text rt_cmc;
    private Text rt_cardName;
    private Text rt_types;
    private Text rt_subTypes;
    private Text rt_artist;
    private Text rt_power;
    private Text rt_toughness;
    private Text rt_artURL;
    
    private static Button filter_btn_fetch;
    private static Button rt_btn_export;
    private Text ft_supertypes;
    private Text rt_supertypes;
    
    private static List resultList;
    
    private Listener listener_fetch;
    private Listener listener_export;
    
    public static ArrayList<Card> results;
    public static AppState curState = AppState.IDLE;
    private Text rt_cardText;
    private Text rt_flavour;
    
    private Combo fdrop_set;
    private Combo fdrop_types;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            MainApplicationWindow window = new MainApplicationWindow();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(1016, 768);
        shell.setText(appName + " - "+ver+" by : "+author);
        
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        
        MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
        mntmFile.setText("File");
        
        Menu menu_1 = new Menu(mntmFile);
        mntmFile.setMenu(menu_1);
        
        MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
        mntmSave.setText("Export");
        
        MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
        mntmExit.setText("Exit");
        
        TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setBounds(10, 10, 977, 689);
        
        TabItem tabFilters = new TabItem(tabFolder, SWT.NONE);
        tabFilters.setText("Filters");
        
        Composite filterComp = new Composite(tabFolder, SWT.NONE);
        tabFilters.setControl(filterComp);
        
        ft_cardName = new Text(filterComp, SWT.BORDER);
        ft_cardName.setBounds(82, 164, 250, 21);
        
        Label filter_lbl_cardName = new Label(filterComp, SWT.NONE);
        filter_lbl_cardName.setBounds(10, 167, 66, 15);
        filter_lbl_cardName.setText("Card Name");
        
        Label filter_lbl_types = new Label(filterComp, SWT.NONE);
        filter_lbl_types.setBounds(10, 215, 55, 15);
        filter_lbl_types.setText("Types");
        
        ft_types = new Text(filterComp, SWT.BORDER);
        ft_types.setText("Creature");
        ft_types.setBounds(82, 212, 250, 21);
        
        ft_subTypes = new Text(filterComp, SWT.BORDER);
        ft_subTypes.setBounds(82, 236, 250, 21);
        
        ft_artist = new Text(filterComp, SWT.BORDER);
        ft_artist.setBounds(82, 260, 250, 21);
        
        Label filter_lbl_subTypes = new Label(filterComp, SWT.NONE);
        filter_lbl_subTypes.setText("Subtypes");
        filter_lbl_subTypes.setBounds(10, 239, 55, 15);
        
        Label filter_lbl_artist = new Label(filterComp, SWT.NONE);
        filter_lbl_artist.setText("Artist");
        filter_lbl_artist.setBounds(10, 263, 55, 15);
        
        Label filter_lbl_setName = new Label(filterComp, SWT.NONE);
        filter_lbl_setName.setText("Set Name");
        filter_lbl_setName.setBounds(10, 34, 66, 15);
        
        ft_setName = new Text(filterComp, SWT.BORDER);
        ft_setName.setText("Dominaria");
        ft_setName.setBounds(82, 31, 250, 21);
        
        Label filter_lbl_colors = new Label(filterComp, SWT.NONE);
        filter_lbl_colors.setText("Colors");
        filter_lbl_colors.setBounds(10, 58, 55, 15);
        
        ft_colors = new Text(filterComp, SWT.BORDER);
        ft_colors.setBounds(82, 55, 250, 21);
        
        Label filter_lbl_rarity = new Label(filterComp, SWT.NONE);
        filter_lbl_rarity.setText("Rarity");
        filter_lbl_rarity.setBounds(10, 82, 55, 15);
        
        ft_rarity = new Text(filterComp, SWT.BORDER);
        ft_rarity.setBounds(82, 79, 250, 21);
        
        Label filter_lbl_cmc = new Label(filterComp, SWT.NONE);
        filter_lbl_cmc.setText("CMC");
        filter_lbl_cmc.setBounds(10, 106, 55, 15);
        
        ft_cmc = new Text(filterComp, SWT.BORDER);
        ft_cmc.setBounds(82, 103, 250, 21);
        
        Label filter_lbl_power = new Label(filterComp, SWT.NONE);
        filter_lbl_power.setText("Power");
        filter_lbl_power.setBounds(10, 287, 66, 15);
        
        ft_power = new Text(filterComp, SWT.BORDER);
        ft_power.setBounds(82, 284, 250, 21);
        
        Label filter_lbl_toughness = new Label(filterComp, SWT.NONE);
        filter_lbl_toughness.setText("Toughness");
        filter_lbl_toughness.setBounds(10, 311, 66, 15);
        
        ft_toughness = new Text(filterComp, SWT.BORDER);
        ft_toughness.setBounds(82, 308, 250, 21);
        
        Label filter_lbl_header_generic = new Label(filterComp, SWT.NONE);
        filter_lbl_header_generic.setBounds(82, 10, 55, 15);
        filter_lbl_header_generic.setText("Generic");
        
        Label filter_lbl_header_specific = new Label(filterComp, SWT.NONE);
        filter_lbl_header_specific.setBounds(82, 137, 55, 15);
        filter_lbl_header_specific.setText("Specific");
        
        filter_btn_fetch = new Button(filterComp, SWT.NONE);
        filter_btn_fetch.setBounds(1, 626, 136, 25);
        filter_btn_fetch.setText("Fetch Results");
        
        Label filter_lbl_header_note1 = new Label(filterComp, SWT.NONE);
        filter_lbl_header_note1.setBounds(1, 610, 322, 15);
        filter_lbl_header_note1.setText("Note: Fields can be blank!");
        
        Label filter_lbl_supertypes = new Label(filterComp, SWT.NONE);
        filter_lbl_supertypes.setText("Supertypes");
        filter_lbl_supertypes.setBounds(10, 191, 66, 15);
        
        ft_supertypes = new Text(filterComp, SWT.BORDER);
        ft_supertypes.setText("Legendary");
        ft_supertypes.setBounds(82, 188, 250, 21);
        
        Button filter_btn_clear = new Button(filterComp, SWT.NONE);
        filter_btn_clear.setText("Clear Fields");
        filter_btn_clear.setBounds(143, 626, 136, 25);
        
        Group grpPresets = new Group(filterComp, SWT.NONE);
        grpPresets.setText("Presets");
        grpPresets.setBounds(6, 337, 953, 267);
        
        fdrop_set = new Combo(grpPresets, SWT.NONE);
        fdrop_set.setBounds(10, 39, 252, 23);
        
        Label fl_drop_set = new Label(grpPresets, SWT.NONE);
        fl_drop_set.setBounds(10, 20, 62, 15);
        fl_drop_set.setText("Set");
        
        Label fl_drop_types = new Label(grpPresets, SWT.NONE);
        fl_drop_types.setText("Types");
        fl_drop_types.setBounds(10, 68, 62, 15);
        
        fdrop_types = new Combo(grpPresets, SWT.NONE);
        fdrop_types.setBounds(10, 87, 252, 23);
        
        Button filter_btn_addPresets = new Button(grpPresets, SWT.NONE);
        filter_btn_addPresets.setBounds(10, 232, 75, 25);
        filter_btn_addPresets.setText("Add Presets");
        
        TabItem tabResults = new TabItem(tabFolder, SWT.NONE);
        tabResults.setText("Results");
        
        Composite resultComp = new Composite(tabFolder, SWT.NONE);
        tabResults.setControl(resultComp);
        
        resultList = new List(resultComp, SWT.BORDER | SWT.V_SCROLL);
        resultList.setBounds(10, 10, 300, 610);
        
        Label result_lbl_setName = new Label(resultComp, SWT.NONE);
        result_lbl_setName.setText("Set Name");
        result_lbl_setName.setBounds(316, 386, 66, 15);
        
        Label result_lbl_colors = new Label(resultComp, SWT.NONE);
        result_lbl_colors.setText("Colors");
        result_lbl_colors.setBounds(316, 410, 55, 15);
        
        Label result_lbl_rarity = new Label(resultComp, SWT.NONE);
        result_lbl_rarity.setText("Rarity");
        result_lbl_rarity.setBounds(316, 458, 55, 15);
        
        Label result_lbl_cmc = new Label(resultComp, SWT.NONE);
        result_lbl_cmc.setText("CMC");
        result_lbl_cmc.setBounds(316, 434, 55, 15);
        
        rt_setName = new Text(resultComp, SWT.BORDER);
        rt_setName.setBounds(388, 383, 250, 21);
        
        rt_colors = new Text(resultComp, SWT.BORDER);
        rt_colors.setBounds(388, 407, 250, 21);
        
        rt_rarity = new Text(resultComp, SWT.BORDER);
        rt_rarity.setBounds(388, 455, 250, 21);
        
        rt_cmc = new Text(resultComp, SWT.BORDER);
        rt_cmc.setBounds(388, 431, 250, 21);
        
        Label result_lbl_cardName = new Label(resultComp, SWT.NONE);
        result_lbl_cardName.setText("Card Name");
        result_lbl_cardName.setBounds(316, 362, 66, 15);
        
        rt_cardName = new Text(resultComp, SWT.BORDER);
        rt_cardName.setBounds(388, 359, 250, 21);
        
        Label result_lbl_types = new Label(resultComp, SWT.NONE);
        result_lbl_types.setText("Types");
        result_lbl_types.setBounds(644, 383, 55, 15);
        
        rt_types = new Text(resultComp, SWT.BORDER);
        rt_types.setBounds(716, 380, 250, 21);
        
        Label result_lbl_subTypes = new Label(resultComp, SWT.NONE);
        result_lbl_subTypes.setText("Subtypes");
        result_lbl_subTypes.setBounds(644, 407, 55, 15);
        
        rt_subTypes = new Text(resultComp, SWT.BORDER);
        rt_subTypes.setBounds(716, 404, 250, 21);
        
        Label result_lbl_artist = new Label(resultComp, SWT.NONE);
        result_lbl_artist.setText("Artist");
        result_lbl_artist.setBounds(644, 490, 55, 15);
        
        rt_artist = new Text(resultComp, SWT.BORDER);
        rt_artist.setBounds(716, 487, 250, 21);
        
        Label result_lbl_power = new Label(resultComp, SWT.NONE);
        result_lbl_power.setText("Power");
        result_lbl_power.setBounds(316, 490, 66, 15);
        
        rt_power = new Text(resultComp, SWT.BORDER);
        rt_power.setBounds(388, 487, 250, 21);
        
        Label result_lbl_toughness = new Label(resultComp, SWT.NONE);
        result_lbl_toughness.setText("Toughness");
        result_lbl_toughness.setBounds(316, 514, 66, 15);
        
        rt_toughness = new Text(resultComp, SWT.BORDER);
        rt_toughness.setBounds(388, 511, 250, 21);
        
        Label result_lbl_artURL = new Label(resultComp, SWT.NONE);
        result_lbl_artURL.setText("Art URL");
        result_lbl_artURL.setBounds(644, 514, 66, 15);
        
        rt_artURL = new Text(resultComp, SWT.BORDER);
        rt_artURL.setBounds(716, 511, 250, 21);
        
        Label result_lbl_supertypes = new Label(resultComp, SWT.NONE);
        result_lbl_supertypes.setText("Supertypes");
        result_lbl_supertypes.setBounds(644, 359, 66, 15);
        
        rt_supertypes = new Text(resultComp, SWT.BORDER);
        rt_supertypes.setBounds(716, 356, 250, 21);
        
        rt_btn_export = new Button(resultComp, SWT.NONE);
        rt_btn_export.setBounds(10, 626, 107, 25);
        rt_btn_export.setText("Export Results");
        
        /*
         * Listeners
         */
        
        listener_export = new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    exportButtonPressed();
                    break;
                }
                }
            }
        };
        
        listener_fetch = new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    fetchButtonPressed();
                    break;
                }
                }
            }
        };
        
        resultList.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                if(resultList.getSelectionCount() > 0)
                    updateResultFields(resultList.getSelectionIndex());
            }
        });
        
        
        rt_btn_export.addListener(SWT.Selection, listener_export);
        filter_btn_fetch.addListener(SWT.Selection, listener_fetch);
        
        initPresets();
        
        Group grpCard = new Group(resultComp, SWT.NONE);
        grpCard.setText("Card");
        grpCard.setBounds(316, 10, 643, 340);
        
        Composite cardArtCanvas = new Composite(grpCard, SWT.NONE);
        cardArtCanvas.setLocation(10, 19);
        cardArtCanvas.setSize(223, 311);
        
        cardImageManager = new CardImageManager(cardArtCanvas, Display.getDefault());
        
        rt_cardText = new Text(grpCard, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        rt_cardText.setLocation(236, 38);
        rt_cardText.setSize(322, 93);
        
        Label result_lbl_cardText = new Label(grpCard, SWT.NONE);
        result_lbl_cardText.setLocation(237, 20);
        result_lbl_cardText.setSize(55, 15);
        result_lbl_cardText.setText("Card Text");
        
        Label result_lbl_flavour = new Label(grpCard, SWT.NONE);
        result_lbl_flavour.setLocation(236, 133);
        result_lbl_flavour.setSize(83, 15);
        result_lbl_flavour.setText("Flavour Text");
        
        rt_flavour = new Text(grpCard, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        rt_flavour.setLocation(235, 148);
        rt_flavour.setSize(322, 51);
    }
    
    private void initPresets()
    {
        fdrop_types.removeAll();
        fdrop_set.removeAll();
        
        for(String s : CardAPI.getAllCardTypes())
        {
            fdrop_types.add(s.toString());
        }
        
        for(MtgSet set : SetAPI.getAllSets())
        {
            fdrop_set.add(set.getName());
        }
        
        fdrop_types.update();
        fdrop_set.update();
    }
    
    private void updateResultFields(int selectionIndex)
    {
        Card curCard = results.get(selectionIndex);
        
        if(curCard != null)
        {
            //Set Name
            updateResultField(rt_setName, curCard.getSetName());
            
            //Colors
            updateResultField(rt_colors, curCard.getColors());
            
            //Rarity
            updateResultField(rt_rarity, curCard.getRarity());
            
            //CMC
            updateResultField(rt_cmc, Double.toString(curCard.getCmc()));
            
            //Card Name
            updateResultField(rt_cardName, curCard.getName());
            
            //Super-Types
            updateResultField(rt_supertypes, curCard.getSupertypes());
            
            //Type
            updateResultField(rt_types, curCard.getTypes());
            
            //Sub-Types
            updateResultField(rt_subTypes, curCard.getSubtypes());
            
            //Artist
            updateResultField(rt_artist, curCard.getArtist());
            
            //Power
            updateResultField(rt_power, curCard.getPower());
            
            //Toughness
            updateResultField(rt_toughness, curCard.getToughness());
            
            //Card Text
            if(curCard.getOriginalText() != null && !curCard.getOriginalText().isEmpty())
                updateResultField(rt_cardText, curCard.getOriginalText());
            else
                updateResultField(rt_cardText, curCard.getText());
            
            //Flavour Text
            updateResultField(rt_flavour, curCard.getFlavor());
            
            //Image URL (Card Art)
            updateResultField(rt_artURL, curCard.getImageUrl());
            
            //Load Image Async
            if(curCard.getImageUrl() != null && !curCard.getImageUrl().isEmpty())
            {
                try {
                    cardImageManager.setVisibility(true);
                    URL cardURL = new URL(curCard.getImageUrl());
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            cardImageManager.loadImage(Display.getDefault(), cardURL);
                        }
                    });
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            } else {
                cardImageManager.setVisibility(false);
            }
        }
    }
    
    private void updateResultField(Text resultField, String[] results)
    {
        if(results != null && results.length > 0)
        {
            String newString = "";
            
            for(int x = 0; x < results.length; x++)
            {
                if(x+1 < results.length)
                {
                    newString+=results[x]+", ";
                } else {
                    newString+=results[x];
                }
                
                resultField.setText(newString);
            }
        } else {
            resultField.setText("");
            
        }
        
        resultField.update();
    }
    
    private void updateResultField(Text resultField, String results)
    {
        if(results != null && results.length() > 0)
        {
            resultField.setText(results);
        } else {
            resultField.setText("");
        }
        
        resultField.update();
    }
    
    private void exportButtonPressed()
    {
        if(rt_btn_export.isEnabled())
        {
            //Disable the buttons
            disableButtons();

            //progressBar.setVisible(true);
            //progressBar.update();

            //Executes the DeckScript in a new thread
            ExportThread et = new ExportThread(results);
            Thread thread = new Thread(et);

            thread.start();
        }
    }
    
    private void fetchButtonPressed()
    {
        if(filter_btn_fetch.isEnabled())
        {
            //Disable the buttons
            disableButtons();

            //progressBar.setVisible(true);
            //progressBar.update();

            //Executes the DeckScript in a new thread
            QueryThread qs = new QueryThread(getFilters());
            Thread thread = new Thread(qs);

            thread.start();
        }
    }
    
    synchronized private void disableButtons()
    {
        filter_btn_fetch.setEnabled(false);
        rt_btn_export.setEnabled(false);
        
        filter_btn_fetch.update();
        rt_btn_export.update();
    }
    
    synchronized private static void enableButtons()
    {
        filter_btn_fetch.setEnabled(true);
        rt_btn_export.setEnabled(true);
        
        filter_btn_fetch.update();
        rt_btn_export.update();
    }
    
    synchronized public static void asyncEnableButtons()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                enableButtons();
            }
        });
    }
    
    private ArrayList<String> getFilters()
    {
        ArrayList<String> newFilters = new ArrayList<String>();
        
        //Set Name
        addFilterSingleton(newFilters, "setName=", ft_setName);
        
        //Colors
        addFilterMulti(newFilters, "colors=", ft_colors);
        
        //Rarity
        addFilterSingleton(newFilters, "rarity=", ft_rarity);
        
        //CMC
        addFilterSingleton(newFilters, "cmc=", ft_cmc);
        
        //Card Name
        addFilterSingleton(newFilters, "name=", ft_cardName);
        
        //Super-Types
        addFilterMulti(newFilters, "supertypes=", ft_supertypes);
        
        //Type
        addFilterMulti(newFilters, "types=", ft_types);
        
        //Sub-Types
        addFilterMulti(newFilters, "subtypes=", ft_subTypes);
        
        //Artist
        addFilterSingleton(newFilters, "artist=", ft_artist);
        
        //Power
        addFilterSingleton(newFilters, "power=", ft_power);
        
        //Toughness
        addFilterSingleton(newFilters, "toughness=", ft_toughness);
        
        return newFilters;
    }
    
    private void addFilterSingleton(ArrayList<String> curFilters, String filterPrefix, Text text)
    {
        String textFromField = text.getText();
        
        if(textFromField != null && !textFromField.isEmpty())
        {
            curFilters.add(filterPrefix+textFromField);
            System.out.println("Added filter: "+filterPrefix+textFromField);
        } else {
            System.out.println("No filter for field.");
        }
            
    }
    
    private void addFilterMulti(ArrayList<String> curFilters, String filterPrefix, Text text)
    {
        String textFromField = text.getText();
        
        if(textFromField != null && !textFromField.isEmpty())
        {
            String newText = "";
            
            //Remove all spaces
            for(char c : textFromField.toCharArray())
            {
                if(c != ' ')
                    newText+=c;
            }
            
            curFilters.add(filterPrefix+newText);
            System.out.println("Added filter: "+filterPrefix+newText);
        } else {
            System.out.println("No filter for field.");
        }
    }
    
    synchronized public static void setState(AppState newState)
    {
        curState = newState;
    }
    
    synchronized public static void asyncUpdateResults()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                updateResults();
            }
        });
    }
    
    public static void updateResults()
    {
        System.out.println("Updating results...");
        
        resultList.removeAll();
        
        if(results != null && results.size() > 0) {
            for(Card curCard : results)
            {
                resultList.add(curCard.getName());
            }
            createDialog("Query finished, check the results!");
        } else {
            createDialog("No results found for current filters!");
        }
        
        resultList.update();
        enableButtons();
    }
    
    public static void createDialog(String dialogTxt)
    {
        JOptionPane.showMessageDialog(null, dialogTxt);
    }
}
