package LibraryItemsPackage;
import ThingsALibraryNeedsPackage.*;
import java.util.ArrayList;

public class LoanableItem extends Item {
    //Attributes
    protected double value;
    private boolean isCheckedOut;
    private boolean isInCart;
    private User reservingUser;
    private ArrayList<User> reservingUsers;

    //Constructor
    public LoanableItem(String newName, double newValue, int ISBN) {
        super(newName, ISBN);
        value = newValue;
        reservingUser = null;
        reservingUsers = new ArrayList<>();
    }

    public LoanableItem() {
    }

    //Getter
    public double getValue() {
        return value;
    }

    public boolean isAvailable() {
        return !(isCheckedOut || isInCart);
    }

    public User getNextReservingUser() {
        return reservingUsers.get(0);
    }

    public ArrayList<User> getReservingUsers() {
        return reservingUsers;
    }

    public boolean isReserved() {
        return reservingUsers.size() > 0;
    }

    //Setter
    public void setCheckedOut(boolean value){
        isCheckedOut = value;
    }
    public void setInCart(boolean value) {isInCart = value; }

//    public void setReserved(boolean value){
//        isReserved = value;
//    }

    public void addToReserveList(User u) {
        reservingUsers.add(u);
    }

    public User popFirstUserFromReserveList() {
        User u = reservingUsers.get(0);
        reservingUsers.remove(u);
        return u;
    }

    public User getFirstUserFromReserveList() {
        return reservingUsers.get(0);
    }
}
