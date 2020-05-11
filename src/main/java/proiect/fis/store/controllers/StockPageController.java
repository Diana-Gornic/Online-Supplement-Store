package proiect.fis.store.controllers;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import proiect.fis.store.model.Product;
import proiect.fis.store.model.databases.StockDB;

import java.io.IOException;

import java.util.function.Predicate;

public class StockPageController {
    @FXML
    private TextField filterField;
  //  @FXML
    //private Button addToDemands;
    @FXML
    private Button backFromStock;
    @FXML
    private Button goToDemandsPage;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    private ObservableList<Product> demandsBucket = FXCollections.observableArrayList();

    public ObservableList<Product> getProductsList() {
        StockDB database = StockDB.getInstance();
        ObservableList<Product> products = database.getProducts();
        return products;
    }

    private FilteredList<Product> filteredProducts = new FilteredList<>(getProductsList(), new Predicate<Product>() {
        @Override
        public boolean test(Product product) {
            return true;
        }
    });

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        productTable.setItems(filteredProducts);
    }

    public void searchProduct() {
        filteredProducts.setPredicate(new Predicate<Product>() {
            @Override
            public boolean test(Product product) {
                boolean res = product.getName().toLowerCase().contains(filterField.getText().toLowerCase().trim());
                return res;
            }
        });
    }

    @FXML
    public boolean backToManagerPage() {
        Stage stage = (Stage) backFromStock.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/manager_page.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                if (param == ManagerController.class) {
                    ManagerController controller = new ManagerController();
                    controller.setData(demandsBucket);
                    return controller;
                } else {
                    try {
                        return param.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Manager Page");
            stage.show();
            return true;
        } catch (IOException e) {
            System.out.println("Error");
            return false;
        }
    }

    @FXML
    public void addToDemandsBucket() {
        Product product = productTable.getSelectionModel().getSelectedItem();
        demandsBucket.add(product);
        System.out.println(product.getName());
    }

    public void setData(ObservableList<Product> demandsBucket) {
        this.demandsBucket = demandsBucket;
    }
    @FXML
    public boolean goToDemandsPage() {
        Stage stage = (Stage) goToDemandsPage.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/demands_page.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                if (param == DemandsPageController.class) {
                    DemandsPageController controller = new DemandsPageController();
                    controller.setData(demandsBucket);
                    return controller;
                } else {
                    try {
                        return param.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Demands Page");
            stage.show();
            return true;
        } catch (IOException e) {
            System.out.println("Error");
            return false;
        }
    }
}
