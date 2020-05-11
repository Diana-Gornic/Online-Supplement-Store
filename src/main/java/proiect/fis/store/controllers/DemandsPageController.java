package proiect.fis.store.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import proiect.fis.store.model.Product;

import java.io.IOException;

public class DemandsPageController {
    private ObservableList<Product> demandsList;
    @FXML
    private TableView<Product> demands;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private Button mainPageButton;
    @FXML
    private Button stockPageButton;
    @FXML
    private Button quantity;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button saveButton;

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        demands.setItems(demandsList);
    }

    public void setData(ObservableList<Product> demands) {
        this.demandsList = demands;
    }

    @FXML
    public boolean goToManagerPage() {
            Stage stage = (Stage) mainPageButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manager_page.fxml"));
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> param) {
                    if(param == ManagerController.class) {
                        ManagerController controller = new ManagerController();
                        controller.setData(demandsList);
                        return controller;
                    }else{
                        try{
                            return param.newInstance();
                        }catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            try{
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Manager Page");
                stage.show();
                return true;
            }catch (IOException e) {
                System.out.println("Error");
                return false;
            }

    }

    @FXML
    public boolean goToStockPage() {
            Stage stage = (Stage) stockPageButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stock_page.fxml"));
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> param) {
                    if(param == StockPageController.class) {
                        StockPageController controller = new StockPageController();
                        controller.setData(demandsList);
                        return controller;
                    }else {
                        try{
                            return param.newInstance();
                        }catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                }
            });
            try {
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Stock Page");
                stage.show();
                return true;
            }catch (IOException e) {
                System.out.println(e.getStackTrace());
                return false;
            }

    }

    @FXML
    public void changeQuantity(ActionEvent e) {
        int value = Integer.parseInt(quantity.getText());
        if (e.getSource().equals(plusButton)) {
            value = value + 1;
            quantity.setText(value + "");
        } else if (e.getSource().equals(minusButton) && value > 0) {
            value = value - 1;
            quantity.setText(value + "");
        }
    }

    @FXML
    public void saveQuantity() {
        int value = Integer.parseInt(quantity.getText());
        quantity.setText("1");
        int position = demands.getSelectionModel().getSelectedIndex();
        if (position < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "PLEASE SELECT AN ITEM", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
            return;
        }
        demandsList.get(position).setQuantity(value);
        demands.refresh();
    }
    @FXML
    public void deleteProduct(){
        int position = demands.getSelectionModel().getSelectedIndex();
        demandsList.remove(position);
        demands.refresh();
    }
}