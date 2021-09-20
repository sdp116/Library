package LibrarySystemPackage;

import ManagersPackage.*;
import LibraryItemsPackage.*;
import ThingsALibraryNeedsPackage.User;

public class LibraryModel {
  //User Database
  protected UserManager userManager;

  //Library Inventory
  protected ItemManager libraryInventory;

  protected User currentUser;

  //Constructor
  public LibraryModel() {
    libraryInventory = new ItemManager();
    userManager = new UserManager();

  }

  public void addUserToUserManager(User buddy) {
    userManager.addUser(buddy);
  }

  public void addItemToItemManager(Item item){
    libraryInventory.addItem(item);
  }

  public ItemManager getItemManager() {
    return libraryInventory;
  }
public UserManager getUserManager() {return userManager;}
  // debug only
  public void setCurrentUser(User u) {
    currentUser = u;
  }
  public User getCurrentUser() {
    return currentUser;
  }
  //Setters

  //Getters
}
