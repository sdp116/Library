package LibraryItemsPackage;

public class LoanableAudio extends LoanableItem{

    //Attributes
    private String primaryAuthor;
    private String type = "audio";

    //Constructor
    public LoanableAudio(String newName, double newValue, String newPrimaryAuthor, int ISBN) {
        super(newName, newValue, ISBN);
        primaryAuthor = newPrimaryAuthor;
    }
    public LoanableAudio(){
        super();
    }

    //Setters
    public void setPrimaryAuthor(String primaryAuthor) {
        this.primaryAuthor = primaryAuthor;
    }

    //Getters
    public String getPrimaryAuthor() {
        return primaryAuthor;
    }

    public String getType(){ return type;}
}
