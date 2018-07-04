package com.gmail.nossr50;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.gmail.nossr50.enums.AppState;
import com.gmail.nossr50.enums.ButtonType;
import com.gmail.nossr50.enums.ExportType;
import com.gmail.nossr50.enums.FieldType;
import com.gmail.nossr50.enums.StyleFlags;
import com.gmail.nossr50.enums.ExportFlags;
import com.gmail.nossr50.runnables.DialogThread;
import com.gmail.nossr50.runnables.ExportThread;
import com.gmail.nossr50.runnables.QueryThread;

import com.gmail.nossr50.tools.CardImageManager;
import com.gmail.nossr50.tools.ExportExample;
import com.gmail.nossr50.tools.UpdateTimerTask;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.api.SetAPI;
import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.MtgSet;
import io.magicthegathering.javasdk.resource.Ruling;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainApplicationWindow {

    private Shell shell;
    private CardImageManager cardImageManager;
    
    private String appName  = "MTG JSON Tool";
    private String ver      = "v0.00.05";
    private String author   = "nossr50";
    
    public static int secondsPassed     = 0;
    public static int numResults        = 0;
    
    public static Label filter_lbl_thinkTime;
    public static Label filter_lbl_progressBar;
    public static Timer timer;
    public static Label filter_lbl_header_note1;
    public static StyledText styledWarning;
    
    public static ExportExample exportExample;
    
    /*
     * Tabs
     */
    
    public static TabFolder tabFolder;
    public static TabItem tabResults;
    public static TabItem tabFilters;
    public static TabItem tbtmAdvancedExport;
    
    /*
     * Text
     * FT = Filter Tab (editable for searching)
     * RT = Result Tab
     * ET = Export Tab
     */
    
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
    private Text ft_superTypes;
    
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
    private Text rt_supertypes;
    private Text rt_cardText;
    private Text rt_flavour;
    private Text rt_rulings;
    
    /*
     * Buttons
     */
    
    //Buttons that do something complicated
    private static Button filter_btn_fetch;
    private static Button rt_btn_export;
    private static Button btnCustomExport;
    private static Button filter_btn_clear;
    
    //Add and Set buttons for presets
    private Button fset_add;
    private Button fset_set;
    private Button ftypes_add;
    private Button ftypes_set;
    private Button fsuperTypes_add;
    private Button fsuperTypes_set;
    private Button fsubTypes_add;
    private Button fsubTypes_set;
    private Button frarity_set;
    
    //Advanced export buttons
    private Button btnLabelBasics;
    private Button btnHeaders;
    private Button btnCardText;
    private Button btnFlavourText;
    private Button btnArtist;
    private Button btnLegalities;
    private Button btnRarity;
    private Button btnToughness;
    private Button btnPower;
    private Button btnCmc;
    private Button btnSubtypes;
    private Button btnTypes;
    private Button btnSupertypes;
    private Button btnSetName;
    private Button btnCardName;
    private static Button btnSimpleExport;
    private Button btnColors;
    
    /*
     * List (Query Results)
     */
    
    private static List resultList;
    
    /*
     * Listeners
     */
    
    private Listener listener_fetch;
    private Listener listener_export;
    private Listener listener_custom_export;
    private Listener listener_clear;
    private Listener listener_custom_export_flags;
    private Listener listener_simple_export;
    
    /*
    private Listener listener_drop_set;
    private Listener listener_drop_types;
    private Listener listener_drop_superTypes;
    private Listener listener_drop_subTypes;
    private Listener listener_drop_examples;
    */
    
    private Listener listener_drop_set_add;
    private Listener listener_drop_set_set;
    private Listener listener_drop_types_add;
    private Listener listener_drop_types_set;
    private Listener listener_drop_superTypes_add;
    private Listener listener_drop_superTypes_set;
    private Listener listener_drop_subTypes_add;
    private Listener listener_drop_subTypes_set;
    private Listener listener_drop_rarity_set;
    
    
    /*
     * Other vars
     */
    public static ArrayList<Card> results;
    public static AppState curState = AppState.IDLE;
    
    /*
     * Combos (Drop Down Menus)
     */
    
    private Combo fdrop_set;
    private Combo fdrop_types;
    private Combo fdrop_superTypes;
    private Combo fdrop_subTypes;
    private Combo fdrop_rarity;
    
    /*
     * Progress Bars
     */
    
    private static ProgressBar queryProgressBar;
    private Label lblStyle;
    private Label lblFluff;
    private Label lblStats;
    private Label lblBasic;
    private Button btnLabelStats;
    private Button btnLabelExtras;
    private Button btnSpacingExtras;
    private Text customExportPreview;
    private Label lblCustomExportPreview;
    private StyledText previewBuildWarning;
    private TabItem tbtmImport;
    

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
        
        tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setBounds(10, 10, 977, 689);
        
        tabFilters = new TabItem(tabFolder, SWT.NONE);
        tabFilters.setText("Filters");
        
        Composite filterComp = new Composite(tabFolder, SWT.NONE);
        tabFilters.setControl(filterComp);
        
        ft_cardName = new Text(filterComp, SWT.BORDER);
        ft_cardName.setBounds(378, 151, 250, 21);
        
        Label filter_lbl_cardName = new Label(filterComp, SWT.NONE);
        filter_lbl_cardName.setBounds(306, 154, 66, 15);
        filter_lbl_cardName.setText("Card Name");
        
        Label filter_lbl_types = new Label(filterComp, SWT.NONE);
        filter_lbl_types.setBounds(306, 202, 55, 15);
        filter_lbl_types.setText("Types");
        
        ft_types = new Text(filterComp, SWT.BORDER);
        ft_types.setText("Creature");
        ft_types.setBounds(378, 199, 250, 21);
        
        ft_subTypes = new Text(filterComp, SWT.BORDER);
        ft_subTypes.setBounds(378, 223, 250, 21);
        
        ft_artist = new Text(filterComp, SWT.BORDER);
        ft_artist.setBounds(378, 247, 250, 21);
        
        Label filter_lbl_subTypes = new Label(filterComp, SWT.NONE);
        filter_lbl_subTypes.setText("Subtypes");
        filter_lbl_subTypes.setBounds(306, 226, 55, 15);
        
        Label filter_lbl_artist = new Label(filterComp, SWT.NONE);
        filter_lbl_artist.setText("Artist");
        filter_lbl_artist.setBounds(306, 250, 55, 15);
        
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
        filter_lbl_power.setBounds(306, 274, 66, 15);
        
        ft_power = new Text(filterComp, SWT.BORDER);
        ft_power.setBounds(378, 271, 250, 21);
        
        Label filter_lbl_toughness = new Label(filterComp, SWT.NONE);
        filter_lbl_toughness.setText("Toughness");
        filter_lbl_toughness.setBounds(306, 298, 66, 15);
        
        ft_toughness = new Text(filterComp, SWT.BORDER);
        ft_toughness.setBounds(378, 295, 250, 21);
        
        Label filter_lbl_header_generic = new Label(filterComp, SWT.CENTER);
        filter_lbl_header_generic.setBounds(82, 10, 250, 15);
        filter_lbl_header_generic.setText("Generic");
        
        Label filter_lbl_header_specific = new Label(filterComp, SWT.CENTER);
        filter_lbl_header_specific.setBounds(378, 130, 250, 15);
        filter_lbl_header_specific.setText("Specific");
        
        Label filter_lbl_supertypes = new Label(filterComp, SWT.NONE);
        filter_lbl_supertypes.setText("Supertypes");
        filter_lbl_supertypes.setBounds(306, 178, 66, 15);
        
        ft_superTypes = new Text(filterComp, SWT.BORDER);
        ft_superTypes.setText("Legendary");
        ft_superTypes.setBounds(378, 175, 250, 21);
        
        Group grpPresets = new Group(filterComp, SWT.NONE);
        grpPresets.setText("Presets");
        grpPresets.setBounds(6, 337, 953, 314);
        
        fdrop_set = new Combo(grpPresets, SWT.NONE);
        fdrop_set.setBounds(78, 33, 252, 23);
        
        Label fl_drop_set = new Label(grpPresets, SWT.NONE);
        fl_drop_set.setBounds(10, 39, 62, 15);
        fl_drop_set.setText("Set");
        
        Label fl_drop_types = new Label(grpPresets, SWT.NONE);
        fl_drop_types.setText("Types");
        fl_drop_types.setBounds(10, 93, 62, 15);
        
        fdrop_types = new Combo(grpPresets, SWT.NONE);
        fdrop_types.setBounds(78, 89, 252, 23);
        
        Label fl_drop_supertypes = new Label(grpPresets, SWT.NONE);
        fl_drop_supertypes.setText("Supertypes");
        fl_drop_supertypes.setBounds(10, 147, 62, 15);
        
        fdrop_superTypes = new Combo(grpPresets, SWT.NONE);
        fdrop_superTypes.setBounds(78, 145, 252, 23);
        
        Label fl_drop_subTypes = new Label(grpPresets, SWT.NONE);
        fl_drop_subTypes.setText("Subtypes");
        fl_drop_subTypes.setBounds(10, 201, 62, 15);
        
        fdrop_subTypes = new Combo(grpPresets, SWT.NONE);
        fdrop_subTypes.setBounds(78, 201, 252, 23);
        
        fset_add = new Button(grpPresets, SWT.NONE);
        fset_add.setBounds(336, 31, 40, 25);
        fset_add.setText("Add");
        
        fset_set = new Button(grpPresets, SWT.NONE);
        fset_set.setText("Set");
        fset_set.setBounds(382, 31, 40, 25);
        
        ftypes_add = new Button(grpPresets, SWT.NONE);
        ftypes_add.setText("Add");
        ftypes_add.setBounds(336, 87, 40, 25);
        
        ftypes_set = new Button(grpPresets, SWT.NONE);
        ftypes_set.setText("Set");
        ftypes_set.setBounds(382, 87, 40, 25);
        
        fsuperTypes_add = new Button(grpPresets, SWT.NONE);
        fsuperTypes_add.setText("Add");
        fsuperTypes_add.setBounds(336, 143, 40, 25);
        
        fsuperTypes_set = new Button(grpPresets, SWT.NONE);
        fsuperTypes_set.setText("Set");
        fsuperTypes_set.setBounds(382, 143, 40, 25);
        
        fsubTypes_add = new Button(grpPresets, SWT.NONE);
        fsubTypes_add.setText("Add");
        fsubTypes_add.setBounds(336, 199, 40, 25);
        
        fsubTypes_set = new Button(grpPresets, SWT.NONE);
        fsubTypes_set.setText("Set");
        fsubTypes_set.setBounds(382, 199, 40, 25);
        
        fdrop_rarity = new Combo(grpPresets, SWT.NONE);
        fdrop_rarity.setBounds(78, 257, 252, 23);
        
        Label fl_drop_rarity = new Label(grpPresets, SWT.NONE);
        fl_drop_rarity.setText("Rarity");
        fl_drop_rarity.setBounds(10, 255, 62, 15);
        
        frarity_set = new Button(grpPresets, SWT.NONE);
        frarity_set.setText("Set");
        frarity_set.setBounds(336, 255, 86, 25);
        
        Group grpQuery = new Group(filterComp, SWT.NONE);
        grpQuery.setText("Query");
        grpQuery.setBounds(10, 151, 278, 114);
        
        filter_lbl_header_note1 = new Label(grpQuery, SWT.NONE);
        filter_lbl_header_note1.setAlignment(SWT.CENTER);
        filter_lbl_header_note1.setBounds(10, 37, 258, 15);
        filter_lbl_header_note1.setText("Tip: Fields can be blank!");
        
        filter_btn_fetch = new Button(grpQuery, SWT.NONE);
        filter_btn_fetch.setBounds(10, 71, 116, 25);
        filter_btn_fetch.setText("Fetch Results");
        
        filter_btn_clear = new Button(grpQuery, SWT.NONE);
        filter_btn_clear.setBounds(152, 71, 116, 25);
        filter_btn_clear.setText("Clear Fields");
        
        queryProgressBar = new ProgressBar(grpQuery, SWT.INDETERMINATE | SWT.SMOOTH | SWT.HORIZONTAL);
        queryProgressBar.setBounds(10, 79, 258, 17);
        queryProgressBar.setVisible(false);
        
        filter_lbl_thinkTime = new Label(grpQuery, SWT.SHADOW_NONE | SWT.CENTER);
        filter_lbl_thinkTime.setBounds(39, 58, 193, 15);
        filter_lbl_thinkTime.setText("[Time Passed]");
        filter_lbl_thinkTime.setAlignment(SWT.CENTER);
        filter_lbl_thinkTime.setVisible(false);
        
        filter_lbl_progressBar = new Label(grpQuery, SWT.SHADOW_NONE | SWT.CENTER);
        filter_lbl_progressBar.setBounds(39, 37, 193, 15);
        filter_lbl_progressBar.setAlignment(SWT.CENTER);
        filter_lbl_progressBar.setText("Querying mtg-json.com DB...");
        filter_lbl_progressBar.setVisible(false);
        
        styledWarning = new StyledText(filterComp, SWT.BORDER | SWT.WRAP);
        styledWarning.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
        styledWarning.setSelectionForeground(SWTResourceManager.getColor(255, 0, 0));
        styledWarning.setText("[WARNING] You are doing a generic query which can return thousands of cards, this can take a very long time to finish!");
        styledWarning.setBounds(10, 274, 278, 59);
        styledWarning.setVisible(false);
        
        previewBuildWarning = new StyledText(filterComp, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        previewBuildWarning.setSelectionForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        previewBuildWarning.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
        previewBuildWarning.setText("NOTE: This is a preview build of an application still in development, if you find any bugs please post them here https://github.com/nossr50/mtg-json-tool/issues");
        previewBuildWarning.setBounds(607, 10, 352, 64);
        previewBuildWarning.setSelection(0, 4);
        
        //queryProgressBar.setMaximum(5);
        //queryProgressBar.setSelection(1);
        
        tabResults = new TabItem(tabFolder, SWT.NONE);
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
        rt_btn_export.setText("Full Export");
        
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
        
        listener_simple_export = new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    simpleExportButtonPressed();
                    break;
                }
                }
            }
        };
        
        listener_custom_export_flags = new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    customExportFlagsChanged();
                    break;
                }
                }
            }
        };
        
        listener_custom_export = new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    customExportButtonPressed();
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
        
        listener_clear = new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    clearButtonPressed();
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
        
        rt_rulings = new Text(grpCard, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        rt_rulings.setBounds(236, 223, 322, 93);
        
        Label result_lbl_cardRulings = new Label(grpCard, SWT.NONE);
        result_lbl_cardRulings.setText("Rulings / Legalities");
        result_lbl_cardRulings.setBounds(237, 205, 321, 15);
        
        tbtmAdvancedExport = new TabItem(tabFolder, SWT.NONE);
        tbtmAdvancedExport.setText("Advanced Export");
        
        Composite composite = new Composite(tabFolder, SWT.NONE);
        tbtmAdvancedExport.setControl(composite);
        
        Group grpPresets_1 = new Group(composite, SWT.NONE);
        grpPresets_1.setText("Presets");
        grpPresets_1.setBounds(10, 10, 166, 106);
        
        Label lblSimpleExportOnly = new Label(grpPresets_1, SWT.WRAP);
        lblSimpleExportOnly.setBounds(10, 54, 147, 49);
        lblSimpleExportOnly.setText("Simple Export only dumps card names from a query.");
        
        btnSimpleExport = new Button(grpPresets_1, SWT.NONE);
        btnSimpleExport.setBounds(10, 23, 147, 25);
        btnSimpleExport.setText("Simple Export");
        
        Group grpCustomExport = new Group(composite, SWT.NONE);
        grpCustomExport.setText("Custom Export");
        grpCustomExport.setBounds(187, 10, 590, 197);
        
        btnCardName = new Button(grpCustomExport, SWT.CHECK);
        btnCardName.setBounds(10, 52, 93, 16);
        btnCardName.setSelection(true);
        btnCardName.setText("Card Name");
        
        btnSetName = new Button(grpCustomExport, SWT.CHECK);
        btnSetName.setBounds(10, 74, 93, 16);
        btnSetName.setText("Set Name");
        btnSetName.setSelection(true);
        
        btnSupertypes = new Button(grpCustomExport, SWT.CHECK);
        btnSupertypes.setBounds(109, 52, 93, 16);
        btnSupertypes.setText("Supertypes");
        btnSupertypes.setSelection(true);
        
        btnTypes = new Button(grpCustomExport, SWT.CHECK);
        btnTypes.setBounds(109, 74, 93, 16);
        btnTypes.setText("Types");
        btnTypes.setSelection(true);
        
        btnSubtypes = new Button(grpCustomExport, SWT.CHECK);
        btnSubtypes.setBounds(109, 96, 93, 16);
        btnSubtypes.setText("Subtypes");
        btnSubtypes.setSelection(true);
        
        btnColors = new Button(grpCustomExport, SWT.CHECK);
        btnColors.setBounds(208, 52, 78, 16);
        btnColors.setText("Colors");
        btnColors.setSelection(true);
        
        btnCmc = new Button(grpCustomExport, SWT.CHECK);
        btnCmc.setBounds(208, 74, 78, 16);
        btnCmc.setText("CMC");
        btnCmc.setSelection(true);
        
        btnPower = new Button(grpCustomExport, SWT.CHECK);
        btnPower.setBounds(208, 96, 78, 16);
        btnPower.setText("Power");
        btnPower.setSelection(true);
        
        btnToughness = new Button(grpCustomExport, SWT.CHECK);
        btnToughness.setBounds(208, 118, 78, 16);
        btnToughness.setText("Toughness");
        btnToughness.setSelection(true);
        
        btnRarity = new Button(grpCustomExport, SWT.CHECK);
        btnRarity.setBounds(208, 140, 78, 16);
        btnRarity.setText("Rarity");
        btnRarity.setSelection(true);
        
        btnLegalities = new Button(grpCustomExport, SWT.CHECK);
        btnLegalities.setBounds(307, 114, 93, 16);
        btnLegalities.setText("Legalities");
        
        btnArtist = new Button(grpCustomExport, SWT.CHECK);
        btnArtist.setBounds(307, 92, 93, 16);
        btnArtist.setText("Artist");
        
        btnFlavourText = new Button(grpCustomExport, SWT.CHECK);
        btnFlavourText.setBounds(307, 70, 93, 16);
        btnFlavourText.setText("Flavour Text");
        
        btnCardText = new Button(grpCustomExport, SWT.CHECK);
        btnCardText.setBounds(307, 48, 93, 16);
        btnCardText.setText("Card Text");
        
        btnHeaders = new Button(grpCustomExport, SWT.CHECK);
        btnHeaders.setBounds(406, 48, 147, 16);
        btnHeaders.setText("Headers between cards");
        btnHeaders.setSelection(true);
        
        btnLabelBasics = new Button(grpCustomExport, SWT.CHECK);
        btnLabelBasics.setBounds(406, 70, 147, 16);
        btnLabelBasics.setText("Label Basics");
        btnLabelBasics.setSelection(true);
        
        btnCustomExport = new Button(grpCustomExport, SWT.NONE);
        btnCustomExport.setBounds(109, 162, 291, 25);
        btnCustomExport.setText("Custom Export");
        
        rt_btn_export.addListener(SWT.Selection, listener_export);
        btnCustomExport.addListener(SWT.Selection, listener_custom_export);
        filter_btn_clear.addListener(SWT.Selection, listener_clear);
        filter_btn_fetch.addListener(SWT.Selection, listener_fetch);
        
        styledWarning.setSelection(0, 9);
        styledWarning.update();
        
        lblStyle = new Label(grpCustomExport, SWT.CENTER);
        lblStyle.setBounds(406, 26, 140, 15);
        lblStyle.setText("Style");
        
        lblFluff = new Label(grpCustomExport, SWT.CENTER);
        lblFluff.setBounds(307, 26, 93, 15);
        lblFluff.setText("Extras");
        
        lblStats = new Label(grpCustomExport, SWT.CENTER);
        lblStats.setBounds(109, 27, 177, 15);
        lblStats.setText("Stats");
        
        lblBasic = new Label(grpCustomExport, SWT.CENTER);
        lblBasic.setBounds(10, 26, 93, 15);
        lblBasic.setText("Basic");
        
        btnLabelStats = new Button(grpCustomExport, SWT.CHECK);
        btnLabelStats.setText("Label Stats");
        btnLabelStats.setSelection(true);
        btnLabelStats.setBounds(406, 92, 147, 16);
        
        btnLabelExtras = new Button(grpCustomExport, SWT.CHECK);
        btnLabelExtras.setText("Label Extras");
        btnLabelExtras.setSelection(true);
        btnLabelExtras.setBounds(406, 114, 147, 16);
        
        btnSpacingExtras = new Button(grpCustomExport, SWT.CHECK);
        btnSpacingExtras.setText("Empty lines between Extras");
        btnSpacingExtras.setSelection(true);
        btnSpacingExtras.setBounds(406, 140, 163, 16);
        
        exportExample = new ExportExample();
        
        customExportPreview = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
        customExportPreview.setBounds(10, 234, 949, 417);
        customExportPreview.setText(exportExample.getExample(getExportFlags(), getStyleFlags()));
        
        lblCustomExportPreview = new Label(composite, SWT.NONE);
        lblCustomExportPreview.setBounds(10, 213, 590, 15);
        lblCustomExportPreview.setText("Custom Export Preview");
        
        tbtmImport = new TabItem(tabFolder, SWT.NONE);
        tbtmImport.setText("Import");
        
        Composite composite_1 = new Composite(tabFolder, SWT.NONE);
        tbtmImport.setControl(composite_1);
        
        Label lblComingSoon = new Label(composite_1, SWT.NONE);
        lblComingSoon.setBounds(394, 297, 181, 15);
        lblComingSoon.setText("Coming Soon...");
        
        Button btnImportMtgaDeck = new Button(composite_1, SWT.NONE);
        btnImportMtgaDeck.setBounds(394, 318, 181, 25);
        btnImportMtgaDeck.setText("Import MTGA Deck");
        
        btnImportMtgaDeck.setEnabled(false);
        
        initButtons();
        initPresets();
        addButtonListeners();
        
        //Finally set all widgets to a default state
        setState(AppState.IDLE);
        updateWidgets();
    }
    
    private void initButtons()
    {
        rt_btn_export.setEnabled(false);
        btnCustomExport.setEnabled(false);
        btnSimpleExport.setEnabled(false);
    }
    
    private void addButtonListeners()
    {
        listener_drop_set_add = getNewPresetButtonListener(FieldType.SETNAME, ButtonType.ADD);
        listener_drop_set_set = getNewPresetButtonListener(FieldType.SETNAME, ButtonType.SET);
        
        listener_drop_types_add = getNewPresetButtonListener(FieldType.TYPES, ButtonType.ADD);
        listener_drop_types_set = getNewPresetButtonListener(FieldType.TYPES, ButtonType.SET);
        
        listener_drop_superTypes_add = getNewPresetButtonListener(FieldType.SUPERTYPES, ButtonType.ADD);
        listener_drop_superTypes_set = getNewPresetButtonListener(FieldType.SUPERTYPES, ButtonType.SET);
        
        listener_drop_subTypes_add = getNewPresetButtonListener(FieldType.SUBTYPES, ButtonType.ADD);
        listener_drop_subTypes_set = getNewPresetButtonListener(FieldType.SUBTYPES, ButtonType.SET);
        
        listener_drop_rarity_set = getNewPresetButtonListener(FieldType.RARITY, ButtonType.SET);
        
        fset_add.addListener(SWT.Selection, listener_drop_set_add);
        fset_set.addListener(SWT.Selection, listener_drop_set_set);
        
        ftypes_add.addListener(SWT.Selection, listener_drop_types_add);
        ftypes_set.addListener(SWT.Selection, listener_drop_types_set);
        
        fsuperTypes_add.addListener(SWT.Selection, listener_drop_superTypes_add);
        fsuperTypes_set.addListener(SWT.Selection, listener_drop_superTypes_set);
        
        fsubTypes_add.addListener(SWT.Selection, listener_drop_subTypes_add);
        fsubTypes_set.addListener(SWT.Selection, listener_drop_subTypes_set);
        
        frarity_set.addListener(SWT.Selection, listener_drop_rarity_set);
        
        btnLabelBasics.addListener(SWT.Selection, listener_custom_export_flags);
        btnLabelStats.addListener(SWT.Selection, listener_custom_export_flags);
        btnLabelExtras.addListener(SWT.Selection, listener_custom_export_flags);
        btnHeaders.addListener(SWT.Selection, listener_custom_export_flags);
        btnCardText.addListener(SWT.Selection, listener_custom_export_flags);
        btnFlavourText.addListener(SWT.Selection, listener_custom_export_flags);
        btnArtist.addListener(SWT.Selection, listener_custom_export_flags);
        btnLegalities.addListener(SWT.Selection, listener_custom_export_flags);
        btnToughness.addListener(SWT.Selection, listener_custom_export_flags);
        btnPower.addListener(SWT.Selection, listener_custom_export_flags);
        btnCmc.addListener(SWT.Selection, listener_custom_export_flags);
        btnSubtypes.addListener(SWT.Selection, listener_custom_export_flags);
        btnSupertypes.addListener(SWT.Selection, listener_custom_export_flags);
        btnTypes.addListener(SWT.Selection, listener_custom_export_flags);
        btnSetName.addListener(SWT.Selection, listener_custom_export_flags);
        btnCardName.addListener(SWT.Selection, listener_custom_export_flags);
        btnColors.addListener(SWT.Selection, listener_custom_export_flags);
        btnSpacingExtras.addListener(SWT.Selection, listener_custom_export_flags);
        
        btnSimpleExport.addListener(SWT.Selection, listener_simple_export);
    }
    
    private Listener getNewPresetButtonListener(FieldType ft, ButtonType bt)
    {
        return new Listener() {
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.Selection:
                {
                    presetButtonPressed(ft, bt);
                    break;
                }
                }
            }
        };
    }
    
    private void presetButtonPressed(FieldType ft, ButtonType bt)
    {
        Combo thisDropDown = getDropDown(ft);
        String newText = thisDropDown.getText();
        
        if(newText != null && !newText.isEmpty())
        {
            switch(bt)
            {
            case ADD:
                addText(newText, getTextField(ft));
                break;
            case SET:
                setText(newText, getTextField(ft));
                break;
            default:
                break;
            }
        }
    }
    
    private Text getTextField(FieldType ft)
    {
        switch(ft)
        {
        case SETNAME:
            return ft_setName;
        case SUBTYPES:
            return ft_subTypes;
        case SUPERTYPES:
            return ft_superTypes;
        case TYPES:
            return ft_types;
        case RARITY:
            return ft_rarity;
        default:
            System.out.println("Text field type not supported! Fix this!");
            return null;
        }
    }
    
    private void setText(String newText, Text textField)
    {
        textField.setText(newText);
        textField.update();
    }
    
    private void addText(String newText, Text textField)
    {
        if(textField.getText().length() < 1)
        {
            setText(newText, textField);
        } else {
            textField.setText(textField.getText()+", "+newText);
            textField.update();
        }
    }
    
    private Combo getDropDown(FieldType ft)
    {
        switch(ft)
        {
        case SETNAME:
            return fdrop_set;
        case SUBTYPES:
            return fdrop_subTypes;
        case SUPERTYPES:
            return fdrop_superTypes;
        case TYPES:
            return fdrop_types;
        case RARITY:
            return fdrop_rarity;
        default:
            System.out.println("Drop down type not supported! Fix this!");
            return null;
        }
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
        
        for(String s : CardAPI.getAllCardSupertypes())
        {
            fdrop_superTypes.add(s.toString());
        }
        
        for(String s : CardAPI.getAllCardSubtypes())
        {
            fdrop_subTypes.add(s.toString());
        }
        
        //Rarities
        fdrop_rarity.add("Uncommon");
        fdrop_rarity.add("Common");
        fdrop_rarity.add("Rare");
        fdrop_rarity.add("Mythic Rare");
        fdrop_rarity.add("Special");
        
        
        fdrop_types.update();
        fdrop_set.update();
        fdrop_superTypes.update();
        fdrop_subTypes.update();
        fdrop_rarity.update();
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
            
            //Rulings & Legalities
            if(curCard.getRulings() != null && curCard.getRulings().length > 0)
                updateResultField(rt_rulings, curCard.getRulings());
            else
                updateResultField(rt_rulings, ""); //Empty it
            
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
    
    private void updateResultField(Text resultField, Ruling[] rulings)
    {
        String newString = "";
        
        for(int a = 0; a < rulings.length; a++)
        {
            String curString = rulings[a].getText();
            
            for(int x = 0; x < curString.length(); x++)
            {
                newString+=curString.toCharArray()[x];
            }
        }
        
        resultField.setText(newString);
        resultField.update();
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
    
    private void simpleExportButtonPressed()
    {
        if(rt_btn_export.isEnabled())
        {
            //Executes the Export script in a new thread
            ExportThread et = new ExportThread(results, ExportType.SIMPLE, ExportFlags.NAMES, StyleFlags.NOFLAGS);
            Thread thread = new Thread(et);

            thread.start();
        }
    }
    
    private void exportButtonPressed()
    {
        if(rt_btn_export.isEnabled())
        {
            //Executes the Export script in a new thread
            ExportThread et = new ExportThread(results, ExportType.STANDARD, ExportFlags.NOFLAGS, StyleFlags.NOFLAGS);
            Thread thread = new Thread(et);

            thread.start();
        }
    }
    
    private void customExportButtonPressed()
    {
        if(btnCustomExport.isEnabled())
        {
            //Executes the Export script in a new thread with custom flags
            ExportThread et = new ExportThread(results, ExportType.CUSTOM, getExportFlags(), getStyleFlags());
            Thread thread = new Thread(et);

            thread.start();
        }
    }
    
    private void customExportFlagsChanged()
    {
        customExportPreview.setText(exportExample.getExample(getExportFlags(), getStyleFlags()));
        customExportPreview.update();
    }
    
    private int getExportFlags()
    {
        int flags = ExportFlags.NOFLAGS;
        
        if(btnCardText.getSelection())
            flags = flags | ExportFlags.CARD_TEXT;
        
        if(btnFlavourText.getSelection())
            flags = flags | ExportFlags.FLAVOUR_TEXT;
        
        if(btnArtist.getSelection())
            flags = flags | ExportFlags.ARTIST;
        
        if(btnLegalities.getSelection())
            flags = flags | ExportFlags.LEGALITIES;
        
        if(btnRarity.getSelection())
            flags = flags | ExportFlags.RARITY;
        
        if(btnToughness.getSelection())
            flags = flags | ExportFlags.TOUGHNESS;
        
        if(btnPower.getSelection())
            flags = flags | ExportFlags.POWER;
        
        if(btnCmc.getSelection())
            flags = flags | ExportFlags.CMC;
        
        if(btnTypes.getSelection())
            flags = flags | ExportFlags.TYPES;
        
        if(btnSubtypes.getSelection())
            flags = flags | ExportFlags.SUBTYPES;
        
        if(btnSupertypes.getSelection())
            flags = flags | ExportFlags.SUPERTYPES;
        
        if(btnSetName.getSelection())
            flags = flags | ExportFlags.SETNAME;
        
        if(btnCardName.getSelection())
            flags = flags | ExportFlags.NAMES;
        
        if(btnColors.getSelection())
            flags = flags | ExportFlags.COLORS;
        
        return flags;
    }
    
    private int getStyleFlags()
    {
        int flags = 0;
        
        if(btnLabelBasics.getSelection())
            flags = flags | StyleFlags.PREFIX_BASIC;
        
        if(btnLabelStats.getSelection())
            flags = flags | StyleFlags.PREFIX_STATS;
        
        if(btnLabelExtras.getSelection())
            flags = flags | StyleFlags.PREFIX_EXTRA;
        
        if(btnSpacingExtras.getSelection())
            flags = flags | StyleFlags.SPACING_BETWEEN_EXTRAS;
        
        if(btnHeaders.getSelection())
            flags = flags | StyleFlags.HEADERS;
        
        return flags;
    }
    
    private void clearButtonPressed()
    {
        if(filter_btn_clear.isEnabled())
        {
            ft_cardName.setText("");
            ft_types.setText("");
            ft_superTypes.setText("");
            ft_subTypes.setText("");
            ft_artist.setText("");
            ft_setName.setText("");
            ft_colors.setText("");
            ft_rarity.setText("");
            ft_cmc.setText("");
            ft_toughness.setText("");
            ft_power.setText("");
            
            ft_cardName.update();
            ft_types.update();
            ft_superTypes.update();
            ft_subTypes.update();
            ft_artist.update();
            ft_setName.update();
            ft_colors.update();
            ft_rarity.update();
            ft_cmc.update();
            ft_toughness.update();
            ft_power.update();
        }
    }
    
    private void fetchButtonPressed()
    {
        if(filter_btn_fetch.isEnabled())
        {
            //Executes the Query in a new thread
            QueryThread qt = new QueryThread(getFilters());
            Thread thread = new Thread(qt);

            thread.start();
        }
    }
    
    synchronized private static void disableButtons()
    {
        filter_btn_fetch.setEnabled(false);
        filter_btn_clear.setEnabled(false);
        rt_btn_export.setEnabled(false);
        btnCustomExport.setEnabled(false);
        btnSimpleExport.setEnabled(false);
        
        filter_btn_fetch.update();
        rt_btn_export.update();
        filter_btn_clear.update();
        btnCustomExport.update();
    }
    
    synchronized private static void enableButtons()
    {
        filter_btn_clear.setEnabled(true);
        
        if(curState == AppState.IDLE || curState == AppState.FINISHED)
        {
            filter_btn_fetch.setEnabled(true);
        }
        
        if(curState == AppState.FINISHED)
        {
            rt_btn_export.setEnabled(true);
            btnCustomExport.setEnabled(true);
            btnSimpleExport.setEnabled(true);
        }
        
        
        filter_btn_fetch.update();
        filter_btn_clear.update();
        rt_btn_export.update();
        btnCustomExport.update();
    }
    
    synchronized public static void asyncEnableButtons()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                enableButtons();
            }
        });
    }
    
    synchronized public static void asyncDisableButtons()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                disableButtons();
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
        addFilterMulti(newFilters, "supertypes=", ft_superTypes);
        
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
            //System.out.println("Added filter: "+filterPrefix+textFromField);
        } else {
            //System.out.println("No filter for field.");
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
            //System.out.println("Added filter: "+filterPrefix+newText);
        } else {
            //System.out.println("No filter for field.");
        }
    }
    
    synchronized public static void setState(AppState newState)
    {
        curState = newState;
        updateWidgets(); //Everytime the state changes we update the widgets
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
        DialogThread dt = new DialogThread(dialogTxt);
        Thread thread = new Thread(dt);

        thread.start();
    }
    
    /**
     * Updates widgets based on the current AppState
     */
    synchronized public static void updateWidgets()
    {
        switch(curState)
        {
        case FINISHED:
            asyncToggleWarning(false); //Turn off the warning if its on
            asyncUpdateProgressBar(false);
            asyncEnableButtons();
            stopTimer();
            asyncUpdateResultTab();
            break;
        case IDLE:
            asyncUpdateProgressBar(false);
            //stopTimer(); //Maybe not a good idea
            asyncEnableButtons();
            break;
        case RUNNING:
            asyncUpdateProgressBar(true);
            asyncDisableButtons();
            startTimer();
            break;
        default:
            break;
        
        }
    }
    
    synchronized public static void asyncUpdateResultTab()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                tabResults.setText("Results ("+numResults+")");
                tabFolder.update();
            }
        });
    }
    
    synchronized public static void asyncToggleWarning(boolean toggle)
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                styledWarning.setVisible(toggle);
                styledWarning.update();
            }
        });
    }
    
    public static void asyncUpdateProgressBar(boolean visible)
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                queryProgressBar.setVisible(visible);
                queryProgressBar.update();
            }
        });
    }
    
    synchronized public static void startTimer()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                timer = new Timer("Query Timer", true);
                filter_lbl_thinkTime.setText("Query Time: "+secondsPassed+"s");
                
                filter_lbl_thinkTime.setVisible(true);
                filter_lbl_progressBar.setVisible(true);
                
                filter_btn_fetch.setVisible(false);
                filter_btn_clear.setVisible(false);
                filter_lbl_header_note1.setVisible(false);
                
                filter_btn_fetch.update();
                filter_btn_clear.update();
                filter_lbl_header_note1.update();
                
                
                filter_lbl_thinkTime.update();
                filter_lbl_progressBar.update();

                timer.schedule(new UpdateTimerTask(), 1000l, 1000l);  // Run once a second 
            }
        });
        
    }
    
    synchronized public static void asyncUpdateTimer()
    {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                secondsPassed+=1;
                filter_lbl_thinkTime.setText("Query Time: "+secondsPassed+"s");
                filter_lbl_thinkTime.update();
            }
        });
    }
    
    synchronized public static void stopTimer()
    {
        if(timer != null)
        {
            timer.cancel();
        }
        
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                secondsPassed=0;
                
                filter_lbl_thinkTime.setVisible(false);
                filter_lbl_progressBar.setVisible(false);
                
                filter_lbl_thinkTime.update();
                filter_lbl_progressBar.update();
                
                filter_btn_fetch.setVisible(true);
                filter_btn_clear.setVisible(true);
                filter_lbl_header_note1.setVisible(true);
                
                filter_btn_fetch.update();
                filter_btn_clear.update();
                filter_lbl_header_note1.update();
            }
        });
    }
    
    synchronized public static void setNumResults(int resultCount)
    {
        numResults = resultCount;
    }
}
