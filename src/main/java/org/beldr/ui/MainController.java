package org.beldr.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

import org.beldr.db.DbSystem;
import org.beldr.model.ApiKey;
import org.beldr.util.TinyPNGer;

public class MainController extends Application {

    public static TableView<ApiKey> apiKeys;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane(tinifyTab(), settingsTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 300, 300);
        stage.setTitle("InfinitesimalPNG");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    private static Tab tinifyTab() {
        StackPane tinifyPane = new StackPane();
        tinifyPane.getChildren().add(new Text("Drag and drop"));
        tinifyPane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        tinifyPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                TinyPNGer.tinifyGroup(db.getFiles());
            }
            event.setDropCompleted(success);
            event.consume();
        });

        Tab tinifyTab = new Tab("Tinify");
        tinifyTab.setContent(tinifyPane);
        return tinifyTab;
    }

    @SuppressWarnings("unchecked")
    private static Tab settingsTab() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20, 20, 20, 20));

        TableView<ApiKey> tableView = new TableView<>();
        apiKeys = tableView;
        TableColumn<ApiKey, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        TableColumn<ApiKey, String> usageColumn = new TableColumn<>("Usage");
        usageColumn.setCellValueFactory(new PropertyValueFactory<>("usage"));
        tableView.getColumns().setAll(keyColumn, usageColumn);
        tableView.getSortOrder().add(usageColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DbSystem.getKeys().forEach(e -> tableView.getItems().add(new ApiKey(e)));

        final TextField addKey = new TextField();
        addKey.setPromptText("API key");
        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            tableView.getItems().add(new ApiKey(
                    addKey.getText()));
            DbSystem.addKey(addKey.getText());
            addKey.clear();

        });
        final Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            List<ApiKey> selected = tableView.getSelectionModel().getSelectedItems();
            selected.forEach(a -> DbSystem.removeKey(a.getKey()));
            tableView.getItems().removeAll(selected);
        });

        pane.add(tableView, 0, 0, 3, 1);
        pane.add(addKey, 0, 1, 1, 1);
        pane.add(addButton, 1, 1, 1, 1);
        pane.add(deleteButton, 2, 1, 1, 1);

        Tab settingsTab = new Tab("Settings");
        settingsTab.setContent(pane);
        return settingsTab;
    }
}
