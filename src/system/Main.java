package system;

import Forms.*;
import Models.AgesModel;
import Models.MatchModel;
import Models.PlayerModel;
import Models.TeamModel;
import Utils.Consts;
import Utils.DatabaseFunctions;
import Utils.SharedModel;
import java.io.File;
import java.time.Clock;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {

    TableView<TeamModel> teams_table;
    TableView<PlayerModel> players_table;
    TableView<AgesModel> ages_table;
    TableView<PlayerModel> allplayers_table;
    Scene scene, team_scene, stats_scene, players_scene, match_scene;
    BorderPane root, team_root, stats_root;
    GridPane players_root;
    BorderPane match_root;
    int round = 1;

    ObservableList<MatchModel> matches;

    ObservableList<TeamModel> Data;

    private BoxBlur blur = new BoxBlur(6, 6, 2);

    @Override
    public void start(Stage primaryStage) {

        drawMainPage(primaryStage);

        primaryStage.setTitle("Egyptian Premier League Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
        

        primaryStage.setOnCloseRequest(value -> closeProgram(primaryStage));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void drawMainPage(Stage primaryStage) {
        root = new BorderPane();
        scene = new Scene(root, 1920, 1080);

        root.setId("mainroot");
        root.getStylesheets().add("Drawable/style.css");

        GridPane standing_layout = new GridPane();
        standing_layout.setId("standing_layout");
        GridPane matches_layout = new GridPane();
        matches_layout.setId("matches_layout");

        standing_layout.setPadding(new Insets(10, 10, 10, 10));
        standing_layout.setHgap(20);
        standing_layout.setVgap(40);

        matches_layout.setPadding(new Insets(10, 10, 10, 10));
        matches_layout.setHgap(20);
        matches_layout.setVgap(40);

        ImageView image = new ImageView("Drawable/nile.png");
        image.setId("image");
        image.setFitHeight(128);
        image.setFitWidth(128);

        Label standing_label = new Label("Egyptian Premier League Standings");
        standing_label.setId("standing_label");

        teams_table = new TableView<>();
        teams_table.setId("teams_table");
        teams_table.setMinHeight(0);
        teams_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        teams_table.getStylesheets().add("Drawable/table_style.css");

        TableColumn<TeamModel, Number> table_index = new TableColumn<>("#");
        table_index.setCellValueFactory(column
                -> new ReadOnlyObjectWrapper<>(teams_table.getItems().indexOf(column.getValue()) + 1)
        );
        table_index.setSortable(false);

        TableColumn<TeamModel, TeamModel> teamColumn = new TableColumn<>("Team");
        teamColumn.setMinWidth(200);
        teamColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        teamColumn.setCellFactory(TeamTableCell.forTableColumn());

        TableColumn table_played = new TableColumn<>("P");
        table_played.setCellValueFactory(new PropertyValueFactory<>("played"));


        TableColumn table_win = new TableColumn<>("Win");
        table_win.setMinWidth(50);
        table_win.setCellValueFactory(new PropertyValueFactory<>("win"));

        TableColumn table_draw = new TableColumn<>("Draw");
        table_draw.setMinWidth(50);
        table_draw.setCellValueFactory(new PropertyValueFactory<>("draw"));

        TableColumn table_lose = new TableColumn<>("Lose");
        table_lose.setMinWidth(50);
        table_lose.setCellValueFactory(new PropertyValueFactory<>("lose"));

        TableColumn table_gs = new TableColumn<>("GS");
        table_gs.setMinWidth(50);
        table_gs.setCellValueFactory(new PropertyValueFactory<>("gs"));

        TableColumn table_gc = new TableColumn<>("GC");
        table_gc.setMinWidth(50);
        table_gc.setCellValueFactory(new PropertyValueFactory<>("gc"));

        TableColumn table_dif = new TableColumn<>("+/-");
        table_dif.setMinWidth(50);
        table_dif.setCellValueFactory(new PropertyValueFactory<>("dif"));

        TableColumn table_points = new TableColumn<>("Points");
        table_points.setMinWidth(50);
        table_points.setCellValueFactory(new PropertyValueFactory<>("points"));

        teams_table.getColumns().addAll(table_index,teamColumn,
                table_played,
                table_win, table_draw, table_lose,
                table_gs, table_gc, table_dif,
                table_points);

        Data = FXCollections.observableArrayList();

        Data.setAll(DatabaseFunctions.orederTeams(Consts.POINTS));
        FilteredList<TeamModel> filteredData = new FilteredList<>(Data, p -> true);
        teams_table.setItems(filteredData);

        VBox header_layout = new VBox();
        HBox title_layout = new HBox();
        HBox order_layout = new HBox();
        HBox search_layout = new HBox();

        title_layout.setId("titlelayout");
        search_layout.setId("titlelayout");
        order_layout.setId("orderlayout");

        TextField searchField = new TextField();
        searchField.setPromptText("Search with Name or Id");
        searchField.setAlignment(Pos.CENTER);
        searchField.setFocusTraversable(false);
        searchField.setMinWidth(500);
        searchField.setMaxWidth(500);

        CheckBox points_box = new CheckBox("Order By Points");
        CheckBox scores_box = new CheckBox("Order By Scores");

        CheckBox held_box = new CheckBox("Show Held Matches");
        held_box.setStyle("-fx-text-fill: white;-fx-font-size: 16;-fx-font-weight: bold;");







        points_box.setSelected(true);
        scores_box.setSelected(false);

        points_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent value) {
                scores_box.setSelected(false);

                Data.setAll(DatabaseFunctions.orederTeams(Consts.POINTS));
                teams_table.setItems(filteredData);
            }
        });

        scores_box.setOnAction(value -> {
            points_box.setSelected(false);
            Data.setAll(DatabaseFunctions.orederTeams(Consts.SCORES));
            teams_table.setItems(filteredData);
        });

        GridPane.setConstraints(title_layout, 0, 0);
        GridPane.setConstraints(teams_table, 0, 1);



        // Header
        VBox contanier = new VBox();

        VBox header = new VBox();
        HBox header1 = new HBox();
        HBox header2 = new HBox();
        HBox header3 = new HBox();

        header.setId("header");

        ImageView previmage = new ImageView(new Image("Drawable/prev.png"));
        ImageView nextimage = new ImageView(new Image("Drawable/nxt.png"));

        previmage.setFitWidth(36);
        previmage.setFitHeight(36);

        nextimage.setFitWidth(36);
        nextimage.setFitHeight(36);

        Button prevButton = new Button("");
        prevButton.setGraphic(previmage);

        Button nextButton = new Button("");
        nextButton.setGraphic(nextimage);

        Label date_label = new Label("Select Date : ");
        DatePicker date_pick_btn = new DatePicker();

        ImageView statimage = new ImageView(new Image("Drawable/stat.png"));

        statimage.setFitWidth(64);
        statimage.setFitHeight(64);



        ImageView playerimage = new ImageView(new Image("Drawable/play.png"));

        playerimage.setFitWidth(64);
        playerimage.setFitHeight(64);


        round = 1;
        Label titleLabel = new Label("Round "+round);

        header1.getChildren().addAll(prevButton, titleLabel, nextButton);
        header1.setAlignment(Pos.CENTER);

        header2.getChildren().addAll(date_label,date_pick_btn);
        header2.setAlignment(Pos.CENTER);

        header3.getChildren().addAll(held_box);
        header3.setAlignment(Pos.CENTER);

        header.getChildren().addAll(header3 ,header2, header1);


        // ListView
        ListView<MatchModel> listView = new ListView<>();
        matches = FXCollections.observableArrayList();
        matches.setAll(DatabaseFunctions.getRoundMatches(round));
        listView.setItems(matches);
        listView.setCellFactory(param -> new MatchCell());
        listView.setId("list");

        listView.setMinHeight(listView.getItems().size() * 100);

        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                MatchModel selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    SharedModel.setSelected_match(selectedItem);
                    scene = null;
                    root = null;
                    drawMatchPage(primaryStage);
                    goPage(primaryStage, match_scene);
                }
            }
        });

        nextButton.setOnAction(e ->{

            if (round<34){
                round +=1;

                titleLabel.setText("Round "+round);
                matches.setAll(DatabaseFunctions.getRoundMatches(round));
                held_box.setSelected(false);


            }

        });

        prevButton.setOnAction(e ->{
            if (round >1){
                round-=1;
                titleLabel.setText("Round "+round);
                matches.setAll(DatabaseFunctions.getRoundMatches(round));
                held_box.setSelected(false);
            }
        });

        held_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent value) {
                if (held_box.isSelected()){
                    matches.setAll(DatabaseFunctions.getHieldMatches());
                }
                else{
                    matches.setAll(DatabaseFunctions.getRoundMatches(round));
                }

            }
        });

        contanier.setAlignment(Pos.CENTER);
        contanier.getChildren().addAll(header, listView);

        GridPane.setConstraints(contanier, 0, 0);

        matches_layout.getChildren().addAll(contanier);

        order_layout.getChildren().addAll(searchField, scores_box, points_box);
        header_layout.getChildren().addAll(order_layout);

        BorderPane topPane = new BorderPane();

        title_layout.getChildren().addAll(image, standing_label);
        standing_layout.getChildren().addAll(header_layout, teams_table);

        contanier.setPadding(new Insets(0,0,0 ,0));

        Menu file_menu = new Menu("New");
        Menu page_menu = new Menu("Pages");


        MenuItem players_menu = new MenuItem("Players");
        MenuItem stats_menu = new MenuItem("Statistics");



        MenuItem new_player = new MenuItem("New Player");
        MenuItem new_team = new MenuItem("New Team");
        MenuItem new_match = new MenuItem("New Match");


        file_menu.getItems().addAll(new_team,new_match,new_player);

        page_menu.getItems().addAll(players_menu,stats_menu);

        CreateTeamForm teamForm = new CreateTeamForm() {
            @Override
            public void afterClose() {
                Data.setAll(DatabaseFunctions.orederTeams(Consts.POINTS));
                primaryStage.getScene().getRoot().setEffect(null);
                teams_table.setItems(filteredData);
                points_box.setSelected(true);
                scores_box.setSelected(false);
            }
        };

        CreateMatchForm matchForm = new CreateMatchForm() {
            @Override
            public void afterClose() {
                primaryStage.getScene().getRoot().setEffect(null);
                matches.setAll(DatabaseFunctions.getAllMatches());
                listView.setItems(matches);
            }
        };

        CreatePlayerForm playerForm = new CreatePlayerForm() {
            @Override
            public void afterClose() {
                primaryStage.getScene().getRoot().setEffect(null);
            }
        };

        new_player.setOnAction(value -> {
            primaryStage.getScene().getRoot().setEffect(blur);
            playerForm.display();
        });
        new_team.setOnAction(value ->{
            primaryStage.getScene().getRoot().setEffect(blur);
            teamForm.display();
        } );
        new_match.setOnAction(value ->{
            primaryStage.getScene().getRoot().setEffect(blur);
            matchForm.display();
        } );

        MenuBar menu_bar = new MenuBar(file_menu,page_menu);
        menu_bar.setId("menu_bar");

        topPane.setLeft(title_layout);
        topPane.setRight(menu_bar);
        root.setTop(topPane);
        root.setLeft(standing_layout);
        root.setRight(matches_layout);

        teams_table.setRowFactory(tv -> {
            TableRow<TeamModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SharedModel.setSelected_team(row.getItem());
                    root =null;
                    scene =null;
                    drawTeamPage(primaryStage);
                    goPage(primaryStage, team_scene);
                }
            });
            return row;
        });

        stats_menu.setOnAction((ActionEvent value) -> {
            stats_root = new BorderPane();
            stats_scene = new Scene(stats_root, 1920, 1080);
            drawStatsPage(primaryStage);
            goPage(primaryStage, stats_scene);
        });


        players_menu.setOnAction((ActionEvent value) -> {
            players_root = new GridPane();
            players_scene = new Scene(players_root, 1920, 1080);
            drawPlayersPage(primaryStage);
            goPage(primaryStage, players_scene);

        });

        date_pick_btn.valueProperty().addListener(new ChangeListener<java.time.LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends java.time.LocalDate> observable, java.time.LocalDate oldValue, java.time.LocalDate newValue) {
                if (newValue != null) {
                    matches.setAll(DatabaseFunctions.getDateMatches(newValue.toString()));
                } else {
                    matches.setAll(DatabaseFunctions.getRoundMatches(round));
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(TeamModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (TeamModel.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(TeamModel.getId()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }

                return false;
            });
        });

        root.setOnMouseClicked(value -> removeTableSelection(teams_table, listView));
        standing_layout.setOnMouseClicked(value -> removeTableSelection(teams_table, listView));
        matches_layout.setOnMouseClicked(value -> removeTableSelection(teams_table, listView));

    }

    public void removeTableSelection(TableView table, ListView list) {
        if (!table.isHover()) {
            table.getSelectionModel().clearSelection();
        }

        if (list != null && !list.isHover()) {
            list.getSelectionModel().clearSelection();
        }
    }

    public void drawTeamPage(Stage primaryStage) {

        team_root = new BorderPane();
        team_scene = new Scene(team_root, 1920, 1080);

        team_root.setId("team_root");
        team_root.getStylesheets().add("Drawable/style.css");

        VBox main_layout = new VBox();
        main_layout.setId("main_layout");



        VBox team_header_layout = new VBox();
        team_header_layout.setId("team_header_layout");



        VBox Team_layout = new VBox();
        Team_layout.setId("standing_layout");

        VBox standings_layout = new VBox();
        standings_layout.setPadding(new Insets(20));
        standings_layout.setSpacing(20);
        Team_layout.setId("standing_layout");


        HBox top_layout = new HBox();
        top_layout.setId("toplayout");


        Team_layout.setPadding(new Insets(10, 10, 10, 10));

        File file = new File(SharedModel.getSelected_team().getImage());
        Image image = new Image(file.toURI().toString());

        ImageView Teamlogo = new ImageView(image);
        Teamlogo.setId("image");
        Teamlogo.setFitHeight(70);
        Teamlogo.setFitWidth(70);

        VBox info_layout = TeamInfoDesign.display();

        Label Team_label = new Label(SharedModel.getSelected_team().getName());
        Team_label.setStyle("-fx-text-fill: white;");
        ImageView backimage = new ImageView(new Image("Drawable/back.png"));

        backimage.setFitWidth(50);
        backimage.setFitHeight(50);

        Button back_btn = new Button("");
        back_btn.setOnAction(value -> {
            team_root = null;
            team_scene = null;
            drawMainPage(primaryStage);
            goPage(primaryStage, scene);
        });
        back_btn.setGraphic(backimage);
        back_btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        Label back_label = new Label("Back");
        back_label.setStyle("-fx-text-fill: white;");

        ImageView updateimage = new ImageView(new Image("Drawable/update.png"));

        updateimage.setFitWidth(50);
        updateimage.setFitHeight(50);

        Button update_team_btn = new Button();
        update_team_btn.setGraphic(updateimage);

        update_team_btn.setOnAction(value -> {
            primaryStage.getScene().getRoot().setEffect(blur);
            UpdateTeamForm form = new UpdateTeamForm() {
                @Override
                public void afterClose() {
                    primaryStage.getScene().getRoot().setEffect(null);
                    Team_label.setText(SharedModel.getSelected_team().getName());
                    team_root = null;
                    team_scene = null;
                    drawTeamPage(primaryStage);
                    goPage(primaryStage, team_scene);

                }
            };
            form.display(SharedModel.getSelected_team());
        });

        players_table = new TableView<>();
        players_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        players_table.getStylesheets().add("Drawable/table_style.css");
        players_table.setId("players_table");

        TableColumn table_name = new TableColumn<>("Player Name");
        table_name.setMinWidth(300);
        table_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn table_age = new TableColumn<>("Age");
        table_age.setMinWidth(50);
        table_age.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn table_no = new TableColumn<>("No");
        table_no.setMinWidth(50);
        table_no.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn table_type = new TableColumn<>("Position");
        table_type.setMinWidth(150);
        table_type.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn table_goals = new TableColumn<>("GS");
        table_goals.setMinWidth(50);
        table_goals.setCellValueFactory(new PropertyValueFactory<>("goals"));

        TableColumn table_rank = new TableColumn<>("Rank");
        table_rank.setMinWidth(50);
        table_rank.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn table_gc = new TableColumn<>("GC");
        table_gc.setMinWidth(50);
        table_gc.setCellValueFactory(new PropertyValueFactory<>("goals_c"));

        table_gc.setCellFactory(new Callback<TableColumn<PlayerModel, Integer>, TableCell<PlayerModel, Integer>>() {
            @Override
            public TableCell<PlayerModel, Integer> call(TableColumn<PlayerModel, Integer> param) {
                return new TableCell<PlayerModel, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else if (item == -1) {
                            setText("N/A");
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });

        TableColumn table_clean = new TableColumn<>("CS");
        table_clean.setMinWidth(50);
        table_clean.setCellValueFactory(new PropertyValueFactory<>("cleansheets"));

        table_clean.setCellFactory(new Callback<TableColumn<PlayerModel, Integer>, TableCell<PlayerModel, Integer>>() {
            @Override
            public TableCell<PlayerModel, Integer> call(TableColumn<PlayerModel, Integer> param) {
                return new TableCell<PlayerModel, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else if (item == -1) {
                            setText("N/A");
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });

        players_table.getColumns().addAll(table_name, table_age, table_type,
                table_no, table_goals, table_gc, table_clean, table_rank);

        players_table.getItems().addAll(DatabaseFunctions.getTeamPlayers(SharedModel.getSelected_team().getId()));

        HBox title_layout = new HBox();
        title_layout.setId("teamlayout");
        title_layout.getChildren().addAll(Teamlogo, Team_label, update_team_btn);

        top_layout.getChildren().addAll(back_btn, back_label);

        Team_layout.getChildren().addAll(players_table);
        standings_layout.getChildren().addAll(teams_table);


        CheckBox hield_box = new CheckBox("Held Matches");
        hield_box.setId("hield_box");

        hield_box.setSelected(false);



        // Header
        VBox contanier = new VBox();


        // ListView
        ListView<MatchModel> listView = new ListView<>();
        ObservableList<MatchModel> matches = FXCollections.observableArrayList();
        matches.setAll(DatabaseFunctions.getTeamMatches(SharedModel.getSelected_team().getId()));
        listView.setItems(matches);
        listView.setCellFactory(param -> new MatchCell());
        listView.setId("list");

        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                MatchModel selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    SharedModel.setSelected_match(selectedItem);
                    team_scene = null;
                    team_root = null;
                    drawMatchPage(primaryStage);
                    goPage(primaryStage, match_scene);
                }
            }
        });

        hield_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent value) {
                if (hield_box.isSelected()){
                    matches.setAll(DatabaseFunctions.getTeamHieldMatches(SharedModel.getSelected_team().getId()));
                }
                else{
                    matches.setAll(DatabaseFunctions.getTeamMatches(SharedModel.getSelected_team().getId()));
                }

            }
        });

        contanier.setId("contanier");

        contanier.setAlignment(Pos.CENTER);
        contanier.getChildren().addAll(hield_box, listView);



        TabPane tabPane = new TabPane();
        tabPane.setId("tabPane");

        // Create Tabs
        Tab overview = new Tab("Overview",info_layout);
        Tab table = new Tab("Table",standings_layout);
        Tab fixtures = new Tab("Fixtures",contanier);
        Tab squad = new Tab("Squad",Team_layout);

        tabPane.getTabs().addAll(overview,table,fixtures,squad);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        team_header_layout.getChildren().addAll(title_layout,tabPane);
        main_layout.getChildren().addAll(team_header_layout);

        team_root.setTop(top_layout);
        team_root.setCenter(main_layout);

    }

    public void drawMatchPage(Stage primaryStage) {

        match_root = new BorderPane();
        match_scene = new Scene(match_root, 1920, 1080);

        match_root.setId("match_root");
        match_root.getStylesheets().add("Drawable/style.css");


        HBox top_layout = new HBox();
        top_layout.setId("toplayout");

        ImageView backimage = new ImageView(new Image("Drawable/back.png"));

        backimage.setFitWidth(50);
        backimage.setFitHeight(50);

        Button back_btn = new Button("");

        back_btn.setOnAction(value
                -> {
            match_root = null;
            match_scene = null;
            primaryStage.getScene().getRoot().setEffect(null);
            drawMainPage(primaryStage);
            goPage(primaryStage, scene);

        }
        );
        back_btn.setGraphic(backimage);

        back_btn.setStyle(
                "-fx-background-color: transparent; -fx-padding: 0;");
        Label back_label = new Label("Back");
        back_label.setStyle("-fx-text-fill: white;");

        VBox main_layout = new VBox();

        Label date_label = new Label(SharedModel.getSelected_match().getDate());
        Label time_label = new Label(SharedModel.getSelected_match().getTime());
        Label round_label = new Label("Egyptian Premiere League 2023/2024 - Round " + SharedModel.getSelected_match().getMatch_round());
        Label team1_Label = new Label(DatabaseFunctions.getTeamName(SharedModel.getSelected_match().getHome_team()));
        Label team2_Label = new Label(DatabaseFunctions.getTeamName(SharedModel.getSelected_match().getAway_team()));
        Label stadium_Label = new Label(SharedModel.getSelected_match().getStadium_name());
        Label ref_Label = new Label(SharedModel.getSelected_match().getMatch_referee());
        Label result_label = new Label("");
        Label hieldstate = new Label("");

        if (SharedModel.getSelected_match().getIshield()>0){
            hieldstate.setText("Helded");
            hieldstate.setStyle("-fx-text-fill: red;");
        }

        Label played_label = new Label();
        played_label.setId("hield_label");

        Integer score1 = SharedModel.getSelected_match().getScoreTeam1();
        Integer score2 = SharedModel.getSelected_match().getScoreTeam2();

        File file1 = new File(DatabaseFunctions.getTeamImage(DatabaseFunctions.getTeamName(SharedModel.getSelected_match().getHome_team())));
        File file2 = new File(DatabaseFunctions.getTeamImage(DatabaseFunctions.getTeamName(SharedModel.getSelected_match().getAway_team())));

        Image image1 = new Image(file1.toURI().toString(), 128, 128, true, true);
        Image image2 = new Image(file2.toURI().toString(), 128, 128, true, true);

        ImageView imgview1 = new ImageView(image1);
        ImageView imgview2 = new ImageView(image2);

        ImageView stdimgview = new ImageView("Drawable/stdm.png");
        stdimgview.setFitHeight(30);
        stdimgview.setFitWidth(30);

        ImageView refimgview = new ImageView("Drawable/whistle.png");
        refimgview.setFitHeight(30);
        refimgview.setFitWidth(30);

        ImageView nileimgview = new ImageView("Drawable/nile.png");
        nileimgview.setFitHeight(100);
        nileimgview.setFitWidth(100);

        ImageView timeimgview = new ImageView("Drawable/clock.png");
        timeimgview.setFitHeight(30);
        timeimgview.setFitWidth(30);

        ImageView dateimgview = new ImageView("Drawable/clndr.png");
        dateimgview.setFitHeight(30);
        dateimgview.setFitWidth(30);

        ImageView updateimage = new ImageView(new Image("Drawable/update.png"));

        updateimage.setFitWidth(50);
        updateimage.setFitHeight(50);

        Button update_team_btn = new Button();
        update_team_btn.setGraphic(updateimage);

        update_team_btn.setOnAction(value -> {

            primaryStage.getScene().getRoot().setEffect(blur);

            UpdateMatchForm form = new UpdateMatchForm() {
                @Override
                public void afterClose() {
                    primaryStage.getScene().getRoot().setEffect(null);
                    match_root = null;
                    match_scene = null;
                    drawMatchPage(primaryStage);
                    goPage(primaryStage, match_scene);
                }
            };

            form.display(SharedModel.getSelected_match());
        });

        ImageView delayimage = new ImageView(new Image("Drawable/delay.png"));

        delayimage.setFitWidth(50);
        delayimage.setFitHeight(50);

        Button delay_btn = new Button();
        delay_btn.setGraphic(delayimage);

        delay_btn.setOnAction(actionEvent -> {
            primaryStage.getScene().getRoot().setEffect(blur);
            DelayMatchForm form = new DelayMatchForm() {
                @Override
                public void afterClose() {
                    primaryStage.getScene().getRoot().setEffect(null);
                    match_root = null;
                    match_scene = null;
                    drawMatchPage(primaryStage);
                    goPage(primaryStage, match_scene);
                }
            };
            form.display(SharedModel.getSelected_match());
        });

        HBox t1 = new HBox(team1_Label,imgview1,result_label);
        HBox t2 = new HBox(imgview2,team2_Label);

        VBox v1 = new VBox();
        VBox v2 = new VBox();

        VBox team1 = new VBox(t1,v1);
        VBox team2 = new VBox(t2,v2);



        //HBox result = new HBox(score1_label, score2_label);
        HBox layout1 = new HBox(nileimgview, round_label);
        HBox layout2 = new HBox(dateimgview, date_label, timeimgview, time_label , hieldstate);
        HBox layout3 = new HBox(team1,team2);
        HBox layout4 = new HBox();
        HBox layout5 = new HBox(refimgview, ref_Label, stdimgview, stadium_Label);

        for(String text : SharedModel.getSelected_match().getScorersNames_Team1()){
            v1.getChildren().add(new Label(text));
        }
        for(String text : SharedModel.getSelected_match().getScorersNames_Team2()){
            v2.getChildren().add(new Label(text));
        }
        

        if (SharedModel.getSelected_match().IsPlayed()) {
            played_label.setText("Ended");
            played_label.setId("nothield_label");
            result_label.setText(score1 + " - " + score2+"  ");
            layout4.getChildren().addAll(played_label);
        } else {
            played_label.setText("Not Played");
            result_label.setText(date_label.getText() + " - " + time_label.getText()+"  ");
            layout4.getChildren().addAll(played_label, update_team_btn,delay_btn);
        }

        VBox header = new VBox(layout2, layout1, layout3, layout4, layout5);
        header.setId("match_header_layout");

        layout1.setId("h_layout");
        layout2.setId("h_layout");
        layout4.setId("h_layout");
        layout5.setId("h_layout");
        layout3.setAlignment(Pos.CENTER);
        t2.setId("h_layout");
        t1.setId("h_layout");

        v1.setId("v_layout");
        v2.setId("v_layout");



        main_layout.getChildren().addAll(header);
        main_layout.setId("main_layout");

        top_layout.getChildren().addAll(back_btn, back_label);

        match_root.setTop(top_layout);
        match_root.setCenter(main_layout);

    }

    public void drawStatsPage(Stage primaryStage) {

        stats_root.setId("stats_root");
        stats_root.getStylesheets().add("Drawable/style.css");

        GridPane Team_layout = new GridPane();
        Team_layout.setId("ages_layout");
        Team_layout.setHgap(20);
        Team_layout.setVgap(40);

        HBox top_layout = new HBox();
        top_layout.setId("toplayout");

        ImageView backimage = new ImageView(new Image("Drawable/back.png"));

        backimage.setFitWidth(50);
        backimage.setFitHeight(50);

        Button back_btn = new Button("");
        back_btn.setOnAction(value -> {
            stats_root = null;
            stats_scene = null;
            goPage(primaryStage, scene);
        });
        back_btn.setGraphic(backimage);
        Label back_label = new Label("Back");
        back_label.setStyle("-fx-text-fill: white;");
        back_btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        ImageView logo = new ImageView("Drawable/age.png");
        logo.setId("image");
        logo.setFitHeight(70);
        logo.setFitWidth(70);

        Label age_label = new Label("AVG of Players ages");
        age_label.setStyle("-fx-text-fill: white;");

        ages_table = new TableView<>();
        ages_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ages_table.getStylesheets().add("Drawable/table_style.css");
        ages_table.setId("ages_table");

        TableColumn<AgesModel, Number> table_index = new TableColumn<>("#");
        table_index.setMinWidth(60);
        table_index.setMaxWidth(60);
        table_index.setCellValueFactory(column
                -> new ReadOnlyObjectWrapper<>(ages_table.getItems().indexOf(column.getValue()) + 1)
        );
        table_index.setSortable(false);

        TableColumn<AgesModel, AgesModel> teamColumn = new TableColumn<>("Team");
        teamColumn.setMinWidth(200);
        teamColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        teamColumn.setCellFactory(AgeTableCell.forTableColumn());



        TableColumn table_avg = new TableColumn<>("Age AVG");
        table_avg.setMinWidth(150);
        table_avg.setMaxWidth(150);
        table_avg.setCellValueFactory(new PropertyValueFactory<>("ages_avg"));

        ages_table.getColumns().addAll(table_index,teamColumn, table_avg);

        ages_table.getItems().addAll(DatabaseFunctions.getAgesAVG());

        top_layout.getChildren().addAll(back_btn, back_label);

        GridPane.setConstraints(logo, 1, 0);
        GridPane.setConstraints(age_label, 2, 0);
        GridPane.setConstraints(ages_table, 0, 1, 3, 1);
        Team_layout.getChildren().addAll(logo, age_label, ages_table);

        GridPane Stats_layout = new GridPane();

        HBox h1 = new HBox();
        HBox h2 = new HBox();

        VBox topScores_layout = TopStatDesign.display("Top 3 Scorers", "Drawable/goal.png", DatabaseFunctions.getTopThreeScorers());
        VBox topRank_layout = TopStatDesign.display("Top 3 Ranked", "Drawable/rank.png", DatabaseFunctions.getTopThreeRanked());
        VBox topCleen_layout = TopStatDesign.display("Top 3 CleanSheets", "Drawable/gloves.png", DatabaseFunctions.getTopThreeCleans());

        h1.getChildren().addAll(topScores_layout, topRank_layout);
        h2.getChildren().addAll(topCleen_layout);
        h2.setAlignment(Pos.CENTER);

        GridPane.setConstraints(h1, 0, 0);
        GridPane.setConstraints(h2, 0, 1);

        Stats_layout.getChildren().addAll(h1, h2);
        Stats_layout.setPadding(new Insets(100));

        stats_root.setTop(top_layout);
        stats_root.setLeft(Team_layout);
        stats_root.setCenter(Stats_layout);

    }

    public void drawPlayersPage(Stage primaryStage) {

        players_root.setId("stats_root");
        players_root.getStylesheets().add("Drawable/style.css");
        players_root.setId("players_root");

        GridPane players_layout = new GridPane();

        players_layout.setId("standing_layout");
        players_layout.setPadding(new Insets(10, 10, 10, 10));
        players_layout.setHgap(20);
        players_layout.setVgap(40);

        VBox details_layout = new VBox();
        details_layout.setId("details_layout");

        HBox top_layout = new HBox();
        top_layout.setId("toplayout");

        ImageView backimage = new ImageView(new Image("Drawable/back.png"));

        backimage.setFitWidth(50);
        backimage.setFitHeight(50);

        Button back_btn = new Button("");
        back_btn.setOnAction(value -> {
            players_root = null;
            players_scene = null;
            goPage(primaryStage, scene);
        });
        back_btn.setGraphic(backimage);
        back_btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        Label back_label = new Label("Back");
        back_label.setStyle("-fx-text-fill: white;");

        allplayers_table = new TableView<>();
        allplayers_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        allplayers_table.getStylesheets().add("Drawable/table_style.css");
        allplayers_table.setId("teams_table");

        TableColumn table_name = new TableColumn<>("Player Name");
        table_name.setMinWidth(300);
        table_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn table_age = new TableColumn<>("Age");
        table_age.setMinWidth(50);
        table_age.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<PlayerModel, String> table_team = new TableColumn<>("Player Team");
        table_team.setMinWidth(300);

        table_team.setCellValueFactory(cellData -> {
            Integer teamId = cellData.getValue().getTeam();
            String formattedTeamId = DatabaseFunctions.getTeamName(teamId);
            return new SimpleStringProperty(formattedTeamId);
        });

        TableColumn table_no = new TableColumn<>("No");
        table_no.setMinWidth(50);
        table_no.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn table_type = new TableColumn<>("Position");
        table_type.setMinWidth(150);
        table_type.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn table_goals = new TableColumn<>("GS");
        table_goals.setMinWidth(50);
        table_goals.setCellValueFactory(new PropertyValueFactory<>("goals"));

        TableColumn table_rank = new TableColumn<>("Rank");
        table_rank.setMinWidth(50);
        table_rank.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn table_gc = new TableColumn<>("GC");
        table_gc.setMinWidth(50);
        table_gc.setCellValueFactory(new PropertyValueFactory<>("goals_c"));

        table_gc.setCellFactory(new Callback<TableColumn<PlayerModel, Integer>, TableCell<PlayerModel, Integer>>() {
            @Override
            public TableCell<PlayerModel, Integer> call(TableColumn<PlayerModel, Integer> param) {
                return new TableCell<PlayerModel, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else if (item == -1) {
                            setText("N/A");
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });

        TableColumn table_clean = new TableColumn<>("CS");
        table_clean.setMinWidth(50);
        table_clean.setCellValueFactory(new PropertyValueFactory<>("cleansheets"));

        table_clean.setCellFactory(new Callback<TableColumn<PlayerModel, Integer>, TableCell<PlayerModel, Integer>>() {
            @Override
            public TableCell<PlayerModel, Integer> call(TableColumn<PlayerModel, Integer> param) {
                return new TableCell<PlayerModel, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else if (item == -1) {
                            setText("N/A");
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });

        allplayers_table.getColumns().addAll(table_name, table_age, table_type, table_no, table_team,
                table_goals, table_gc, table_clean, table_rank);

        ObservableList<PlayerModel> PlayerData = FXCollections.observableArrayList();
        PlayerData.setAll(DatabaseFunctions.getAllPlayers());

        FilteredList<PlayerModel> filteredData = new FilteredList<>(PlayerData, p -> true);

        allplayers_table.setItems(filteredData);

        ImageView logo = new ImageView("Drawable/play.png");
        logo.setId("image");
        logo.setFitHeight(70);
        logo.setFitWidth(70);

        Label title_label = new Label("EPL Players");
        title_label.setStyle("-fx-text-fill: white;");

        //details layout
        Label info_label = new Label("Player Information");
        Label infopos_label = new Label("Position Information");

        Label name_label = new Label("Player Name");

        Label age_label = new Label("Player Age : ");
        Label age_value = new Label("25");

        Label type_label = new Label("Player postion : ");
        Label type_value = new Label("postion");

        Label number_label = new Label("Player Number : ");
        Label number_value = new Label("10");

        Label team_label = new Label("Team : ");
        Label team_value = new Label("TeameName");

        Label gs_label = new Label("Goals Scored : ");
        Label gs_value = new Label("0");

        Label gc_label = new Label("Goals Conceced : ");
        Label gc_value = new Label("N/A");

        Label clean_label = new Label("Clean Sheets : ");
        Label clean_value = new Label("N/A");

        Label rank_label = new Label("Rank : ");
        Label rank_value = new Label("1000");

        Label pos_label = new Label("GoalKeeper");
        Label des_label = new Label("description of postion");
        des_label.setWrapText(true);
        des_label.setMaxWidth(450);

        TextField searchField = new TextField();
        searchField.setPromptText("Search with Name,Number or Team");
        searchField.setAlignment(Pos.CENTER);
        searchField.setFocusTraversable(false);
        searchField.setMinWidth(500);
        searchField.setMaxWidth(500);

        VBox data = new VBox();
        VBox data2 = new VBox();

        HBox layout1 = new HBox();
        HBox layout2 = new HBox();
        HBox layout3 = new HBox();
        HBox layout4 = new HBox();
        HBox layout5 = new HBox();
        HBox layout6 = new HBox();
        HBox layout7 = new HBox();
        HBox layout8 = new HBox();
        HBox layout9 = new HBox();
        HBox layout10 = new HBox();
        HBox layout11 = new HBox();
        HBox layout12 = new HBox();
        HBox layout13 = new HBox();

        ImageView updateimage = new ImageView(new Image("Drawable/update.png"));

        updateimage.setFitWidth(50);
        updateimage.setFitHeight(50);

        Button update_player_btn = new Button();
        update_player_btn.setGraphic(updateimage);

        update_player_btn.setOnAction(value -> {
            primaryStage.getScene().getRoot().setEffect(blur);
            UpdatePlayerForm form = new UpdatePlayerForm() {
                @Override
                public void afterClose() {
                    primaryStage.getScene().getRoot().setEffect(null);
                    PlayerData.setAll(DatabaseFunctions.getAllPlayers());
                }
            };
            form.display(SharedModel.getSelected_player());
        });

        layout6.setId("layout");
        layout11.setId("layout");
        data.setId("layout");
        data2.setId("layout");

        layout6.setAlignment(Pos.CENTER);
        layout1.setAlignment(Pos.CENTER);
        layout11.setAlignment(Pos.CENTER);
        layout12.setAlignment(Pos.CENTER);

        layout6.getChildren().addAll(info_label, update_player_btn);
        layout1.getChildren().addAll(name_label);
        layout2.getChildren().addAll(age_label, age_value);
        layout3.getChildren().addAll(type_label, type_value);
        layout4.getChildren().addAll(number_label, number_value);
        layout5.getChildren().addAll(team_label, team_value);

        layout11.getChildren().addAll(infopos_label);
        layout12.getChildren().addAll(pos_label);
        layout13.getChildren().addAll(des_label);

        layout7.getChildren().addAll(gs_label, gs_value);
        layout8.getChildren().addAll(gc_label, gc_value);
        layout9.getChildren().addAll(clean_label, clean_value);
        layout10.getChildren().addAll(rank_label, rank_value);

        data.getChildren().addAll(layout1, layout2, layout3, layout4, layout5, layout7, layout8, layout9, layout10);
        data2.getChildren().addAll(layout12, layout13);

        ImageView nochosenView = new ImageView("Drawable/picc.png");
        nochosenView.setFitHeight(512);
        nochosenView.setFitWidth(512);

        details_layout.getChildren().setAll(nochosenView);
        details_layout.setAlignment(Pos.CENTER);

        HBox searchlayout = new HBox();
        searchlayout.setId("search_layout");
        searchlayout.getChildren().addAll(logo, title_label, searchField);

        GridPane.setConstraints(logo, 0, 0);
        GridPane.setConstraints(allplayers_table, 0, 1);
        players_layout.getChildren().addAll(searchlayout, allplayers_table);

        top_layout.getChildren().addAll(back_btn, back_label);

        GridPane.setConstraints(top_layout, 0, 0, 2, 1);
        GridPane.setConstraints(players_layout, 0, 1);
        GridPane.setConstraints(details_layout, 1, 1);

        players_root.getChildren().addAll(top_layout, players_layout, details_layout);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PlayerModel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (PlayerModel.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(PlayerModel.getNumber()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (DatabaseFunctions.getTeamName(PlayerModel.getTeam()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }

                return false;
            });
        });

        allplayers_table.setRowFactory(tv -> {
            TableRow<PlayerModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    details_layout.getChildren().setAll(layout6, data, layout11, data2);
                    SharedModel.setSelected_player(row.getItem());

                    name_label.setText(SharedModel.getSelected_player().getName());
                    age_value.setText(SharedModel.getSelected_player().getAge() + "");
                    type_value.setText(SharedModel.getSelected_player().getType());
                    pos_label.setText(SharedModel.getSelected_player().getType());
                    gs_value.setText(SharedModel.getSelected_player().getGoals() + "");

                    number_value.setText(SharedModel.getSelected_player().getNumber() + "");
                    rank_value.setText(SharedModel.getSelected_player().getRank() + "");
                    team_value.setText(DatabaseFunctions.getTeamName(SharedModel.getSelected_player().getTeam()));
                    des_label.setText(SharedModel.getSelected_player().getDes());

                    if (SharedModel.getSelected_player().getGoals_c() !=-1){

                        gc_value.setText(SharedModel.getSelected_player().getGoals_c() + "");
                        clean_value.setText(SharedModel.getSelected_player().getCleansheets() + "");
                    }

                }
            });
            return row;
        });

    }

    private void goPage(Stage stage, Scene s) {
        stage.setScene(s);
    }

    private void closeProgram(Stage window) {

        if (ConfirmationForm.showAlert("Confirm", "Are you Sure want to exit")) {
            window.close();
        }
    }

}
