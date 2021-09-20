package LibraryItemsPackage;

import java.time.LocalDate;

public class LoanableBook extends LoanableItem {
    //Attributes
    private boolean isBestSeller;
    private String author;
    private String subject;
    private String publisher;
    private LocalDate publicationDate;
    private String type = "book";

    //Constructor
    public LoanableBook(String name, double value, int ISBN){
        super(name, value, ISBN);
    }
    public LoanableBook(){
        super();
    }

    //Setters
    public void setBestSeller(boolean bestSeller) {
        isBestSeller = bestSeller;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    //Getters
    public String getAuthor() {
        return author;
    }

    public String getSubject() {
        return subject;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public boolean getIsBestSeller() {
        return isBestSeller;
    }

    public String getType(){ return type;}
}
