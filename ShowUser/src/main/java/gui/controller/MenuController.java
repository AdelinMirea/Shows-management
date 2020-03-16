package gui.controller;

import domain.ArtistDTO;
import domain.ShowSearchDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import show.services.IShowObserver;
import show.services.IShowServer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuController implements IShowObserver {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<ShowSearchDTO> tableView;
    @FXML
    private TextField showIdName;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField noTicketsTextField;
    @FXML
    private TableView<ArtistDTO> tableArtists;

    private int showId = -1;

//    private Service service;
    private IShowServer server;
    private Stage thisStage;
//    private ObservableList<ShowSearchDTO> observableList = FXCollections.observableList(new ArrayList<>());
    private Parent parent;

    public void setServer(IShowServer server) {
        this.server = server;
        initializeAllShowsTable();
    }

    public void setThisStage(Stage stage) {
        this.thisStage = stage;
        thisStage.setOnCloseRequest((event)-> {
            try {
                onLogOutPressed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void initialize() {
        initializeSearchTable();
    }

    private void initializeSearchTable(){
        tableView.getColumns().clear();
        TableColumn<ShowSearchDTO, String> artistNameCol = new TableColumn<>("Artist");
        TableColumn<ShowSearchDTO, String> showNameCol = new TableColumn<>("Spectacol");
        TableColumn<ShowSearchDTO, String> showLocationCol = new TableColumn<>("Locatia");
        TableColumn<ShowSearchDTO, String> hourCol = new TableColumn<>("Ora");
        TableColumn<ShowSearchDTO, Integer> availableSeatsCol = new TableColumn<>("Locuri");

        artistNameCol.setCellValueFactory(new PropertyValueFactory<ShowSearchDTO, String>("artist"));
        showNameCol.setCellValueFactory(new PropertyValueFactory<ShowSearchDTO, String>("show"));
        showLocationCol.setCellValueFactory(new PropertyValueFactory<ShowSearchDTO, String>("location"));
        hourCol.setCellValueFactory(new PropertyValueFactory<ShowSearchDTO, String>("hour"));
        availableSeatsCol.setCellValueFactory(new PropertyValueFactory<ShowSearchDTO, Integer>("seats"));

        tableView.getColumns().addAll(artistNameCol, showNameCol, showLocationCol, hourCol, availableSeatsCol);
        showIdName.setEditable(false);
        designSearchTable(availableSeatsCol);
    }

    private void initializeAllShowsTable(){
        tableArtists.getColumns().clear();

        TableColumn<ArtistDTO, String> nameCol = new TableColumn<>("Artist");
        TableColumn<ArtistDTO, LocalDate> dateCol = new TableColumn<>("Date");
        TableColumn<ArtistDTO, String> locationCol = new TableColumn<>("Location");
        TableColumn<ArtistDTO, Integer> availableCol = new TableColumn<>("Available");
        TableColumn<ArtistDTO, Integer> soldCol = new TableColumn<>("Sold");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("availableTickets"));
        soldCol.setCellValueFactory(new PropertyValueFactory<>("soldTickets"));

        tableArtists.getColumns().addAll(nameCol, dateCol, locationCol, availableCol, soldCol);
        designTotalTable(availableCol);
        populateArtistORMTable();
    }

    private void designSearchTable(TableColumn<ShowSearchDTO, Integer> availableSeats) {
        availableSeats.setCellFactory(param -> getTableCell());
    }

    private void designTotalTable(TableColumn<ArtistDTO, Integer> column){
        column.setCellFactory(param -> getTableCell());
    }

    private <T> TableCell<T, Integer> getTableCell() {
        return new TableCell<T, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                setText(empty ? "" : getItem().toString());
                setGraphic(null);

                TableRow<T> currentRow = getTableRow();

                if (!isEmpty()) {

                    if (item.equals(0) && currentRow != null)
                    {
                        currentRow.setStyle("-fx-background-color:lightcoral");
                    }
                }
            }
        };
    }

    private void populateArtistORMTable(){
        List<ArtistDTO> artists = server.findAllShows();
        tableArtists.setItems(FXCollections.observableList(artists));
    }

    @FXML
    private void setTableItemClick() {
        ShowSearchDTO show = tableView.getSelectionModel().getSelectedItem();
        if (show != null) {
            if (show.getSeats() == 0) {
                showId = -1;
                alerting("Nu se mai pot vinde locuri la acest spectacol", Alert.AlertType.ERROR);
            } else {
                showId = show.getShowId();
                showIdName.setText(showId + "." + show.getShow());
            }
        }

    }

    @FXML
    private void onBuyClick() {

        if (showId == -1) {
            alerting("Selectati un spectacol disponibil din lista", Alert.AlertType.ERROR);
        } else {
            String name = nameTextField.getText();
            int noTickets;
            try {
                noTickets = Integer.parseInt(noTicketsTextField.getText());
                if (noTickets < 0 || name.equals("")) {
                    throw new IllegalArgumentException("Datele introduse sunt incorecte");
                }
                server.buy(showId, name, noTickets);
//                alerting("Operatiune efectuata cu succes", Alert.AlertType.INFORMATION);
                searchByDate();
                populateArtistORMTable();
            } catch (NumberFormatException ne) {
                alerting("Numar de bilete gresit", Alert.AlertType.ERROR);
            } catch (IllegalArgumentException ia) {
                alerting(ia.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception e) {
                alerting(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void alerting(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void searchByDate() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Alegeti o data!");
            alert.show();
        } else {
            List<ShowSearchDTO> shows = server.search(date);
//            tableView.getItems().clear();
            initializeSearchTable();
            tableView.setItems(FXCollections.observableList(shows));
        }
    }

    private void updateSearch() {
        LocalDate date = datePicker.getValue();
        if (date != null) {
            List<ShowSearchDTO> shows = server.search(date);
            initializeSearchTable();
            tableView.setItems(FXCollections.observableList(shows));
        }
    }


    @FXML
    private void onLogOutPressed() throws IOException {
        this.server.logOut(this);
//        Stage primaryStage = new Stage();
//
//        primaryStage.setTitle("Events");
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../resources/login.fxml"));
//        Parent root = loader.load();
//        LogInController controller = loader.getController();
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        controller.setServer(this.server);
//        controller.setStage(primaryStage);
//        controller.setController(this);
//        controller.setParent(parent);
//        thisStage.close();
        Platform.exit();
    }

    @Override
    public void updateData() {
        Platform.runLater(this::initializeAllShowsTable);
        Platform.runLater(this::updateSearch);
    }

    public void setParent(Parent parent){
        this.parent = parent;
    }
}
