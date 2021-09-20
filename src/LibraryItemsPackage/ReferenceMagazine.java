package LibraryItemsPackage;

public class ReferenceMagazine extends ReferenceItem {
    //Attributes
    private String author = "";
    private String type = "reference magazine";

    //Constructor
    public ReferenceMagazine(String newName, int ISBN) {
        super(newName, ISBN);
    }
    
    public ReferenceMagazine(){
        super();
    }

    //Setter
    public void setAuthor(String author) {
        this.author = author;
    }

    //Getter
    public String getAuthor() {
        return author;
    }

    public String getType(){ return type;}
}
