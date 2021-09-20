package ManagersPackage;
import ThingsALibraryNeedsPackage.*;
import LibraryItemsPackage.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class LoanManager extends Manager {
  ArrayList<Loan> loans;

  public LoanManager() {
    loans = new ArrayList<Loan>();
  }

  public Loan createLoan(LoanableItem i) {
    Loan l = new Loan(i);
    return l;
  }

  public ArrayList<Loan> getAllLoans() {
    return loans;
  }

  public void deleteLoan(Loan l) {
    loans.remove(l);
  }

  public void addLoan(Loan l) {
    loans.add(l);
  }

  public int getLoanCount() {
    return loans.size();
  }

  public Loan lookupLoanByISBN(int isbn) {
    Loan loanToReturn = null;
    for (Loan l : loans) {
      if (l.getItem().getISBN() == isbn) {
        loanToReturn = l;
      }
    }

  return loanToReturn;
  }
  public Loan getLoan(Loan l) {
    for(Loan m : loans) {
      if (m.equals(l)) {
        return m;
      }
    }
    return null;
  }

  public double calculateFine(Loan l) {
    double fine = 0.0f;
    //int dueDate = l.getDueDate();
    LocalDate today = LocalDate.now();
    if(today.isAfter(l.getDueDate())) {
      //fine = (dueDate - today) * 0.1;
      fine = (double) (ChronoUnit.DAYS.between(l.getDueDate().atStartOfDay(),
              today.atStartOfDay()) * .1);
    }
    if (fine > l.getItem().getValue()) {
      fine = l.getItem().getValue();
    }
    return fine;
  }

}
