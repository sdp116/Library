package LibraryItemsPackage;

import java.time.LocalDate;
import java.util.Date;

public class ReferenceBook extends ReferenceItem {
    //Attributes
    private String author;
    private String subject;
    private String publisher;
    private LocalDate publicationDate;
    private String type = "reference book";

    //Constructor
    public ReferenceBook(String newName, int ISBN) {
        super(newName, ISBN);
    }
    public ReferenceBook(){
        super();
    }

    //Setters
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

    public String getType(){ return type;}
}
