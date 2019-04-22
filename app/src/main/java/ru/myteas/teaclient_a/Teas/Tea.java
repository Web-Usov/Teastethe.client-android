package ru.myteas.teaclient_a.Teas;

import java.sql.Struct;

public class Tea {
    public static class Parent{

        private String name;
        private String id;

        public Parent(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Parent{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }


    private String name;
    private String type;
    private double rating;
    private Parent parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Tea() {
        this.name = "DefaultName";
        this.type = "DefaultType";
        this.rating = 0.0;
        this.parent = new Parent("DefaultParentName","DefaultParentId");
    }

    public Tea(String name, String type, double rating, Parent parent) {
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Tea{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", rating=" + rating +
                ", parent=" + parent +
                '}';
    }
}
