package ThingsALibraryNeedsPackage;
import LibraryItemsPackage.Item;
import LibraryItemsPackage.LoanableItem;
import LibrarySystemPackage.*;
import ManagersPackage.*;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.Year;

// TODO: delete child, only use user

public class User {
    // data members of the class.
    //private LibraryModel library;
    protected String name;
    protected LocalDate bday;
    protected int age;
    protected String address;
    protected String phoneNum;
    protected int cardNum;
    protected double balance;
    protected ItemManager cart;
    protected ItemManager inventory;
    protected LoanManager loans;

    public User() {}

    public User(String n, String addr, Integer lno, String pno, LocalDate bd) {
        name = n;
        address = addr;
        phoneNum = pno;
        cardNum = lno;
        cart = new ItemManager();
        inventory = new ItemManager();
        loans = new LoanManager();
        balance = 0.0;
        bday = bd;
    }

    // Set name method
    public void setName(String name)
    {
        this.name = name;
    }

    // Get name method
    public String getName()
    {
        return name;
    }

    // Set birthday method
    public void setBirthday(LocalDate birthday)
    {
        bday = birthday;
    }
    // Get birthday method
    public LocalDate getBirthday(){
        return bday;
    }

    // Get age method
    public int getAge() {
        return Period.between(
                bday,
                LocalDate.now()
        ).getYears();
    }

    // Set address method
    public void setAddress(String address)
    {
        this.address = address;
    }

    // Get address method
    public String getAddress()
    {
        return address;
    }

    // Set phone number method
    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    // Get phone number method
    public String getPhoneNum()
    {
        return phoneNum;
    }

    // Set card number method
    public void setCardNum(int cardNum)
    {
        this.cardNum = cardNum;
    }

    // Get card number method
    public int getCardNum()
    {
        return cardNum;
    }

    public ItemManager getCart() {
        return cart;
    }

    public void addLoan(Loan l) {
        loans.addLoan(l);
    }

    public void deleteLoan(Loan l) {
        loans.deleteLoan(l);
    }

    public int getInventoryCount() {
        return inventory.getItems().size();
    }

    public ItemManager getInventory() {
        return inventory;
    }

    public void addItemToCart(LoanableItem i) {
        cart.addItem(i);
        i.setInCart(true);
    //     i.setCheckedOut(true); // i guess it's not, but... uh
    }

    public void removeItemFromCart(LoanableItem i) {
        cart.removeItem(i);
        i.setInCart(false);
    }

    public int getLoanCount() {
        return loans.getLoanCount();
    }

    public int getCartCount() {
        return cart.getItems().size();
    }

    public LoanManager getLoanManager() {
        return loans;
    }

    public void increaseBalance(double addend) {
        balance += addend;
    }

    public void zeroBalance() {balance = 0;}

    public double getBalance() {
        return balance;
    }

    public double getPendingBalance() {
        ArrayList<Loan> allLoans = loans.getAllLoans();
        double total = 0;

        for(Loan l : allLoans) {
            if (l.isOverdue()) {
                total += loans.calculateFine(l);
            }
        }
        return total;
    }

    public void clearCart() {
        for(Item i : cart.getItems()) {
            ((LoanableItem) i).setInCart(false);
        }
        cart = new ItemManager();
    }
}
