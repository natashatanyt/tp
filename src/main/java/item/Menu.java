package item;

import java.io.IOException;
import java.util.ArrayList;

import java.lang.reflect.Type;

import app.Command;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParseException;
import exception.ItemException;
import org.apache.commons.lang3.StringUtils;
import utility.Store;
import utility.Ui;
import validation.item.AddItemValidation;
import validation.item.DeleteItemValidation;
import validation.item.FindItemValidation;
import validation.item.UpdateItemValidation;

public class Menu {

    private ArrayList<Item> items;
    private Store store;

    public Menu() {
        this.store = new Store("menu.json");
        Type type = new TypeToken<ArrayList<Item>>() {
        }.getType();

        try {
            this.items = store.load(type);
        } catch (IOException | JsonParseException | NumberFormatException e) {
            System.out.println(e.getMessage());
            this.items = new ArrayList<>();
        }
    }

    public Menu(boolean isTest) {
        this.items = new ArrayList<>();
    }

    public void displayList() {
        Ui ui = new Ui();
        ui.printMenu(items);
        ui.printCommandSuccess("listitem");
    }

    public void appendItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(int index) {
        this.items.remove(index);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Adds an item and its price onto the menu.
     *
     * @param command the Command object containing the search term
     * @throws ItemException if the command format, name or price is invalid
     */
    public void addItem(Command command) throws ItemException {
        AddItemValidation addItemValidation = new AddItemValidation();
        try {
            addItemValidation.validateFlags(command);
            command.mapArgumentAlias(addItemValidation.LONG_NAME_FLAG, addItemValidation.SHORT_NAME_FLAG);
            command.mapArgumentAlias(addItemValidation.LONG_PRICE_FLAG, addItemValidation.SHORT_PRICE_FLAG);
            addItemValidation.validateCommand(command, this);
        } catch (ItemException e) {
            throw new ItemException(e.getMessage());
        }

        String name = command.getArgumentMap().get(addItemValidation.LONG_NAME_FLAG);
        Double price = Double.valueOf(command.getArgumentMap().get(addItemValidation.LONG_PRICE_FLAG));
        Item item = new Item(name, price);
        appendItem(item);
        assert this.getItem(this.getItems().size() - 1).getName().equals(item.getName())
                : "Item failed to append";
        save();
    }

    /**
     * Updates a specified item on the menu by its given index.
     *
     * @param command the Command object containing the search term
     * @throws ItemException if the command format is invalid
     *                       or index does not exist
     */
    public void updateItem(Command command) throws ItemException {
        if (this.getItems().size() == 0) {
            Ui ui = new Ui();
            throw new ItemException(ui.getEmptyMenu());
        }

        UpdateItemValidation updateItemValidation = new UpdateItemValidation();

        try {
            updateItemValidation.validateFlags(command);
            command.mapArgumentAlias(updateItemValidation.LONG_INDEX_FLAG, updateItemValidation.SHORT_INDEX_FLAG);
            command.mapArgumentAlias(updateItemValidation.LONG_NAME_FLAG, updateItemValidation.SHORT_NAME_FLAG);
            command.mapArgumentAlias(updateItemValidation.LONG_PRICE_FLAG, updateItemValidation.SHORT_PRICE_FLAG);
            updateItemValidation.validateCommand(command, this);
        } catch (ItemException e) {
            throw new ItemException(e.getMessage());
        }

        int index = Integer.parseInt(command.getArgumentMap().get(updateItemValidation.LONG_INDEX_FLAG));

        if (command.getArgumentMap().containsKey(updateItemValidation.LONG_NAME_FLAG)) {
            this.getItem(index).setName(command.getArgumentMap().get(updateItemValidation.LONG_NAME_FLAG));
        }

        if (command.getArgumentMap().containsKey(updateItemValidation.LONG_PRICE_FLAG)) {
            Double price = Double.valueOf(command.getArgumentMap().get(updateItemValidation.LONG_PRICE_FLAG));
            this.getItem(index).setPrice(price);
        }
        save();
    }

    /**
     * Deletes a specified item on the menu by its given index.
     *
     * @param command the Command object containing the search term
     * @throws ItemException if the command format is invalid
     *                       or index does not exist
     */
    public void deleteItem(Command command) throws ItemException {
        if (this.getItems().size() == 0) {
            Ui ui = new Ui();
            throw new ItemException(ui.getEmptyMenu());
        }

        DeleteItemValidation deleteItemValidation = new DeleteItemValidation();
        try {
            deleteItemValidation.validateFlags(command);
            command.mapArgumentAlias(deleteItemValidation.LONG_INDEX_FLAG, deleteItemValidation.SHORT_INDEX_FLAG);
            deleteItemValidation.validateCommand(command, this);
        } catch (ItemException e) {
            throw new ItemException(e.getMessage());
        }

        int index = Integer.parseInt(command.getArgumentMap().get(deleteItemValidation.LONG_INDEX_FLAG));
        removeItem(index);
        save();
    }

    /**
     * Finds the index of the first item in the provided ArrayList of Item objects
     * whose name contains the specified itemName, case-insensitively.
     *
     * @param itemName the name of the item to search for, case-insensitively
     * @return the index of the first matching item if found, or -1 if no matching item is found
     */
    public int findItemIndex(String itemName) {

        Ui ui = new Ui();
        ArrayList<Item> menu = this.getItems();
        itemName = itemName.toLowerCase();

        if (itemName.contains("\"")) {
            itemName = itemName.replace("\"", "");
        }

        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).getName().toLowerCase().contains(itemName)) {
                return i;
            }
        }

        ui.printItemNotFound();
        return -1;

    }

    /**
     * Finds the indexes of all items in the provided ArrayList of Item objects
     * whose names contain the specified itemName, case-insensitively.
     * If itemName is an exact match for an item's name, only the index of that item is returned.
     *
     * @param itemName the name of the item to search for, case-insensitively
     * @return an ArrayList of integers containing the indexes of all matching items,
     * or an empty list if no matching item is found
     */
    public ArrayList<Integer> findMatchingItemNames(String itemName) {

        ArrayList<Integer> itemIndexes = new ArrayList<>();
        ArrayList<Item> menu = this.getItems();
        itemName = itemName.toLowerCase();

        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).getName().toLowerCase().contains(itemName)) {
                if (itemName.equals(menu.get(i).getName().toLowerCase())) {
                    itemIndexes.removeAll(itemIndexes);
                    itemIndexes.add(i);
                    break;
                }
                itemIndexes.add(i);
            }
        }

        return itemIndexes;
    }

    /**
     * Displays a header followed by the names and prices of all items
     * in the provided ArrayList of Item objects whose names contain
     * the search term specified in the provided Command object.
     *
     * @param command the Command object containing the search term
     */
    public void showResultsOfFind(Command command) throws ItemException {

        Ui ui = new Ui();
        FindItemValidation findItemValidation = new FindItemValidation();

        ArrayList<Item> menu = this.getItems();
        ArrayList<Integer> indexes = new ArrayList<>();

        String itemName = command.getArgumentString().trim();

        if (!findItemValidation.validateName(itemName)) {
            return;
        }

        if (itemName.contains("\"")) {
            itemName = itemName.replace("\"", "");
        }

        for (int i = 0; i < menu.size(); i++) {
            if (StringUtils.containsIgnoreCase(menu.get(i).getName(), itemName)) {
                indexes.add(i);
            }
        }

        if (indexes.size() == 0) {
            ui.printNoItemsFound(itemName);
            return;
        }

        ui.printMenuHeader();
        for (int i = 0; i < indexes.size(); i++) {
            ui.printFindItem(indexes.get(i), menu);
        }
    }

    public void save() {
        try {
            store.save(items);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
