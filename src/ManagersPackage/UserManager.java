package ManagersPackage;
import ThingsALibraryNeedsPackage.*;
import java.util.HashSet;

public class UserManager {
    HashSet<User> users;

    public UserManager() {
        users = new HashSet<User>();
    }

    public User lookupUser(int n) {
        for (User u : users) {
            if (u.getCardNum() == n) {
                return u;
            }
        }
        return null;
    }
public HashSet<User> getUsers() {
        return users;
}
    public void addUser(User user) {
        users.add(user);
    }
}
