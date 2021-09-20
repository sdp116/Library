package LibrarySystemPackage;

import ThingsALibraryNeedsPackage.*;
import LibraryItemsPackage.*;
import ManagersPackage.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;

public class LibraryView {
    NumberFormat formatter = new DecimalFormat("#0.00");

    /*
     *Pages
     */
    public void displayWelcomePage() {
        System.out.println("---------Welcome to the Library System---------");
        System.out.println("Library Information...");
        System.out.println("-----------------------------------------------");
    }

    public void displayLandingPage(){
        System.out.println("Please select one of the following options:");
        System.out.println("Option 1: Log In");
        System.out.println("Option 2: Sign Up");
        System.out.println("Enter in '1' or '2': ");
    }

    public void displayLogInPage(){
        System.out.println("To Log In, please enter in your library card number:");
    }

    public void displaySignUpPage(){
        System.out.println("To Sign Up, please provide the appropriate details as requested:");
    }

    public void displaySignUpCompletePage(User newUser){
        System.out.println();
        System.out.println("Account information:");
        System.out.println("Name: " + newUser.getName());
        System.out.println("Age: " + newUser.getAge());
        System.out.println("Address: " + newUser.getAddress());
        System.out.println("Phone Number: " + newUser.getPhoneNum());
        System.out.println("You can change these details later via 'View Account' at the Main Menu\n");
        System.out.println("Now your library card number is: " + newUser.getCardNum() + "\n");
        System.out.println("This library card number is used to log in to your account\nBe sure to remember this number!");
        System.out.println("Proceeding to Main Menu...\n");
    }

    public void displayMainMenu() {
        System.out.println("------------------Main Menu------------------");
        System.out.println("Please select one of the following options:");
        System.out.println("Option 1: Search For Item");
        System.out.println("Option 2: Return Item");
        System.out.println("Option 3: View Account");
        System.out.println("Option 4: View Cart");
        System.out.println("Option 5: Checkout");
        System.out.println("Option 6: Remove Item From Cart");
        System.out.println("Option 7: View Inventory");
        System.out.println("Option 8: Log Out");
    }

    //Search Pages
    public void displaySearchPage(){
        System.out.println("---------Search Page---------");
        System.out.println("Commands: ");
        System.out.println("type ':all' to display all library items");
        System.out.println("type ':exit' to return to main menu");
        System.out.println("Please enter in the name of the item you are looking for (or command): ");
    }
    public void displayMultipleSearchResultsPage(ItemManager resultsManager){
        System.out.println("---------Search Results Page---------");
        //for(Item item : list){
        //    System.out.println("ISBN: " + item.getISBN() + "    Type: " + item.getClass().toString() + "    Title: " + item.getName());
        //}
        displayItems(resultsManager);
        System.out.println("Please enter in the ISBN of the item you want to see the details of:");
    }
    public void displayAfterSearchResultsPage(boolean isCheckedOut){
        System.out.println("---------After Search Page---------");
        System.out.println("Please select one of the following options:");
        if(!isCheckedOut){
            //Item available (not checked out)
            System.out.println("Option 1: Add To Cart");
        } else {
            //Item unavailable (checked out)
            System.out.println("Option 1: Reserve Item");
        }
        System.out.println("Option 2: Keep Searching");
    }
    public void displayAddToCartPage(){
        System.out.println("---------Add To Cart Page---------");
        System.out.println("Item was added to your cart...Proceeding to Main Menu\n");
    }
    public void displayReserveItemPage(){
        System.out.println("---------Reserve Item Page---------");
        System.out.println("Item was reserved...Proceeding to Main Menu\n");
    }

    //Return Pages
    public void displayReturnPage(){
        System.out.println("---------Return Page---------");
    }
    public void displayReturnSelectionPage(){
        System.out.println("---------Return Selection Page---------");
        System.out.println("Please enter in the ISBN of the item you are returning:");
    }
    public void displayReturnedOverduePage(){
        System.out.println("---------Overdue Page---------");
        System.out.println("Checking if returned items were overdue...");
    }

    //Account Pages
    public void displayAccountPage(User currentUser){
        System.out.println("---------Account Page---------");
        System.out.println("Account information:\n");
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Age: " + currentUser.getAge() + " (" + currentUser.getBirthday() + ")");
        System.out.println("Address: " + currentUser.getAddress());
        System.out.println("Phone Number: " + currentUser.getPhoneNum() + "\n");
        System.out.println("Library Card Number: " + currentUser.getCardNum() + "\n");
        System.out.println("Your balance is: $" + formatter.format(currentUser.getBalance()) + "\n");
        System.out.println("Your pending balance is: $" + formatter.format(currentUser.getPendingBalance()) + "\n");
        if(currentUser.getLoanCount() == 0){ //loan count == 0
            //no loans
            System.out.println("You have no loans\n");
        } else {
            //have at least one loan
            System.out.println("Your Current Loans are: ");

            displayLoans(currentUser.getLoanManager());
            System.out.println("");
        }
        System.out.println("Please select one of the following options:");
        System.out.println("Option 1: Edit Account");
        System.out.println("Option 2: Pay Balance");
        System.out.println("Option 3: View Main Menu");
    }
    public void displayEditAccountPage(){
        System.out.println("---------Edit Account Page---------");
        System.out.println("Please select one of the following options to edit:");
        System.out.println("Option 1: Name");
        System.out.println("Option 2: Birthdate");
        System.out.println("Option 3: Address");
        System.out.println("Option 4: Phone Number");
    }

    //Cart Pages
    public void displayCartPage(){
        System.out.println("---------Cart Page---------");
    }

    //Checkout Pages
    public void displayCheckoutPage(){
        System.out.println("---------Checkout Page---------");
    }
    public void displayCheckoutOptionsPage(){
        System.out.println("Are you ready to checkout?");
        System.out.println("Option 1: Yes");
        System.out.println("Option 2: No");
    }

    //Remove Item Pages
    public void displayRemoveItemPage(){
        System.out.println("---------Remove Item Page---------");
    }
    public void displayRemoveItemSelectionPage(){
        System.out.println("---------Remove Item Selection Page---------");
        System.out.println("Please enter in the ISBN of the item you are removing:");
    }

    //Logout Pages
    public void displayLogOutPage(){
        System.out.println("---------Log Out Page---------");
    }

    //display of the user's inventory to remind them of their borrowed books
    public void displayUserInventoryPage(){
        System.out.println("---------User Inventory---------");
    }

    //User Display
    public void displayUserInventory(User currentUser){
        System.out.println("Your inventory's items are presented below:");
        displayItems(currentUser.getInventory());
    }
    public void displayUserCart(User currentUser){
        System.out.println("Your cart's items are presented below:");
        displayItems(currentUser.getCart());
    }

    //Display
    public void displayItems(ItemManager im) {
        for(Item i : im.getItems()) {
            System.out.println("ISBN: " + i.getISBN() + "    Type: " + i.getType() + "    Title: " + i.getName());
        }
    }

    public void displayLoans(LoanManager lm) {
        for(Loan l : lm.getAllLoans()) {
            System.out.print("ISBN: " + l.getItem().getISBN() + "    Title: " + l.getItem().getName()
                                                              + "    Due: " + l.getDueDate());
            if (l.isOverdue())
                System.out.print(" (overdue)");
            System.out.println();
        }

    }

    //Messages
    public void displayEmptyInventoryMessage(){
        System.out.println("Your inventory is empty\n");
    }
    public void displayEmptyCartMessage(){
        System.out.println("Your cart is empty\n");
    }
    public void displayMessage(String message){
        System.out.println(message);
    }

    //Error Messages
    public void displayErrorIncorrectLibraryCardNum(){
        System.out.println("Error: Please enter in a correct library card number\n");
    }
    public void displayErrorNameWithNum(){
        System.out.println("Error: Please enter in your name without numbers\n");
    }
    public void displayErrorOnlyPhoneNumber(){
        System.out.println("Error: Please enter in only your phone number\n");
    }
    public void displayErrorEnterCorrectType(){
        System.out.println("Error: Please try again entering the correct type of data\n");
    }
    public void displayErrorCannotFindItem(){
        System.out.println("Error: The library does not contain that book\n");
    }
    public void displayErrorEnterValidString(){
        System.out.println("Error: Please enter in a valid string\n");
    }
    public void displayErrorEnterNumInRange(){
        System.out.println("Error: Please enter in an integer in range\n");
    }
    public void displayErrorEnterPositiveInteger(){
        System.out.println("Error: Please enter in a positive integer\n");
    }
    public void displayErrorEnterInteger(){
        System.out.println("Error: Please enter in an integer\n");
    }
    public void displayErrorEnterValidISBN(){
        System.out.println("Error: Please enter in a valid isbn\n");
    }
    public void displayErrorBadBirthMonth(){
        System.out.println("Error: Please enter in valid birth month\n");
    }
    public void displayErrorBadBirthDay(){
        System.out.println("Error: Please enter in valid birth day\n");
    }
    public void displayErrorBadBirthYear(){
        System.out.println("Error: Please enter in valid birth year\n");
    }
}
