package org.example.model;

import org.hibernate.validator.constraints.Range;

public class Product {
    private int id;
    private String name;
    //50
    private String article;
    @Range(min = 1, max = 3, message = "Size of state must be from 1 to 3")
    private int type;

    public Product(int id, String name, String article, int type) {
        this.id = id;
        this.name = name;
        this.article = article;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id == product.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", article='" + article + '\'' +
                ", type=" + type +
                '}';
    }
}
