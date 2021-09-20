package LibraryItemsPackage;
import java.util.Random;

public class Item implements Comparable<Item> {
    //Attributes
    protected String name;
    protected int isbn;
    private String type;

    public int compareTo(Item i) {
        if (this.isbn < i.getISBN()) {
            return -1;
        }
        else if (this.isbn == i.getISBN()) {
            return 0;
        }
        return 1;
    }

    //Constructor
    public Item(String newName, int ISBN){ // Library has to assign isbns so it can check if a generated isbn is taken.

        name = newName;
        type = "";
        isbn = ISBN;
    }

    public Item() {
        // Generate the ISBN.
        Random r = new Random();
        isbn = r.nextInt(8999);
        isbn += 1000;
    }

    //Getters
    public String getName() {
        return name;
    }

    public int getISBN() {
        return isbn;
    }

    public String getType() {
        return type;
    }
}
