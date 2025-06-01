package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.HashSet;
import java.util.Set;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

public class GMSView {

    public static final EventType<Event> REFRESH = new EventType<>("REFRESH");
    private static final Set<Node> refreshableNodes = new HashSet<>();

    public static void registerRefreshableNode(Node n) {
        refreshableNodes.add(n);
    }

    public static void refresh() {
        for (Node n : refreshableNodes) {
            n.fireEvent(new Event(REFRESH));
        }
    }
}
