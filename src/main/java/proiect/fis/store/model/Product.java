package proiect.fis.store.model;

public class Product {
    private String name ;
    private double price ;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }



    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Product) {
            Product product = (Product)obj;
            return this.getName().equals(product.getName());
        }
        return false;
    }

    public boolean updateQuantity(int quantity) {
        this.quantity += quantity;
        return true;
    }
}
