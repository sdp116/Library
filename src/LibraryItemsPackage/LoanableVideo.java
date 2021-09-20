package LibraryItemsPackage;

public class LoanableVideo extends LoanableItem {

    //Attributes
    private String director;
    private String type = "video";

    //Constructor
    public LoanableVideo(String newName, double newValue, String newDirector, int ISBN) {
        super(newName, newValue, ISBN);
        director = newDirector;
    }

    public LoanableVideo(){
        super();
    }

    //Setters
    public void setDirector(String name){
        director = name;
    }

    //Getters
    public String getDirector(){
        return director;
    }

    public String getType(){ return type;}
}
