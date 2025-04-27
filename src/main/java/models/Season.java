// Season.java
package models;

public class Season {
    private int id;
    private int year;
    private String name;

    public Season() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", year=" + year +
                ", name='" + name + '\'' +
                '}';
    }
}
