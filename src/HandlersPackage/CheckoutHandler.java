package HandlersPackage;

import LibraryItemsPackage.*;
import ManagersPackage.*;
import ThingsALibraryNeedsPackage.*;

import java.util.ArrayList;
import java.util.TreeSet;

public class CheckoutHandler {
  User user;
  ItemManager cart;
  LoanManager loans;

  public CheckoutHandler(User u) {
    loans = new LoanManager();
    user = u;
    cart = user.getCart();
  }

  public void checkOut() {
    for (Item i : cart.getItems()) {
        Loan l = createLoan((LoanableItem) i);
        assignLoanToThis(l);
        assignLoanToUser(l);
        addItemToInventory(i);

        setItemCheckedOut((LoanableItem) i);
    }
    user.clearCart();
  }

  private void assignLoanToThis(Loan l) {
    loans.addLoan(l);
  }

  public boolean userNeedsToPutBackCartItems() {
    if(user.getAge() <= 12) {
      if (user.getCartCount() + user.getLoanManager().getLoanCount() > 5) {
        return true;
      }
    }
      return false;
  }

  public void assignLoanToUser(Loan l) {
    user.addLoan(l);
  }

  public Loan createLoan(LoanableItem i) {
    Loan l = new Loan((LoanableItem) i);
    return l;
  }

  public void setItemCheckedOut(LoanableItem i) {
    i.setCheckedOut(true);
  }

  public void removeItemFromCart(Item i) {
    user.getCart().removeItem(i);
  }

  public void clearCart() {
    user.clearCart();
  }

  public void addItemToInventory(Item i) {
    user.getInventory().addItem(i);
  }

  private boolean userShouldGraduate() {
    if(user.getAge() > 12)
      return true;
    return false;
  }

  public LoanManager getLoans() {
    return loans;
  }
}
