package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;

import java.sql.Date;


class TOConverter {

    public static TOUser convert(User user) {
        String address = null;
        String role = "";

        for (UserRole r : user.getRoles()){
            if (r instanceof Customer) {
                Customer c = (Customer) r;
                address = c.getAddress();
                role += "Customer ";
            } 
            else if (r instanceof Employee) {
                role += "Employee ";
            }
        }

        return new TOUser(
            user.getUsername(),
            user.getName(),
            user.getPhoneNumber(),
            role.strip(),
            address);
    }

    public static TOItem convert(Item item) {
        return new TOItem(
            item.getName(),
            item.getIsPerishable(),
            item.getQuantityInInventory(),
            item.getPrice(),
            item.getNumberOfPoints()
        );
    }
    public static TOShipment convert(Shipment shipment) {
        int n = shipment.numberOfShipmentItems();
        TOShipmentItem[] shipmentItems = new TOShipmentItem[n];
        for (int i = 0; i<n; i++){
            shipmentItems[i] = convert(shipment.getShipmentItem(i));
        }
        return new TOShipment(
            shipment.getShipmentNumber(),
            shipment.getDateOrdered(),
            shipmentItems
        );
    }
    public static TOShipmentItem convert(ShipmentItem shipmentItem) {
        return new TOShipmentItem(shipmentItem.getQuantity(), convert(shipmentItem.getItem()));
    }

    public static TOOrder convert(Order order) {

        int n = order.numberOfOrderItems();
        TOOrderItem[] toItems = new TOOrderItem[n];
        for (int i = 0; i < n; i++) {
            OrderItem orderItem = order.getOrderItem(i);
            toItems[i] = convert(orderItem);
        }

        double total = order.getOrderItems().stream()
                .mapToDouble(oi -> oi.getQuantity() * oi.getItem().getPrice())
                .sum();

        java.util.Date placed = order.getDatePlaced();
        Date deadline = placed == null ? null : new Date(placed.getTime());

        String status   = order.getStateOrder().name();
        String assignee = order.hasOrderAssignee() ? order.getOrderAssignee().getUser().getUsername() : "";
        return new TOOrder(
                order.getOrderNumber(),
                deadline,
                total,
                status,
                assignee,
                toItems
        );
    }
    public static TOOrderItem convert(OrderItem orderItem) {
        return new TOOrderItem(
                orderItem.getQuantity(),
                convert(orderItem.getItem())
        );
    }
}