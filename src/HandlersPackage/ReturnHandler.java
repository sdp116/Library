package HandlersPackage;
import LibraryItemsPackage.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import ThingsALibraryNeedsPackage.Loan;
import ThingsALibraryNeedsPackage.User;

public class ReturnHandler {
    User user;
    Loan loan;

    public ReturnHandler(User u, Loan l) {
        user = u;
        loan = l;
    }

    //Set the status of an item to being in the library.
    public void returnItem(LoanableItem i) {
        i.setCheckedOut(false);
    }

    //Remove the loan from the user.
    public void deleteLoan(Loan i) {
        user.deleteLoan(i);
    }

    //Determine if the item is overdue based on the checkout date.
    public Loan getLoan() {
        return loan;
    }
}
