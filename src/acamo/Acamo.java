package acamo;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import de.saring.leafletmap.events.MapClickEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jsonstream.PlaneDataServer;
import messer.BasicAircraft;
import messer.*;
import org.jetbrains.annotations.NotNull;
import senser.Senser;
import de.saring.leafletmap.*;

public class Acamo<mapView> extends Application implements Observer {
    private ActiveAircrafts activeAircrafts;
    private TableView<BasicAircraft> table = new TableView<BasicAircraft>();
    private ObservableList<BasicAircraft> aircraftList = FXCollections.observableArrayList();
    private HashMap<String, Label> aircraftLabelMap;
    private ArrayList<String> fields;
    private ArrayList<Label> aircraftLabelList;
    private int selectedIndex = 0;
    private HashMap<String, Label> LabelAircraft;
    private LeafletMapView mapView;
    private CompletableFuture<Worker.State> loadState;
    private HashMap<String, Marker> markerList = new HashMap<>();
    private Marker homeMarker;


    private double latitude = 48.7433425;
    private double longitude = 9.3201122;
    private static boolean haveConnection = false;

    public static void main(String[] args) {
        launch(args);
    }

    private void resetLocation(double lat, double lng, int distance,
                               PlaneDataServer server) {
        server.resetLocation(lat, lng, distance);
        // loop through the hashtable of markers
        for (Marker anMarker : markerList.values()) {
            mapView.removeMarker(anMarker);
        }
        /* clear the hashtable */
        markerList.clear();

        // move the home marker and the map
        homeMarker.move(new LatLong(lat, lng));
        mapView.panTo(new LatLong(lat, lng));
    }
    /* clear the ActiveAircrafts */


    @Override
    public void start(Stage stage) {
        String urlString = "https://opensky-network.org/api/states/all";
        PlaneDataServer server;

        if (haveConnection)
            server = new PlaneDataServer(urlString, latitude, longitude, 500);
        else
            server = new PlaneDataServer(latitude, longitude, 100);

        new Thread(server).start();

        Senser senser = new Senser(server);
        new Thread(senser).start();

        Messer messer = new Messer();
        senser.addObserver(messer);
        new Thread(messer).start();

        // TODO: create activeAircrafts
        activeAircrafts = new ActiveAircrafts();

        // TODO: activeAircrafts and Acamo needs to observe messer

        fields = BasicAircraft.getAttributesNames();
        messer.addObserver(activeAircrafts);
        messer.addObserver(this);

        // TODO: Fill column header using the attribute names from BasicAircraft
        for (int i = 0; i < fields.size(); i++) {
            TableColumn<BasicAircraft, String> column = new TableColumn<BasicAircraft, String>(fields.get(i));
            column.setCellValueFactory(new PropertyValueFactory<>(fields.get(i)));
            table.getColumns().add(column);
        }
        table.setItems(aircraftList);
        table.setEditable(false);
        table.autosize();

        //Textfields
        final TextField latText = new TextField();
        final TextField lonText = new TextField();

        //Button
        Button subButton = new Button("Submit");

        //Create Table
        mapView = new LeafletMapView();
        mapView.setLayoutX(0);
        mapView.setLayoutY(0);
        mapView.setMaxWidth(640);
        List<MapLayer> config = new LinkedList<>();
        config.add(MapLayer.MAPBOX);


        //Config Map
        loadState = mapView.displayMap(new MapConfig(config,
                new ZoomControlConfig(),
                new ScaleControlConfig(),
                new LatLong(latitude, longitude)));

        //Create Markers
        // markers can only be added when the map is completes
        loadState.whenComplete((state, throwable) -> {
            // create home marker
            homeMarker = new Marker(new LatLong(latitude, longitude),
                    "HOME", "HOME", 0);
            // add custom home marker data
            mapView.addCustomMarker("HOME", "icons/basestation.png");
            // add to display in map
            mapView.addMarker(homeMarker);
            // create plane icons
            for (int i = 0; i <= 24; i++) {
                String number = String.format("%02d", i);
                mapView.addCustomMarker("plane" + number,
                        "icons/plane" + number + ".png");
            }
            // plane markers will be added in update()
            mapView.onMapClick(new MapClickEventListener() {
                @Override
                public void onMapClick(@NotNull LatLong latLong) {
                    String lat = String.valueOf(latLong.getLatitude());
                    String lon = String.valueOf(latLong.getLongitude());
                    latText.setText(lat);
                    lonText.setText(lon);

                    resetLocation(latLong.getLatitude(), latLong.getLongitude(), 500, server);
                }
            });

            subButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String latt = latText.getText();
                    String lonn = lonText.getText();
                    double lat = Double.parseDouble(latt);
                    double lon = Double.parseDouble(lonn);

                    resetLocation(lat, lon, 500, server);

                }
            });
        });


        // TODO: Create layout of table and pane for selected aircraft
        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(10, 10, 10, 10));

        // Vertical Box
        VBox vBoxLeft = new VBox();
        VBox mapBox = new VBox();
        vBoxLeft.setPadding(new Insets(10, 10, 10, 10));
        mapBox.setPadding(new Insets(10, 10, 10, 10));


        // Horizontal Box and two vertical Boxes
        HBox hBoxRight = new HBox();
        VBox vBoxRightA = new VBox();
        VBox vBoxRightB = new VBox();
        VBox mapLeft = new VBox();
        vBoxRightA.setPadding(new Insets(10, 10, 10, 10));
        vBoxRightB.setPadding(new Insets(10, 10, 10, 10));

        // Labels
        Label AircraftsLabel = new Label("Active Aircrafts");
        AircraftsLabel.setFont(new Font(20));
        Label SelectedAircraftALabel = new Label("Selected ...");
        SelectedAircraftALabel.setFont(new Font(20));
        Label SelectedAircraftBLabel = new Label("Aircraft");
        SelectedAircraftBLabel.setFont(new Font(20));
        Label IcaoLabel = new Label("ICAO:");
        Label Operatorlabel = new Label("OPERATOR:");
        Label PosTimeLabel = new Label("POSTIME:");
        Label CoordinateLabel = new Label("COORDINATE:");
        Label SpeedLabel = new Label("SPEED:");
        Label TrakLabel = new Label("TRAK:");
        Label latLabel = new Label("Latitude");
        Label lonLabel = new Label("Longitude");

        vBoxRightA.getChildren().addAll(SelectedAircraftALabel, IcaoLabel, Operatorlabel, PosTimeLabel, CoordinateLabel, SpeedLabel, TrakLabel);
        mapBox.getChildren().addAll(mapView, latLabel, latText, lonLabel, lonText, subButton);


        // Labels value
        vBoxRightB.getChildren().add(SelectedAircraftBLabel);
        LabelAircraft = new HashMap<String, Label>();
        for (int i = 0; i < fields.size(); i++) {
            Label tempLabel = new Label("");
            LabelAircraft.put(fields.get(i), tempLabel);
            vBoxRightB.getChildren().add(tempLabel);
        }

        //add
        vBoxLeft.getChildren().addAll(AircraftsLabel, table);
        hBoxRight.getChildren().addAll(vBoxRightA, vBoxRightB);
        pane.getChildren().addAll(mapBox, vBoxLeft, hBoxRight);

        // TODO: Add event handler for selected aircraft
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    // indentify index
                    selectedIndex = table.getSelectionModel().getSelectedIndex();
                    table.getSelectionModel().select(selectedIndex);
                    BasicAircraft aircraft = table.getSelectionModel().getSelectedItem();
                    ArrayList<Object> attributeValues = BasicAircraft.getAttributesValues(aircraft);

                    // Set values
                    for (int i = 0; i < LabelAircraft.size(); i++) {
                        Label LableTemp = LabelAircraft.get(fields.get(i));
                        LableTemp.setText(attributeValues.get(i).toString());
                    }
                }
            }
        });


        Scene scene = new Scene(pane, 1600, 850);
        stage.setScene(scene);
        stage.setTitle("Acamo");
        stage.sizeToScene();
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }


    // TODO: When messer updates Acamo (and activeAircrafts) the aircraftList must be updated as well
    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update table rows
                aircraftList.clear();
                aircraftList.addAll(activeAircrafts.values());
                aircraftList.add((BasicAircraft) arg);

                // update all values
                table.getSelectionModel().select(selectedIndex);
                BasicAircraft aircraft = table.getSelectionModel().getSelectedItem();
                ArrayList<Object> aircraftValues = BasicAircraft.getAttributesValues(aircraft);

                // Fill in Values
                for (int i = 0; i < LabelAircraft.size(); i++) {
                    Label temp = LabelAircraft.get(fields.get(i));
                    temp.setText(aircraftValues.get(i).toString());
                }

                //Display plane
                for (int i = 0; i < aircraftList.size(); i++) {
                    BasicAircraft b = aircraftList.get(i);
                    //Trak, latlong, icao, planeIcon
                    double h = b.getTrak();
                    int heading = (int) (h / 15);
                    Coordinate cor = b.getCoordinate();
                    double lat = cor.getLatitude();
                    double lon = cor.getLongitude();
                    LatLong latlong = new LatLong(lat, lon);
                    String icao = b.getIcao();
                    String planeIcon = "plane" + heading;

                    if (markerList.containsKey(icao)) {
                        // update marker position and icon
                        Marker marker = markerList.get(icao);
                        marker.move(latlong);
                        marker.changeIcon(planeIcon);
                    } else {
                        //add new marker
                        loadState.whenComplete((state, throwable) -> {
                            Marker marker = new Marker(latlong, icao, planeIcon, 0);
                            mapView.addMarker(marker);
                            markerList.put(icao, marker);
                        });
                    }
                }
            }
        });
    }
}


