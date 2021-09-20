package ThingsALibraryNeedsPackage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import LibraryItemsPackage.*;


public class Loan {
    LocalDate dateCheckedOut;
    LoanableItem item;
    boolean renewed;

    public Loan(LoanableItem i){
        item = i;
        dateCheckedOut = LocalDate.now();
        renewed = false;
    }

    public int getLoanLimit(){
        // If item is a book and is not a best seller, it's due in 3 weeks.
        if (item instanceof LoanableBook && !((LoanableBook) item).getIsBestSeller())
            return 21;

        // Everything else is due in 2 weeks.
        return 14;
    }

    public LocalDate getDueDate() {
        return dateCheckedOut.plus(getLoanLimit(), ChronoUnit.DAYS);
    }

    public LoanableItem getItem(){
        return item;
    }

    public boolean isOverdue() {
        boolean status = false;
        //If today is after l.getDueDate(), then the status is overdue.
        if(LocalDate.now().isAfter(this.getDueDate())) {
            status = true;
        }
        return status;
    }

    private void setRenewed(boolean value) {
        renewed = value;
    }

    public void renewLoan() {
        int extension = this.getLoanLimit();
        this.setDueDate(this.getDueDate().plus(extension, ChronoUnit.DAYS));
        this.setRenewed(true);
    }

    // debug only
    public void setDueDate(LocalDate of) {
        dateCheckedOut = of.minus(getLoanLimit(), ChronoUnit.DAYS);
    }

    public boolean getRenewed() {
        return renewed;
    }
}
