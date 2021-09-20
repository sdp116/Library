import LibraryItemsPackage.*;
import LibrarySystemPackage.*;
import ThingsALibraryNeedsPackage.*;
import java.time.LocalDate;

// TODO: format money amounts correctly

public class Main {
    static LibraryModel l;
    static LibraryView view;
    static LibraryController controller;

    public static void main(String[] args) {
        //Create Model View Controller
        l = new LibraryModel();
        view = new LibraryView();
        controller = new LibraryController(view, l);

        //Add various items to the model (libraryInventory)
        LoanableBook book = new LoanableBook("Harry Potter and the Chamber of Secrets", 1.50, controller.generateISBN());
        book.setAuthor("J. K. Rowling");
        book.setSubject("Fantasy");
        book.setPublisher("Bloomsbury Publishing");
        book.setPublicationDate(LocalDate.of(1998,7,2)); //needs adjusting possibly calendar date
        book.setBestSeller(true);
        l.addItemToItemManager(book);

        LoanableBook book2 = new LoanableBook("Harry Potter and the Sorcerer's Stone", 1.50, controller.generateISBN());
        book2.setAuthor("J. K. Rowling");
        book2.setSubject("Fantasy");
        book2.setPublisher("Bloomsbury Publishing");
        book2.setPublicationDate(LocalDate.of(1997,6,26)); //needs adjusting possibly calendar date
        book2.setBestSeller(true);
        l.addItemToItemManager(book2);

        LoanableBook book3 = new LoanableBook("Where the Wild Things Are", 6.50, controller.generateISBN());
        book3.setAuthor("Maurice Sendak");
        book3.setSubject("Fantasy");
        book3.setPublisher("Harpes & Row");
        book3.setPublicationDate(LocalDate.of(1963,11,25)); //needs adjusting possibly calendar date
        book3.setBestSeller(false);
        l.addItemToItemManager(book3);

        LoanableVideo video = new LoanableVideo("Avatar", 9.99, "James Cameron", controller.generateISBN());
        l.addItemToItemManager(video);

        LoanableVideo video2 = new LoanableVideo("Black Widow", 24.99, "Cate Shortland", controller.generateISBN());
        l.addItemToItemManager(video2);

        LoanableAudio audio = new LoanableAudio("Stronger", 3.99, "Kayne West", controller.generateISBN());
        l.addItemToItemManager(audio);

        LoanableAudio audio2 = new LoanableAudio("Origin of Symmetry",4.20,"Muse", controller.generateISBN());
        l.addItemToItemManager(audio2);

        //Reference Items
        ReferenceBook referenceBook = new ReferenceBook("Oxford English Dictionary", controller.generateISBN());
        referenceBook.setAuthor("N/A");
        referenceBook.setSubject("Dictionary");
        referenceBook.setPublisher("Oxford University Press");
        referenceBook.setPublicationDate(LocalDate.of(1884, 2, 1));
        l.addItemToItemManager(referenceBook);

        ReferenceMagazine referenceMagazine = new ReferenceMagazine("Sports Illustrated", controller.generateISBN());
        referenceMagazine.setAuthor("N/A");
        l.addItemToItemManager(referenceMagazine);


        //Add users to log in as (userManager)
        User buddy = new User("Buddy","911 Faren Ht", 666, "5121234562", LocalDate.of(1969, 6,22));
        l.addUserToUserManager(buddy);
//        buddy.addItemToCart(book3);

        User tester = new User("Joe", "123 West St", 112233, "5121234567", LocalDate.of(1995,1,10));
        l.addUserToUserManager(tester);

        User kid = new User("Kid", "Mom's House", 555, "512", LocalDate.of(2011,1,10));
        l.addUserToUserManager(kid);

        giveABunchOfCartItems(kid);

        User graduatingKid = new User("SoonToBeGraduate", "123 Main Dr.", 123, "512", LocalDate.of(2008,7,30));
        l.addUserToUserManager(graduatingKid);

        //testing if "child" who is now adult can checkout more than 5 items
//        graduatingKid.addItemToCart(video);
//        graduatingKid.addItemToCart(video2);
//        graduatingKid.addItemToCart(audio);
//        graduatingKid.addItemToCart(audio2);
//        graduatingKid.addItemToCart(book);
//        graduatingKid.addItemToCart(book2);

        // assign a fake loan to buddy
        assignFakeLoanToUser(buddy,book2);


        //additional testing
        //book2.setReservedBy(tester);

        //Starting the Library System
        //controller.getLandingFlow();

        makeEightThousandNineHundredNinetyUsers();
        checkIfLibraryCardNumbersAreUniqueAndPersistent();
        showLibraryKnowsUserInfo();
    }

    public static void assignFakeLoanToUser(User u, LoanableItem i) {
        Loan loan1 = new Loan(i);
        loan1.setDueDate(LocalDate.of(2020,6,30));
        u.getLoanManager().addLoan(loan1);
        u.getInventory().addItem(i);
        i.setCheckedOut(true);
    }

    public static void giveABunchOfCartItems(User u) {
        for (Item i : l.getItemManager().getItems()) {
            if (i instanceof LoanableItem) {
                u.addItemToCart((LoanableItem) i);
                if (u.getCart().getItems().size() >= 5) {
                    return;
                }
            }
        }
    }

    public static void makeEightThousandNineHundredNinetyUsers() {
        for(Integer i = 0; i < 8990; i++) {
            User newUser = new User(i.toString(),i.toString(), controller.generateLibraryCardNumber(), i.toString(), LocalDate.now());
            l.addUserToUserManager(newUser);
        }
    }

    ///
    /// 1. Each user has one unique library card for as long as they are in the system.
    ///
    public static void checkIfLibraryCardNumbersAreUniqueAndPersistent(){
        // TODO: demonstrate 1st requirement
        if(!areDuplicateLibraryCardNumbers()) {
            System.out.println("No duplicates here!");
        }
        else {
            System.out.println("Uh oh");
        }

        if(!usersLibraryCardNumberChangedBetweenLogins()) {
            System.out.println("Library card numbers persist!");
        }
        else {
            System.out.println("Uh oh");
        }
    }
    public static boolean areDuplicateLibraryCardNumbers() {
        for(User u : l.getUserManager().getUsers()) {
            for(User v : l.getUserManager().getUsers()) {
                if(!v.equals(u)) {
                    if(v.getCardNum() == u.getCardNum()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean usersLibraryCardNumberChangedBetweenLogins() {
        int[] userCardNumberOnFirstLogin = new int[l.getUserManager().getUsers().size()];
        int[] userCardNumberOnSecondLogin = new int[l.getUserManager().getUsers().size()];
        int i = 0;
        for(User u : l.getUserManager().getUsers()) {
            controller.logInUser(u);
            userCardNumberOnFirstLogin[i] = l.getCurrentUser().getCardNum();
            controller.logOutCurrentUser();
            i++;
        }

        i=0;
        for(User u : l.getUserManager().getUsers()) {
            controller.logInUser(u);
            userCardNumberOnSecondLogin[i] = l.getCurrentUser().getCardNum();
            controller.logOutCurrentUser();
            i++;
        }

        for(i = 0; i < userCardNumberOnFirstLogin.length; i++) {
            if(userCardNumberOnFirstLogin[i] != userCardNumberOnSecondLogin[i] && userCardNumberOnFirstLogin[i] != 0) {
                return true;
            }
        }
        return false;
    }
    ///
    ///
    ///

    ///
    /// 2. The library needs to know the name, address, phone number, and library card number for each user.
    ///
    public static void showLibraryKnowsUserInfo() {
            if(libraryKnowsTheNameAddressPhoneNumberAndLibraryCardNumber()) {
                    System.out.println("Library knows it all");
            }
            else {
                System.out.println("Uh oh");
            }
    }
    public static boolean libraryKnowsTheNameAddressPhoneNumberAndLibraryCardNumber() {
        for(User u : l.getUserManager().getUsers()) {
            if(!l.getUserManager().lookupUser(u.getCardNum()).getName().equals(u.getName())) {
                return false;
            }
            if(!l.getUserManager().lookupUser(u.getCardNum()).getAddress().equals(u.getAddress())) {
                return false;
            }
            if(!l.getUserManager().lookupUser(u.getCardNum()).getPhoneNum().equals(u.getPhoneNum())) {
                return false;
            }
            if(! (l.getUserManager().lookupUser(u.getCardNum()).getCardNum() == u.getCardNum())) {
                return false;

        }

    }
        return true;
}
    ///
    ///
    ///

    ///
    /// 3. At any particular point in time, the library may need to know or to calculate the items a user has checked out, when they are due, and any outstanding overdue fines.
    // TODO: this^
    ///
    ///
    ///

    ///
    ///4. Children (<=12) can only check out five items at a time.
    ///
    // could make a user for every possible bday and have them all try to check out 6 items at once
    // or try to check out 1 item when they have 5 etc
    ///
    ///
    ///

    ///
    ///5.    A user can check out books or audio/video materials.
    ///
    //      flow
    ///
    ///
    ///

    ///
    ///6.    Books are checked out for three weeks, unless they are current best sellers, in which case the limit is two weeks.
    ///
    //
    ///     flow
    ///
    ///

    ///
    ///7.    Audio/video materials may be checked out for two weeks.
    ///
    //      flow
    ///
    ///
    ///

    ///
    ///8.    The overdue fine is ten cents per item per day, but cannot be higher than the value of the overdue item.
    ///
    //      could generate loans of different levels of overdueness, compare values to expected values and max value
    ///
    ///
    ///

    ///
    ///9.    The library also has reference books and magazines, which cannot be checked out.
    ///
    //       flow
    ///
    ///
    ///

    ///
    ///10.  A user can request a book or audio/video item that is not currently in.
    ///
    //      flow
    ///
    ///
    ///

    ///
    ///11.  A user can renew an item once (and only once), unless there is an outstanding request for the item, in which case the user must return it.
    ///
    //
    ///
    ///
    ///

}
