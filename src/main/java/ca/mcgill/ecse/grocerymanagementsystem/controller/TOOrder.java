/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller;
import java.sql.Date;
import java.util.*;

// line 35 "../../../../../../model.ump"
// line 74 "../../../../../../model.ump"
public class TOOrder
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //TOOrder Attributes
    private int orderNumber;
    private Date deadline;
    private double totalPrice;
    private String status;
    private String assignee;

    //TOOrder Associations
    private List<TOOrderItem> tOOrderItems;

    //Helper Variables
    private boolean canSetTOOrderItems;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public TOOrder(int aOrderNumber, Date aDeadline, double aTotalPrice, String aStatus, String aAssignee, TOOrderItem... allTOOrderItems)
    {
        orderNumber = aOrderNumber;
        deadline = aDeadline;
        totalPrice = aTotalPrice;
        status = aStatus;
        assignee = aAssignee;
        canSetTOOrderItems = true;
        tOOrderItems = new ArrayList<TOOrderItem>();
        boolean didAddTOOrderItems = setTOOrderItems(allTOOrderItems);
        if (!didAddTOOrderItems)
        {
            throw new RuntimeException("Unable to create TOOrder, must not have duplicate tOOrderItems. See https://manual.umple.org?RE001ViolationofImmutability.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public int getOrderNumber()
    {
        return orderNumber;
    }

    public Date getDeadline()
    {
        return deadline;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public String getStatus()
    {
        return status;
    }

    public String getAssignee()
    {
        return assignee;
    }
    /* Code from template association_GetMany */
    public TOOrderItem getTOOrderItem(int index)
    {
        TOOrderItem aTOOrderItem = tOOrderItems.get(index);
        return aTOOrderItem;
    }

    public List<TOOrderItem> getTOOrderItems()
    {
        List<TOOrderItem> newTOOrderItems = Collections.unmodifiableList(tOOrderItems);
        return newTOOrderItems;
    }

    public int numberOfTOOrderItems()
    {
        int number = tOOrderItems.size();
        return number;
    }

    public boolean hasTOOrderItems()
    {
        boolean has = tOOrderItems.size() > 0;
        return has;
    }

    public int indexOfTOOrderItem(TOOrderItem aTOOrderItem)
    {
        int index = tOOrderItems.indexOf(aTOOrderItem);
        return index;
    }
    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfTOOrderItems()
    {
        return 0;
    }
    /* Code from template association_SetUnidirectionalMany */
    private boolean setTOOrderItems(TOOrderItem... newTOOrderItems)
    {
        boolean wasSet = false;
        if (!canSetTOOrderItems) { return false; }
        canSetTOOrderItems = false;
        ArrayList<TOOrderItem> verifiedTOOrderItems = new ArrayList<TOOrderItem>();
        for (TOOrderItem aTOOrderItem : newTOOrderItems)
        {
            if (verifiedTOOrderItems.contains(aTOOrderItem))
            {
                continue;
            }
            verifiedTOOrderItems.add(aTOOrderItem);
        }

        if (verifiedTOOrderItems.size() != newTOOrderItems.length)
        {
            return wasSet;
        }

        tOOrderItems.clear();
        tOOrderItems.addAll(verifiedTOOrderItems);
        wasSet = true;
        return wasSet;
    }

    public void delete()
    {}


    public String toString()
    {
        return super.toString() + "["+
                "orderNumber" + ":" + getOrderNumber()+ "," +
                "totalPrice" + ":" + getTotalPrice()+ "," +
                "status" + ":" + getStatus()+ "," +
                "assignee" + ":" + getAssignee()+ "]" + System.getProperties().getProperty("line.separator") +
                "  " + "deadline" + "=" + (getDeadline() != null ? !getDeadline().equals(this)  ? getDeadline().toString().replaceAll("  ","    ") : "this" : "null");
    }
}


