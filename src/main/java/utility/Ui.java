package utility;

import app.Command;
import order.Order;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * All print command will be done through here.
 */
public class Ui {

    public final String NULL_MESSAGE = "Input is empty. Please enter something.";
    public final String ERROR_MESSAGE = "Please do not use special characters such as ';' and ':'.";
    public final String INTEGER_ERROR = "Argument needs to be an integer";
    public final String MISSING_ORDER_ARGUMENT = "Please use -i or --item and -d or --done to add order.";
    public final String ITEM_NAME_MIN_LENGTH_ERROR = "Name cannot be empty.";
    public final String ITEM_NAME_MAX_LENGTH_ERROR = "Name exceeds the 25 character limit.";
    public final String ITEM_PRICE_MIN_LENGTH_ERROR = "Price cannot be empty.";
    public final String ITEM_PRICE_NEGATIVE_ERROR = "Price cannot be negative.";
    public final String INVALID_ADDITEM_FORMAT = "additem command format is invalid.";
    public final String INVALID_DELETEITEM_FORMAT = "deleteitem command format is invalid.";
    public final String PRICE_DECIMAL_ERROR = "Price must have at most 2 decimal points.";
    public final String INVALID_PRICE_ERROR = "Price must be a number.";
    public final String SUCCESSFUL_COMMAND = "Successfully executed your command!";
    public final String PROMPT_MESSAGE = "Please enter again:";

    public Ui() {
    }

    public void printUserInput() {
        System.out.println("Please enter something: ");
    }

    public void printInvalidCommand(String command) { System.out.println("The command: " + command + " is not a valid command.");}

    public void printInvalidIndex() {
        System.out.println("Please enter a valid index!");
    }

    public void printRequiresInteger() {
        System.out.println("This input requires a whole number!");
    }

    public void printTableHeader(String col1, String col2, String col3) {
        System.out.printf("| %-5s | %-25s | %-5s |\n", col1, col2, col3);
        System.out.println("| " + "-".repeat(5) + " | " + "-".repeat(25) + " | " + "-".repeat(5) + " |");
    }

    public void printItemMenu(int index, String name, double price) {
        System.out.printf("| %-5d | %-25s | %-5.2f |\n", index, name, price);
    }

    /**
     * Prints string to user and moves the cursor to a new line.
     */
    public void println(Object string) {
        System.out.println(string);
    }

    /**
     * Prints string to user without moving the cursor to a new line.
     */
    public void print(Object string) {
        System.out.print(string);
    }

    /**
     * Prints the list of orders.
     * This includes the subtotal cost of each order.
     *
     * @param orders Arraylist of orders stored
     */
    public void printOrderList(ArrayList<Order> orders) {

        DecimalFormat df = new DecimalFormat("#.00");

        System.out.println("================================================");

        for (int i = 0; i < orders.size(); i++) {

            System.out.println("Order " + (i + 1));
            System.out.println("Order ID: " + orders.get(i).getOrderId());
            System.out.println("Order time: " + orders.get(i).getDateTime());

            for (int j = 0; j < orders.get(i).getOrderEntries().size(); j++) {
                System.out.println((j + 1) + ". "
                        + orders.get(i).getOrderEntries().get(j).getItem().getName()
                        + " x" + orders.get(i).getOrderEntries().get(j).getQuantity());

            }

            String subtotal = df.format(orders.get(i).getSubTotal());
            System.out.println("\nSubtotal: $" + subtotal);

            System.out.println("================================================");

        }


    }
}
