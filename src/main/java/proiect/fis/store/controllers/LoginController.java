package proiect.fis.store.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import proiect.fis.store.model.Customer;
import proiect.fis.store.model.HashPassword;
import proiect.fis.store.model.Supplier;
import proiect.fis.store.model.databases.CustomersDB;
import proiect.fis.store.model.databases.SupplierDB;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Button toRegisterBtn;
    @FXML
    private CheckBox checkButton;

    public void initialize(){
        loginButton.setDisable(true);
    }

    @FXML
    public void setButtonProperties(){
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText().trim();

        boolean isDisabled = username.isEmpty() || password.isEmpty();
        loginButton.setDisable(isDisabled);
    }

    @FXML
    public boolean logIn() {
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText().trim();
        String hashedPassword = HashPassword.encrypt(password);

        usernameInput.clear();
        passwordInput.clear();

        if(checkButton.isSelected()){
            SupplierDB supplierDB = SupplierDB.getInstance();

           Supplier supplierFound = supplierDB.searchSupplier(username,hashedPassword);
           if(supplierFound != null){
               if (supplierFound.getPassword_changed() != 0) {
                   try {
                       Stage stage = (Stage) loginButton.getScene().getWindow();
                       FXMLLoader loader = new FXMLLoader(getClass().getResource("/supplier_page.fxml"));
                       loader.setControllerFactory(new Callback<Class<?>, Object>() {
                           @Override
                           public Object call(Class<?> param) {
                               if(param == SupplierController.class){
                                   SupplierController supplierController = new SupplierController();
                                   supplierController.setSupplier(supplierFound);
                                   return supplierController;
                               }else{
                                   try{
                                       return param.newInstance();
                                   }catch (Exception e){
                                       throw new RuntimeException(e);
                                   }
                               }
                           }
                       });
                       Parent root = loader.load();
                       Scene scene = new Scene(root);
                       stage.setScene(scene);
                       stage.show();
                       return true;
                   } catch (IOException e) {
                       //
                       return false;
                   }
               }

               //if the password is not changed
               try {
                   Stage stage = (Stage) loginButton.getScene().getWindow();
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/change_password_page.fxml"));
                   loader.setControllerFactory(new Callback<Class<?>, Object>() {
                       @Override
                       public Object call(Class<?> param) {
                           if(param == ChangePassController.class){
                               ChangePassController changePassController = new ChangePassController();
                               changePassController.setSupplier(supplierFound);
                               return changePassController;
                           }else{
                               try{
                                   return param.newInstance();
                               }catch (Exception e){
                                   throw new RuntimeException(e);
                               }
                           }
                       }
                   });
                   Parent root = loader.load();
                   Scene scene = new Scene(root);
                   stage.setScene(scene);
                   stage.show();
                   return true;
               } catch (IOException e) {
                   //
                   return false;
               }
           }
           }

        Customer customerFound;
        CustomersDB customersDB = CustomersDB.getInstance();

//        if(username.equals("manager") && password.equals("passwordManager")){
//            try{
//                Stage stage = (Stage) loginButton.getScene().getWindow();
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/manager_page.fxml"));
//                Parent root = loader.load();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//                return true ;
//            }catch (IOException e){
//
//            }
//        }


        customerFound = customersDB.searchCustomer(username, hashedPassword);

        //daca am gasit customerul
        if (customerFound != null) {
            if (customerFound.getPassword_changed() != 0) {
                try {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer_page.fxml"));
                    loader.setControllerFactory(new Callback<Class<?>, Object>() {
                        @Override
                        public Object call(Class<?> param) {
                            if(param == CustomerController.class){
                                CustomerController customerController = new CustomerController();
                                customerController.setCustomer(customerFound);
                                return customerController;
                            }else{
                                try{
                                    return param.newInstance();
                                }catch (Exception e){
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    return true;
                } catch (IOException e) {
                    //
                    return false;
                }
            }

            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/change_password_page.fxml"));
                loader.setControllerFactory(new Callback<Class<?>, Object>() {
                    @Override
                    public Object call(Class<?> param) {
                        if(param == ChangePassController.class){
                            ChangePassController changePassController = new ChangePassController();
                            changePassController.setCustomer(customerFound);
                            return changePassController;
                        }else{
                            try{
                                return param.newInstance();
                            }catch (Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                return true;
            } catch (IOException e) {
                //
                return false;
            }
        }

        Alert alert = new Alert(Alert.AlertType.NONE, "Couldn't log in", ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }

        return false;
    }
}
