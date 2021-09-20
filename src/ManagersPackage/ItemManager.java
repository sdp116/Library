package ManagersPackage;
import LibraryItemsPackage.*;
import ThingsALibraryNeedsPackage.Loan;

import java.util.*;

public class ItemManager extends Manager { // may just extend treeset
    private TreeSet<Item> items;

    public ItemManager() {
        items = new TreeSet<Item>();
    }
    public ItemManager(ArrayList<Item> is) {
        items = new TreeSet<Item>();
        for(Item i : is) {
            items.add(i);
        }
    }

    public void addItem(Item i) {
        items.add(i);
    }
    public void removeItem(Item i) {
        items.remove(i);
    }

    public TreeSet<Item> getItems() {
        return items;
    }

    public Item lookupItemByISBN(int isbn) {
        for(Item i : items) {
            if (i.getISBN() == isbn)
                return i;
        }
        return null;
    }

    public LoanableItem lookupLoanableItemByISBN(int isbn) {
        for(Item i : items) {
            if (i.getISBN() == isbn) {
                if (i instanceof LoanableItem)
                return (LoanableItem) i;
            }        }
        return null;
    }
    
    public ArrayList<Item> lookupItemByName(String query) {
        ArrayList<Item> results = new ArrayList<>();
        String[] queryParts = query.split("[^a-zA-Z0-9]", 0);

        for(Item i : items) {
            if (i.getName().equalsIgnoreCase(query)) {
                results.add(i);
            }

            else {
                String[] itemNameParts = i.getName().split("[^a-zA-Z0-9]",0);
                if(atLeastOneStringPartMatches(itemNameParts, queryParts)) {
                    results.add(i);
                }
            }
        }
        return results;
    }

    private boolean atLeastOneStringPartMatches(String[] nameParts, String[] queryParts) {
        for(String s : nameParts) {
            for (String t : queryParts) {
                if (s.equalsIgnoreCase(t))
                    return true;
            }
        }
        return false;
    }
}
