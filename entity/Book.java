package by.epamtc.stanislavmelnikov.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String name;
    private String author;
    private int year;
    private int amountPages;

    public Book() {

    }

    public Book(String name, String author, int year, int amountPages, int id) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.amountPages = amountPages;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAmountPages() {
        return amountPages;
    }

    public void setAmountPages(int amountPages) {
        this.amountPages = amountPages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", amountPages=" + amountPages +
                '}';
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return prime * id + ((null == name) ? 0 : name.hashCode()) + ((null == author) ? 0 : author.hashCode()) +
                prime * year + prime * amountPages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Book book = (Book) obj;
        if (id != book.id) {
            return false;
        }
        if (year != book.year) {
            return false;
        }
        if (year != book.year) {
            return false;
        }
        if (null == name) {
            return (name == book.name);
        } else {
            if (!name.equals(book.name)) {
                return false;
            }
        }
        if (null == author) {
            return (author == book.author);
        } else {
            if (!author.equals(book.author)) {
                return false;
            }
        }
        return true;
    }
}
