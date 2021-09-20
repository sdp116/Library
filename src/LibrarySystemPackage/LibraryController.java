package LibrarySystemPackage;

import HandlersPackage.CheckoutHandler;
import HandlersPackage.ReturnHandler;
import LibraryItemsPackage.*;
import ThingsALibraryNeedsPackage.*;
import ManagersPackage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;

public class LibraryController {

    //Attributes
    private LibraryView libraryView;
    private LibraryModel libraryModel;
    NumberFormat formatter;

    //Constructor
    public LibraryController(LibraryView libraryView, LibraryModel libraryModel) {
        this.libraryView = libraryView;
        this.libraryModel = libraryModel;
        this.formatter = new DecimalFormat("#0.00");
    }


    /*
    Diagram Functions
     */
    //Methods
    public void logInUser(User newUser){
        libraryModel.currentUser = newUser;
    }
    public void logOutCurrentUser() { libraryModel.currentUser = null;}

    public User createUser(){
        return new User();
    } //

    public void viewAccount(){ //might not need; functions further below cover this
        libraryView.displayAccountPage(libraryModel.currentUser);
        getViewAccountFlow();
    }

    public CheckoutHandler createCheckoutHandler(){
        return new CheckoutHandler(libraryModel.currentUser);
    }

    public void addItem(){

    }

    public void verifyCredentials(int libraryCardNumber){
        if(libraryCardNumber == libraryModel.currentUser.getCardNum()){
            //Valid User
            //Proceed to log in
        } else {
            //Invalid User
            //Ask to enter an actual library card number
        }
    }

    public void searchItem(){

    }

    public void removeUser(){

    }

    public void removeItem(){

    }

    public void editUsername(String n){
            libraryModel.currentUser.setName(n);
    }

    public void editAddress(String a){
            libraryModel.currentUser.setAddress(a);
    }

    public void editPhoneNumber(String p){
            libraryModel.currentUser.setPhoneNum(p);
    }

    public void editBirthDate(LocalDate birthdate){
        libraryModel.currentUser.setBirthday(birthdate);
    }

    public void reserveItem(){
        //line 544 is where we do this (can extract to here)
    }

    public void applyUserFine(Double fine){

    }

    /*
    ---------------------------Library Flows---------------------------
     */
    //Loops until user selects log in OR sign up, then transitions to the picked item
    public void getLandingFlow(){
        libraryView.displayWelcomePage();
        libraryView.displayLandingPage();
        boolean onLandingFlow = true;
        while(onLandingFlow) {
            Scanner in = new Scanner(System.in);
            try {
                int option = in.nextInt();
                if (option == 1) {
                    libraryView.displayMessage("You selected Log In...Proceeding to Log In page\n");
                    onLandingFlow = false;
                    libraryView.displayLogInPage();
                    getUserLogInFlow();
                } else if (option == 2) {
                    libraryView.displayMessage("You selected Sign Up...Proceeding to Sign Up page\n");
                    onLandingFlow = false;
                    libraryView.displaySignUpPage();
                    getUserSignUpFlow();
                } else {
                    libraryView.displayErrorEnterNumInRange();
                    libraryView.displayLandingPage();
                }
            } catch (InputMismatchException e){
                libraryView.displayErrorEnterInteger();
                libraryView.displayLandingPage();
            }
        }
    }

    //Loops until user is able to sign in, then transitions to Main Menu
    public void getUserLogInFlow() {
        boolean onLogInFlow = true;
        while (onLogInFlow) {
            Scanner in = new Scanner(System.in);
            try {
                int option = in.nextInt();
                User findUser = libraryModel.userManager.lookupUser(option);
                if (findUser != null) {
                    logInUser(findUser);
                    libraryView.displayMessage("Welcome " + libraryModel.currentUser.getName());
                    libraryView.displayMessage("You successfully logged in...Proceeding to Main Menu\n");
                    onLogInFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();
                } else {
                    libraryView.displayErrorIncorrectLibraryCardNum();
                    libraryView.displayLogInPage();
                }
            } catch (InputMismatchException e) {
                libraryView.displayErrorEnterInteger();
                libraryView.displayLogInPage();
            }
        }
    }

    //Loops until user submits valid information, then transitions to Main Menu
    public void getUserSignUpFlow(){
        boolean onSignUpFlow = true;
        String name = "";
        int birthYear = 0;
        int birthMonth = 0;
        int birthDay = 0;
        //int age = 0;
        String address = "";
        String phoneNumber = "";
        while (onSignUpFlow) {
            Scanner in = new Scanner(System.in);
            try {
                //LocalDate.now().isLeapYear();
                //name
                if(name.isEmpty()){
                    libraryView.displayMessage("Please enter in your name:");
                    name = in.nextLine();
                    if(!isLetters(name)){
                        //bad name
                        libraryView.displayErrorNameWithNum();
                        libraryView.displaySignUpPage();
                        name = "";
                        continue;
                    }
                }

                //birth month
                if(birthMonth == 0){
                    libraryView.displayMessage("Please enter in your birth month:");
                    birthMonth = in.nextInt();
                    if(birthMonth <= 0 || birthMonth >= 13){
                        //bad birth month
                        birthMonth = 0;
                        libraryView.displayErrorBadBirthMonth();
                        continue;
                    }
                }
                //birth day
                if(birthDay == 0){
                    libraryView.displayMessage("Please enter in your birth day:");
                    birthDay = in.nextInt();
                    if(birthDay <= 0 || birthDay >= 31){
                        //bad birth day
                        birthDay = 0;
                        libraryView.displayErrorBadBirthDay();
                        continue;
                    }
                }
                //birth year
                if(birthYear == 0){
                    libraryView.displayMessage("Please enter in your birth year:");
                    birthYear = in.nextInt();
                    if(birthYear <= 1902 || birthYear >= 2022){
                        //bad birth year
                        birthYear = 0;
                        libraryView.displayErrorBadBirthYear();
                        continue;
                    }
                    in.nextLine();
                }

                //address
                if(address.isEmpty()){
                    libraryView.displayMessage("Please enter in your address:");
                    address = in.nextLine();
                }

                //phone number
                if(phoneNumber.isEmpty()){
                    libraryView.displayMessage("Please enter in your phone number (no dashes):");
                    phoneNumber = in.next();
                    if(!doesStringOnlyContainNum(phoneNumber)){
                        libraryView.displayErrorOnlyPhoneNumber();
                        libraryView.displaySignUpPage();
                        phoneNumber = "";
                        continue;
                    }
                }
                onSignUpFlow = false;

                //Create Library Number
                int libraryCardNum = this.generateLibraryCardNumber();

                LocalDate userBday = LocalDate.of(birthYear, birthMonth, birthDay);

                //create user
                User newUser = new User(name, address, libraryCardNum, phoneNumber, userBday);

                libraryModel.addUserToUserManager(newUser);

                //set currentUser
                logInUser(newUser);

                //Display User's information after creation
                libraryView.displaySignUpCompletePage(newUser);

                libraryView.displayMainMenu();
                getMainMenuFlow();

            } catch (InputMismatchException | NumberFormatException e) {
                libraryView.displayErrorEnterCorrectType();
                libraryView.displaySignUpPage();
            }
        }

    }

    //Loops until user logs out then presents the landing page
    public void getMainMenuFlow(){
        boolean onMainMenuFlow = true;
        while(onMainMenuFlow) {
            Scanner in = new Scanner(System.in);
            try {
                int option = in.nextInt();
                if (option == 1) {
                    libraryView.displayMessage("You selected Search Item...Proceeding to Search Page\n");
                    onMainMenuFlow = false;
                    libraryView.displaySearchPage();
                    getSearchItemFlow();
                } else if (option == 2) {
                    libraryView.displayMessage("You selected Return Item...Proceeding to Return Page\n");
                    onMainMenuFlow = false;
                    libraryView.displayReturnPage();
                    getReturnItemFlow();
                } else if (option == 3) {
                    libraryView.displayMessage("You selected View Account...Proceeding to Account Page\n");
                    onMainMenuFlow = false;
                    libraryView.displayAccountPage(libraryModel.currentUser);
                    getViewAccountFlow();
                }  else if (option == 4) {
                    libraryView.displayMessage("You selected View Cart...Proceeding to Cart Page\n");
                    onMainMenuFlow = false;
                    libraryView.displayCartPage();
                    getViewCartFlow();
                } else if (option == 5) {
                    libraryView.displayMessage("You selected Checkout...Proceeding to Checkout Page\n");
                    onMainMenuFlow = false;
                    libraryView.displayCheckoutPage();
                    getCheckoutFlow();
                } else if (option == 6) {
                    libraryView.displayMessage("You selected Remove Item From Cart...Proceeding to Remove Item Page\n");
                    onMainMenuFlow = false;
                    libraryView.displayRemoveItemPage();
                    getRemoveItemFlow();
                } else if(option == 7){
                    libraryView.displayMessage("You selected View Inventory...Proceeding to Inventory Page");
                    onMainMenuFlow = false;
                    libraryView.displayUserInventoryPage();
                    getUserInventoryFlow();
                } else if (option == 8) {
                    libraryView.displayMessage("You selected Log Out...Exiting out of Library System\n");
                    onMainMenuFlow = false;
                    libraryView.displayLogOutPage();
                    getLogOutFlow();
                } else {
                    libraryView.displayErrorEnterNumInRange();
                    libraryView.displayMainMenu();
                }
            } catch (InputMismatchException e){
                libraryView.displayErrorEnterInteger();
                libraryView.displayMainMenu();
            }
        }
    }

    //Search Flows
    //Loops until user is satisfied with searching, then can exit to Main Menu
    public void getSearchItemFlow(){
        boolean onSearchItemFlow = true;
        while (onSearchItemFlow) {
            Scanner in = new Scanner(System.in);
            try {
                String query = in.nextLine();

                if (!query.contains(":")) {

                    //query is a title

                    //query the libraryInventory for the title
                    ArrayList<Item> resultsList = libraryModel.libraryInventory.lookupItemByName(query);
                    ItemManager resultsManager = new ItemManager(resultsList);

                    //non empty list means items found
                    if(!resultsList.isEmpty()){
                        //non empty list

                        if(resultsList.size() > 1) {
                            //Multiple Results Flow
                            libraryView.displayMultipleSearchResultsPage(resultsManager);

                            getMultipleResultFlow(resultsManager);
                        } else {
                            //Get item info from result list
                            Item searchedItem = resultsList.get(0);

                            libraryView.displayMessage("Found " + searchedItem.getType() + ": " + searchedItem.getName() + "...showing details\n");
                            libraryView.displayMessage("Name: " + searchedItem.getName());

                            getShowSearchItemDetails(searchedItem);

                            //get if searched item is checked out (available/unavailable)
                            boolean isAvailable = ((LoanableItem) searchedItem).isAvailable();

                            if (isAvailable) {
                                //Item available (not checked out)
                                libraryView.displayMessage(searchedItem.getName() + " is currently available to checkout\n");
                            }else {
                                //Item unavailable (checked out)
                                libraryView.displayMessage(searchedItem.getName() + " is currently unavailable to check out\n");
                            }
                            onSearchItemFlow = false;
                            libraryView.displayAfterSearchResultsPage(!isAvailable);
                            getAfterSearchFlow(!isAvailable, (LoanableItem) searchedItem);
                        }
                    } else {
                        //empty list

                        libraryView.displayErrorCannotFindItem();
                        libraryView.displaySearchPage();
                    }
                } else if(query.toLowerCase().equals(":exit")){
                    //":exit" is command to leave search to main menu
                    libraryView.displayMessage("Exiting search item page...Proceeding to Main Menu\n");
                    onSearchItemFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();
                // else if query is :info <ISBN>
                } else if(query.toLowerCase().equals(":all")){
                    //":all" is command to show catalog
                    libraryView.displayMessage("Displaying Catalog:");
                    libraryView.displayItems(libraryModel.getItemManager());
                    libraryView.displaySearchPage();
                } else{
                    //query isn't a valid command and doesn't contain search content
                    libraryView.displayErrorCannotFindItem();
                    libraryView.displaySearchPage();
                }
            } catch (InputMismatchException e) {
                libraryView.displayErrorEnterValidString();
                libraryView.displaySearchPage();
            }
        }
    }
    public void getShowSearchItemDetails(Item searchedItem){
        if(searchedItem instanceof LoanableItem) {
            //searchedItem is type loanableItem

            //depending on type of loanableItem print out extra details
            if(searchedItem instanceof LoanableBook){
                libraryView.displayMessage("Author: " + ((LoanableBook) searchedItem).getAuthor());
                libraryView.displayMessage("Subject: " + ((LoanableBook) searchedItem).getSubject());
                libraryView.displayMessage("Publisher: " + ((LoanableBook) searchedItem).getPublisher());
                libraryView.displayMessage("Publishing Date: " + ((LoanableBook) searchedItem).getPublicationDate());

            } else if (searchedItem instanceof LoanableAudio){
                libraryView.displayMessage("Primary Author: " + ((LoanableAudio) searchedItem).getPrimaryAuthor() );

            } else if(searchedItem instanceof LoanableVideo) {
                libraryView.displayMessage("Director: " + ((LoanableVideo) searchedItem).getDirector());

            }
            libraryView.displayMessage(""); //new line spacer

        } else {
            //searchedItem is of type ReferenceItem

            //print out details based on ReferenceItem type
            if(searchedItem instanceof ReferenceBook){
                libraryView.displayMessage("Author: " + ((ReferenceBook) searchedItem).getAuthor());
                libraryView.displayMessage("Subject: " + ((ReferenceBook) searchedItem).getSubject());
                libraryView.displayMessage("Publisher: " + ((ReferenceBook) searchedItem).getPublisher());
                libraryView.displayMessage("Publishing Date: " + ((ReferenceBook) searchedItem).getPublicationDate());

            } else if (searchedItem instanceof  ReferenceMagazine){
                libraryView.displayMessage("Author: " + ((ReferenceMagazine) searchedItem).getAuthor());
            }

            libraryView.displayMessage("This item is a reference item...Unable to checkout\n");
            //Proceed to main menu
            libraryView.displayMainMenu();
            getMainMenuFlow();
        }
    }
    //Loops until user selects item from result list then proceeds
    public void getMultipleResultFlow(ItemManager resultsManager){
        boolean inMultipleResultFlow = true;

        while(inMultipleResultFlow){
            Scanner in = new Scanner(System.in);

            try{
                int isbn = in.nextInt();
                if(isbn <= 9999 || isbn >= 1000){ //good input

                    if(libraryModel.libraryInventory.lookupItemByISBN(isbn) == null){ //bad input
                        libraryView.displayMultipleSearchResultsPage(resultsManager);
                    } else { //good input (isbn is within the range of possible ISBNs and matches an item)
                        //search library inventory for inputted isbn

                        LoanableItem selectedItem = (LoanableItem) libraryModel.libraryInventory.lookupItemByISBN(isbn);

                        //check if selectedItem returned back an item
                        if(selectedItem != null){
                            //selectedItem is an actual item

                            libraryView.displayMessage("Found " + selectedItem.getType() + ": " + selectedItem.getName() + "...showing details\n");
                            libraryView.displayMessage("Name: " + selectedItem.getName());
                            getShowSearchItemDetails(selectedItem);

                            //get if searched item is checked out (available/unavailable)
                            boolean isCheckedOut = selectedItem.isAvailable();
                            if (!isCheckedOut) {
                                //Item available (not checked out)
                                libraryView.displayMessage(selectedItem.getName() + " is currently available to check out\n");
                            } else {
                                //Item unavailable (checked out)
                                libraryView.displayMessage(selectedItem.getName() + " is currently checked out\n");
                            }

                            inMultipleResultFlow = false;
                            libraryView.displayAfterSearchResultsPage(isCheckedOut);
                            getAfterSearchFlow(isCheckedOut, selectedItem);
                        } else {
                            //selectedItem is null, a.k.a isbn could not be found, try again
                            libraryView.displayErrorEnterValidISBN();
                            libraryView.displayMultipleSearchResultsPage(resultsManager);
                        }
                    }
                } else { //bad input
                    libraryView.displayErrorEnterPositiveInteger();
                    libraryView.displayMultipleSearchResultsPage(resultsManager);
                }
            } catch (InputMismatchException e) {
                libraryView.displayErrorEnterInteger();
                libraryView.displayMultipleSearchResultsPage(resultsManager);
            }
        }
    }
    //User has option to pick based on availability and default keep searching option that returns to getSearchItemFlow()
    public void getAfterSearchFlow(boolean isCheckedOut, LoanableItem item){

        boolean inAfterSearchFlow = true;
        String presentedOption;
        if(!isCheckedOut) {
            //item available (not checked out)
            presentedOption = "Add To Cart";
        } else {
            //item unavailable (checked out)
            presentedOption = "Reserve Item";
        }


        while(inAfterSearchFlow){
            Scanner in = new Scanner(System.in);
            try {
                int option = in.nextInt();
                if(option == 1){
                    //reserve OR add to cart
                    libraryView.displayMessage("You selected " + presentedOption + "...Proceeding to " + presentedOption + " page\n");

                    if(!isCheckedOut){
                        //item available (not checked out)
                        libraryView.displayAddToCartPage();
                        libraryModel.currentUser.addItemToCart(item);
                    } else {
                        //item unavailable (checked out)
                        libraryView.displayReserveItemPage();
                        if (item.isReserved()){
                            //item reserved
                            libraryView.displayMessage(item.getName() + " reserved by " + item.getNextReservingUser().getName());
                        }
                        item.addToReserveList(libraryModel.currentUser);
                        libraryView.displayMessage("You were added to the reserve list\n");
                    }
                    inAfterSearchFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();

                } else if(option == 2){
                    //keep searching
                    libraryView.displayMessage("You selected Keep Searching...Proceeding to Search Page\n");
                    inAfterSearchFlow = false;
                    libraryView.displaySearchPage();
                    getSearchItemFlow();
                } else {
                    libraryView.displayErrorEnterNumInRange();
                    libraryView.displayAfterSearchResultsPage(isCheckedOut);
                }

            } catch (InputMismatchException e){
                libraryView.displayErrorEnterInteger();
                libraryView.displayAfterSearchResultsPage(isCheckedOut);
            }
        }
    }

    //Return Flows
    //Initial check if user actually has items to return
    public void getReturnItemFlow(){
        boolean userInventoryEmpty = libraryModel.currentUser.getInventoryCount() == 0;

        if(userInventoryEmpty){
            //inventory empty

            //display inventory empty message
            libraryView.displayEmptyInventoryMessage();

            libraryView.displayMessage("Proceeding to Main Menu...");

            //transition to main menu
            libraryView.displayMainMenu();
            getMainMenuFlow();
        } else {
            //inventory not empty

            //present inventory
            libraryView.displayUserInventory(libraryModel.currentUser);

            //transition to getReturnItemSelectionFlow
            libraryView.displayReturnSelectionPage();
            getReturnItemSelectionFlow();
        }
    }
    //Checks if item is reserved, prev. renewed, and wants to renew; in the end removes item or renews item
    public void getReturnItemSelectionFlow(){
        boolean onReturnItemSelectionFlow = true;
        while(onReturnItemSelectionFlow){
            Scanner in = new Scanner(System.in);
            try{
                int isbn = in.nextInt();

                if(isbn > 0){ //good input

                    if(isbn > 9999 || isbn < 1000){ //bad input
                        libraryView.displayErrorEnterNumInRange();
                        libraryView.displayReturnPage();
                        libraryView.displayUserInventory(libraryModel.currentUser);
                        libraryView.displayReturnSelectionPage();

                    } else { //good input (isbn is within the range of possible ISBNs)
                        LoanableItem selectedItem = libraryModel.libraryInventory.lookupLoanableItemByISBN(isbn);
                        Loan currentLoan = libraryModel.currentUser.getLoanManager().lookupLoanByISBN(isbn);
                        //check if selectedItem returned back an item
                        if(selectedItem != null){
                            //selectedItem is an actual item
                            libraryView.displayMessage("You selected " + selectedItem.getName() + "\n");

                            // make ReturnHandler
                            ReturnHandler currentReturn = new ReturnHandler(libraryModel.currentUser, libraryModel.currentUser.getLoanManager().lookupLoanByISBN(selectedItem.getISBN()));

                            //continue with normal flow
                            //if: selected item is reserved
                            if(selectedItem.isReserved()) { //Item reserved
                                //Display item status
                                libraryView.displayMessage("Item has a reservation...No option to renew");
                                libraryView.displayMessage("Removing " + selectedItem.getName() + " from your inventory...\n");

                                //remove item from user inventory
                                libraryModel.currentUser.getInventory().removeItem(selectedItem);

                                //Proceed to check if item is overdue
                                onReturnItemSelectionFlow = false;
                                libraryView.displayReturnedOverduePage();
                                getReturnedItemOverdue(currentReturn);
                            } else { //Item not reserved
                                //check is item previously renewed
                                if(currentLoan.getRenewed()){ //Item previously renewed
                                    libraryView.displayMessage("Item was previously renewed...No option to renew again");
                                    //Remove item from inventory
                                    libraryView.displayMessage("Removing " + selectedItem.getName() + " from your inventory...\n");

                                    //remove item from inventory
                                    libraryModel.currentUser.getInventory().removeItem(selectedItem);

                                    //Proceed to check if item is overdue
                                    onReturnItemSelectionFlow = false;
                                    libraryView.displayReturnedOverduePage();
                                    getReturnedItemOverdue(currentReturn);
                                } else { //Item not previously renewed
                                    //Present message asking if they want to renew the book
                                    libraryView.displayMessage("Do you want to:");
                                    libraryView.displayMessage("Option 1: Renew the item");
                                    libraryView.displayMessage("Option 2: Return the item");

                                    int option = in.nextInt();

                                    if(option == 1){ //User wants to renew item
                                        //loan is an available object from above

                                        //extend the loan
                                        currentLoan.renewLoan();

                                        libraryView.displayMessage("You loan has been extended...Proceeding to Main Menu\n");

                                        //Proceed to Main Menu
                                        onReturnItemSelectionFlow = false;
                                        libraryView.displayMainMenu();
                                        getMainMenuFlow();

                                    } else if (option == 2) { //User wants to return item
                                        //Display removing item from inventory
                                        libraryView.displayMessage("Removing " + selectedItem.getName() + " from your inventory...\n");

                                        //return handler return item (set as not checked out)
                                        currentReturn.returnItem(selectedItem);

                                        //remove item from user inventory
                                        libraryModel.currentUser.getInventory().removeItem(selectedItem);

                                        //Proceed to check if item is overdue
                                        onReturnItemSelectionFlow = false;
                                        libraryView.displayReturnedOverduePage();
                                        getReturnedItemOverdue(currentReturn);

                                    } else { //User entered number out of available range
                                        libraryView.displayErrorEnterNumInRange();
                                        libraryView.displayReturnPage();
                                        libraryView.displayUserInventory(libraryModel.currentUser);
                                        libraryView.displayReturnSelectionPage();
                                    }
                                }
                            }
                        } else {
                            //selectedItem is null, a.k.a isbn could not be found, try again
                            libraryView.displayErrorEnterValidISBN();
                            libraryView.displayReturnPage();
                            libraryView.displayUserInventory(libraryModel.currentUser);
                            libraryView.displayReturnSelectionPage();
                        }
                    }
                } else {
                    //bad input
                    libraryView.displayErrorEnterPositiveInteger();
                    libraryView.displayReturnPage();
                    libraryView.displayUserInventory(libraryModel.currentUser);
                    libraryView.displayReturnSelectionPage();
                }
            } catch (InputMismatchException e){
                libraryView.displayErrorEnterInteger();
                libraryView.displayReturnPage();
                libraryView.displayUserInventory(libraryModel.currentUser);
                libraryView.displayReturnSelectionPage();
            }
        }
    }
    //Checks if item was returned on time
    public void getReturnedItemOverdue(ReturnHandler currentReturn){ //need item's value and loan dates
        //calculate if item was returned within due date in ReturnHandler

        if(!currentReturn.getLoan().isOverdue()){ //Item returned before due date
            libraryView.displayMessage("Item returned on time...No fines to apply\n");
            libraryView.displayMessage("Proceeding to Main Menu...\n");

        } else { //Item returned after due date
            libraryView.displayMessage("Item returned late...Calculating fines\n");
            Loan currentLoan = libraryModel.currentUser.getLoanManager().getLoan(currentReturn.getLoan());
            //calculate fines based on amount of days past since due date * $0.10  (fine cannot be more than the value of the item)
            double fineAmount = libraryModel.currentUser.getLoanManager().calculateFine(currentLoan);

            libraryView.displayMessage("Your fine is: $" + formatter.format(fineAmount) + "\n");

            libraryView.displayMessage("Updating account balance...Account balance updated\n");
            //update account balance with the fine amount (add the fine amount)
            libraryModel.currentUser.increaseBalance(libraryModel.currentUser.getBalance() + fineAmount);

            libraryView.displayMessage("Your account balance is: $" + formatter.format(libraryModel.currentUser.getBalance()));
            libraryView.displayMessage("To pay your account balance: Go to Main Menu > View Account > Pay Balance\n");
        }

        //deleting loan; updates user's account
        currentReturn.deleteLoan(currentReturn.getLoan());

        //Proceeding to Main Menu
        libraryView.displayMainMenu();
        getMainMenuFlow();
    }

    //Account Flows
    public void getViewAccountFlow(){
        boolean onViewAccountFlow = true;
        while(onViewAccountFlow){
            Scanner in = new Scanner(System.in);
            try {
                int option = in.nextInt();
                if(option == 1){ //Edit Account
                    libraryView.displayMessage("You selected Edit Account...Proceeding to Edit Account\n");

                    onViewAccountFlow = false;
                    libraryView.displayEditAccountPage();
                    getEditAccountFlow();
                }else if (option == 2){ //Pay Balance
                    libraryView.displayMessage("You selected Pay Balance...\n");

                    //get user's balance here
                    if(libraryModel.currentUser.getBalance() == 0){
                        libraryView.displayMessage("You have no payments due...Proceeding to Main Menu");

                    } else {
                        libraryView.displayMessage("Account Balance was: $" + formatter.format(libraryModel.currentUser.getBalance()));
                        libraryModel.currentUser.zeroBalance();
                        libraryView.displayMessage("Account Balance now: $0.00\n");
                        libraryView.displayMessage("Account balance has been paid...Updating Account Details\n");
                        libraryView.displayMessage("Account details updated...Proceeding to Main Menu\n");

                    }
                    onViewAccountFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();
                } else if (option == 3){ //View Main Menu
                    libraryView.displayMessage("You selected Main Menu...Proceeding to Main Menu\n");

                    onViewAccountFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();
                } else {
                    libraryView.displayErrorEnterNumInRange();
                    libraryView.displayAccountPage(libraryModel.currentUser);
                }
            } catch (InputMismatchException e){
                libraryView.displayErrorEnterInteger();
                libraryView.displayAccountPage(libraryModel.currentUser);
            }
        }
    }
    public void getEditAccountFlow(){
        boolean onEditAccountFlow = true;
        while(onEditAccountFlow){
            Scanner in = new Scanner(System.in);
            try {
                int option = in.nextInt();
                in.nextLine();
                if(option == 1){ //Edit Name
                    libraryView.displayMessage("You selected to edit name...Enter in your new name:");
                    String newName = in.nextLine();

                    //User input check.
                    while (!isLetters(newName)) {
                        libraryView.displayMessage("\nPlease enter your name with letters only.\n");
                        newName = in.nextLine();
                    }
                    editUsername(newName);
                    libraryView.displayMessage("\nUpdating Account Details...Proceeding back to Account Page\n");
                    onEditAccountFlow = false;
                    libraryView.displayAccountPage(libraryModel.currentUser);
                    getViewAccountFlow();

                } else if (option == 2){ //Edit Birthdate
                    libraryView.displayMessage("You selected to edit birthdate...Enter in your birth month:");
                    int birthMonth = in.nextInt();
                    while(birthMonth <= 0 || birthMonth >= 13){
                        //bad birth month
                        libraryView.displayMessage("\nPlease enter a valid birth month.\n");
                        birthMonth = in.nextInt();
                    }

                    libraryView.displayMessage("Enter in your birth day:");
                    int birthDay = in.nextInt();
                    while(birthDay <= 0 || birthDay >= 31){
                        libraryView.displayMessage("\nPlease enter a valid birth day.\n");
                        birthDay = in.nextInt();
                    }

                    libraryView.displayMessage("Enter in your birth year:");
                    int birthYear = in.nextInt();
                    while(birthYear <= 1902 || birthYear >= 2022){
                        libraryView.displayMessage("\nPlease enter a valid birth year.\n");
                        birthYear = in.nextInt();
                    }

                    //good birth month, day, and year
                    editBirthDate(LocalDate.of(birthYear, birthMonth, birthDay));
                    libraryView.displayMessage("\nUpdating Account Details...Proceeding back to Account Page\n");
                    onEditAccountFlow = false;
                    libraryView.displayAccountPage(libraryModel.currentUser);
                    getViewAccountFlow();

                } else if (option == 3) { //Edit Address
                    libraryView.displayMessage("You selected to edit address...Enter in your new address:");
                    String newAddress = in.nextLine();

                    //User input check.
                    while (!noSpecialCharacters(newAddress)) {
                        libraryView.displayMessage("\nPlease enter your address without special characters.\n");
                        newAddress = in.nextLine();
                    }
                    editAddress(newAddress);
                    libraryView.displayMessage("\nUpdating Account Details...Proceeding back to Account Page\n");
                    onEditAccountFlow = false;
                    libraryView.displayAccountPage(libraryModel.currentUser);
                    getViewAccountFlow();
                } else if (option == 4) { //Edit Phone Number
                    libraryView.displayMessage("You selected to edit phone number...Enter in your new phone number:");
                    String newPhoneNumber = in.nextLine();

                    //User input check.
                    while (!newPhoneNumber.matches("[0-9]+")) {
                        libraryView.displayMessage("\nPlease enter your phone number with numbers only.\n");
                    }
                    editPhoneNumber(newPhoneNumber);
                    libraryView.displayMessage("\nUpdating Account Details...Proceeding back to Account Page\n");
                    onEditAccountFlow = false;
                    libraryView.displayAccountPage(libraryModel.currentUser);
                    getViewAccountFlow();
                } else {
                    libraryView.displayErrorEnterNumInRange();
                    libraryView.displayEditAccountPage();
                }
            } catch (InputMismatchException e) {
                libraryView.displayErrorEnterInteger();
                libraryView.displayEditAccountPage();
            }
        }
    }

    //Cart Flows
    public void getViewCartFlow(){
        //get user's cart count
        if(libraryModel.currentUser.getCartCount() == 0){ //cart empty
            libraryView.displayEmptyCartMessage();

        } else { //cart not empty
            //get user's cart and print out items
            libraryView.displayUserCart(libraryModel.currentUser);
        }
        libraryView.displayMessage("Proceeding to Main Menu...\n");
        libraryView.displayMainMenu();
        getMainMenuFlow();
    }

    //Checkout Flows
    public void getCheckoutFlow(){
        //get user's cart status
        if(libraryModel.currentUser.getCartCount() == 0){ //cart empty
            libraryView.displayEmptyCartMessage();

            libraryView.displayMessage("Proceeding to Main Menu...\n");
            libraryView.displayMainMenu();
            getMainMenuFlow();
        } else { //cart not empty

            //print out user cart
            libraryView.displayUserCart(libraryModel.currentUser);

            //start checkout process
            getUserCheckoutFlow();
        }
    }
    public void getUserCheckoutFlow(){
        boolean onUserCheckoutFlow = true;
        while(onUserCheckoutFlow) {
            Scanner in = new Scanner(System.in);

            //user checkout flow
            libraryView.displayCheckoutOptionsPage();
            try {
                int option = in.nextInt();
                if (option == 1) { //Proceed with checkout
                    libraryView.displayMessage("Entering checkout process...\n");

                    CheckoutHandler newCheckoutHandler = new CheckoutHandler(libraryModel.currentUser);

                    if(newCheckoutHandler.userNeedsToPutBackCartItems()){
                        //child has too many items
                        libraryView.displayMessage("You have " + libraryModel.currentUser.getLoanManager().getLoanCount() +
                                                   " outstanding loans and " + libraryModel.currentUser.getCart().getItems().size() + " items in your cart.");
                        libraryView.displayMessage("You're only allowed a total of five items.");
                        libraryView.displayMessage("You need to return some items or remove some from your cart.");

                        //back out to main menu
                        libraryView.displayMainMenu();
                        getMainMenuFlow();
                    }

                    libraryView.displayMessage("Setting up loan details...\n");
                    libraryView.displayMessage("Today is " + LocalDate.now() );
                    libraryView.displayMessage("Loan details below: ");

                    newCheckoutHandler.checkOut();
                    libraryView.displayLoans(newCheckoutHandler.getLoans());

                    libraryView.displayMessage("Finished checkout process...Proceeding to Main Menu\n");
                    onUserCheckoutFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();
                } else if (option == 2) { //Return to main menu
                    libraryView.displayMessage("Exiting checkout...Proceeding to Main Menu\n");

                    onUserCheckoutFlow = false;
                    libraryView.displayMainMenu();
                    getMainMenuFlow();
                } else { //Integer out of range
                    libraryView.displayErrorEnterNumInRange();
                    libraryView.displayCheckoutPage();
                }
            } catch (InputMismatchException e) {
                libraryView.displayErrorEnterInteger();
                libraryView.displayCheckoutPage();
            }
        }
    }

    //Remove Item Flows
    public void getRemoveItemFlow(){
        //get user's cart status
        if(libraryModel.currentUser.getCartCount() == 0){ //cart empty
            libraryView.displayEmptyCartMessage();

            libraryView.displayMessage("Proceeding to Main Menu...\n");
            libraryView.displayMainMenu();
            getMainMenuFlow();
        } else { //cart not empty
            boolean onRemoveItemFlow = true;
            while(onRemoveItemFlow) {
                Scanner in = new Scanner(System.in);
                try {
                    //get user's cart and print out items
                    libraryView.displayUserCart(libraryModel.currentUser);

                    //get number of items in cart
                    int numItems = libraryModel.currentUser.getCartCount();

                    libraryView.displayRemoveItemSelectionPage();

                    int isbn = in.nextInt();
                    if (isbn > 999) { //good input
                        if(isbn > 9999 || isbn < 1000) { //bad input
                            libraryView.displayErrorEnterNumInRange();
                            libraryView.displayRemoveItemPage();
                        } else { //good input (option > 0 && option < numItems)
                            //option will be the value of the item to remove
                            //remove item from user's cart
                            LoanableItem currentItem = libraryModel.currentUser.getCart().lookupLoanableItemByISBN(isbn);
                            libraryModel.currentUser.removeItemFromCart(currentItem);

                            libraryView.displayMessage("Removing " + currentItem.getName() + " from your cart...\n");

                            onRemoveItemFlow = false;
                            libraryView.displayMessage(currentItem.getName() + " removed from cart...Proceeding to Main Menu\n");
                            libraryView.displayMainMenu();
                            getMainMenuFlow();
                        }

                    } else { //bad input
                        libraryView.displayErrorEnterPositiveInteger();
                        libraryView.displayRemoveItemPage();
                    }
                } catch (InputMismatchException e) {
                    libraryView.displayErrorEnterInteger();
                    libraryView.displayRemoveItemPage();
                }
            }
        }
    }

    //view the user's inventory to remind them of their borrowed books
    public void getUserInventoryFlow(){
        //get user's inventory count
        if(libraryModel.currentUser.getInventoryCount() == 0){ //inventory empty
            libraryView.displayEmptyInventoryMessage();

        } else { //inventory not empty
            //get user's inventory and print out items
            libraryView.displayUserInventory(libraryModel.currentUser);
        }
        libraryView.displayMessage("Proceeding to Main Menu...\n");
        libraryView.displayMainMenu();
        getMainMenuFlow();
    }

    public void getLogOutFlow(){
        //get user's cart count
        if(libraryModel.currentUser.getCartCount() > 0){ //cart not empty
            boolean isOnLogOut = true;
            while(isOnLogOut){
                Scanner in = new Scanner(System.in);
                try {
                    libraryView.displayMessage("You still have items in your cart.\nLogging out will clear your cart, do you want to proceed?");
                    libraryView.displayMessage("Option 1: Return to Main Menu\nOption 2: Proceed with log out");

                    int option = in.nextInt();
                    if(option == 1) {
                        //return to main menu
                        libraryView.displayMessage("Proceeding to Main Main menu\n");
                        isOnLogOut = false;
                        libraryView.displayMainMenu();
                        getMainMenuFlow();
                    } else if(option == 2) {
                        //proceed with log out
                        libraryView.displayMessage("Proceeding with log out...\nYou have been logged out");
                        libraryModel.currentUser.clearCart();
                        isOnLogOut = false;
                        getLandingFlow();
                    } else {
                        //enter integer in range
                        libraryView.displayErrorEnterNumInRange();
                    }
                } catch (InputMismatchException e){
                    //error please enter integer
                    libraryView.displayErrorEnterInteger();
                }
            }

        } else {
            //cart empty
            libraryView.displayMessage("You logged out..Proceeding to landing page...\n");
            getLandingFlow();
        }
    }


    //Helper Methods
    private boolean isLetters(String s) {
        return s.matches("[a-zA-Z]+");
    }

    private boolean noSpecialCharacters(String s) {
        return s.matches("[a-zA-Z0-9]*");
    }



    private boolean doesStringOnlyContainNum(String s){
        boolean doesStringOnlyContainNum;
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if(s == null){
            doesStringOnlyContainNum = false;
        } else {
            Matcher m = p.matcher(s);
            if(m.matches()){
                doesStringOnlyContainNum = true;
            } else {
                doesStringOnlyContainNum = false;
            }
        }
        return  doesStringOnlyContainNum;
    }

    public int generateISBN() {
        // Generate the ISBN.
        Random r = new Random();
        int isbn = r.nextInt(8999);
        isbn += 1000;
        while (libraryModel.getItemManager().lookupItemByISBN(isbn) != null) {
            isbn = r.nextInt(8999);
            isbn += 1000;
        }
        return isbn;
    }

    public int generateLibraryCardNumber() {
        Random r = new Random();
        int libcno = r.nextInt(8999);
        libcno += 1000;
        while(libraryModel.userManager.lookupUser(libcno) != null) {
            libcno = r.nextInt(8999);
            libcno += 1000;
        }
        return libcno;
    }

}