package app;

import exception.item.ItemException;
import exception.OrderException;
import item.Menu;
import item.MenuAssistant;
import order.Order;
import order.OrderAssistant;
import order.Transaction;
import payment.Refund;
import ui.MenuUi;
import ui.TransactionUi;
import utility.Ui;

/**
 * The Router class handles the routing of commands, based on whether they start with a slash (/) or not.
 * It contains methods for processing the different types of commands and directing them to the appropriate
 * classes for execution.
 */
public class Router {
    public Ui ui;
    public MenuUi menuUi;
    public TransactionUi transactionUi;
    public Menu menu;
    public Transaction transactions;

    /**
     * Constructs a new Router object with the specified menu and transactions.
     *
     * @param menu         the menu object to use for item-related commands
     * @param transactions the transactions object to use for order-related commands
     */
    public Router(Menu menu, Transaction transactions, Ui ui) {
        this.ui = ui;
        this.menu = menu;
        this.transactions = transactions;
    }

    /**
     * Processes professional commands (starting with a slash (/)) and directs them to the appropriate method
     * for execution.
     *
     * @param command the Command object containing the professional command to process
     */
    private void proRoute(Command command) {
        try {
            switch (command.getCommand()) {
            case "/help":
                ui.printHelp();
                break;
            case "/additem":
                menu.addItem(command);
                menuUi.printSuccessfulAddItem();
                break;
            case "/deleteitem":
                menu.deleteItem(command);
                menuUi.printSuccessfulDeleteItem();
                break;
            case "/listitem":
                menu.displayList();
                break;
            case "/updateitem":
                menu.updateItem(command);
                menuUi.printSuccessfulUpdateItem();
                break;
            case "/finditem":
                menu.showResultsOfFind(command);
                menuUi.printFindItemComplete();
                break;
            case "/addorder":
                Order order = new Order(command, menu, transactions);
                transactionUi.printSuccessfulAddOrder();
                break;
            case "/listorder":
                transactions.displayList();
                transactionUi.printSuccessfulListOrder();
                break;
            case "/refundorder":
                Refund refund = new Refund();
                refund.refundTransaction(command, transactions);
                transactionUi.printSuccessfulRefundOrder();
                break;
            default:
                ui.printInvalidCommand(command.getCommand());
            }

        } catch (ItemException | OrderException e) {
            ui.println(e.getMessage());
        }
    }

    /**
     * Processes assisted commands (not starting with a slash (/)) and directs them to the appropriate method
     * for execution.
     *
     * @param command the Command object containing the assisted command to process
     */
    private void assistRoute(Command command) {

        MenuAssistant menuAssistant = new MenuAssistant();
        OrderAssistant orderAssistant = new OrderAssistant();
        boolean isCancelled;

        try {
            switch (command.getCommand()) {
            case "?":
            case "help":
                ui.printHelp();
                break;
            case "1":
            case "additem":
                isCancelled = menuAssistant.addItem(command, menu);
                menuAssistant.printResult(command, isCancelled);
                break;
            case "2":
            case "deleteitem":
                isCancelled = menuAssistant.deleteItem(command, menu);
                menuAssistant.printResult(command, isCancelled);
                break;
            case "3":
            case "listitem":
                menu.displayList();
                break;
            case "4":
            case "updateitem":
                isCancelled = menuAssistant.updateItem(command, menu);
                menuAssistant.printResult(command, isCancelled);
                break;
            case "5":
            case "finditem":
                isCancelled = menuAssistant.showResultsOfFind(command, menu);
                menuAssistant.printResult(command, isCancelled);
                break;
            case "6":
            case "addorder":
                isCancelled = orderAssistant.assistedAddOrder(menu, transactions);
                menuAssistant.printResult(command, isCancelled);
                break;
            case "7":
            case "listorder":
                transactions.displayList();
                break;
            default:
                ui.printInvalidCommand(command.getCommand());
            }
        } catch (OrderException e) {
//            new MenuUi().printError(Flags.Error.);
            ui.println(e.getMessage());
        }
    }

    /**
     * This method handles the routing of commands based on the first character of the command string.
     * If the first character of the command is "/", the method invokes the proRoute method to process the command.
     * If the first character of the command is not "/", the method invokes the assistRoute method to process the
     * command.
     *
     * @param command The command to be processed.
     */
    public void handleRoute(Command command) {
        if (command.getCommand().charAt(0) == '/') {
            proRoute(command);
        } else {
            assistRoute(command);
        }
    }
}