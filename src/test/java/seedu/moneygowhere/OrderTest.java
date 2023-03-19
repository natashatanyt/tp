package seedu.moneygowhere;

import item.Item;
import order.Order;
import order.OrderEntry;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    @Test
    void orderEntryTest() {

        Item item = new Item("chicken rice", 2.00);
        OrderEntry orderEntry = new OrderEntry(item, 10);

        assertEquals(10, orderEntry.getQuantity());
        assertEquals("chicken rice", orderEntry.getItem().getName());
        assertEquals(2.00, orderEntry.getItem().getPrice());

    }

    @Test
    void orderTest() {

        Item item1 = new Item("chicken rice", 2.00);
        Item item2 = new Item("bubble tea", 5.00);

        OrderEntry orderEntry1 = new OrderEntry(item1, 10);
        OrderEntry orderEntry2 = new OrderEntry(item2, 10);

        ArrayList<OrderEntry> orderEntries = new ArrayList<>();
        orderEntries.add(orderEntry1);
        orderEntries.add(orderEntry2);

        Order order = new Order(orderEntries);

        assertEquals(70, order.getSubTotal());

        assertEquals("chicken rice", order.getOrderEntries().get(0).getItem().getName());
        assertEquals("bubble tea", order.getOrderEntries().get(1).getItem().getName());

        assertEquals(2, order.getOrderEntries().get(0).getItem().getPrice());
        assertEquals(5, order.getOrderEntries().get(1).getItem().getPrice());

    }

    @Test
    void refundOrderTest() {

        Item item1 = new Item("chicken rice", 2.00);

        OrderEntry orderEntry1 = new OrderEntry(item1, 10);

        ArrayList<OrderEntry> orderEntries = new ArrayList<>();
        orderEntries.add(orderEntry1);
        Order order = new Order(orderEntries);

        assertEquals("COMPLETED", order.getStatus());

    }

}