/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller;
import java.sql.Date;
import java.util.*;

// line 22 "../../../../../../resources/GMSTransferObjects.ump"
public class TOShipment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOShipment Attributes
  private int shipmentNumber;
  private Date dateOrdered;

  //TOShipment Associations
  private List<TOShipmentItem> tOShipmentItems;

  //Helper Variables
  private boolean canSetTOShipmentItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOShipment(int aShipmentNumber, Date aDateOrdered, TOShipmentItem... allTOShipmentItems)
  {
    shipmentNumber = aShipmentNumber;
    dateOrdered = aDateOrdered;
    canSetTOShipmentItems = true;
    tOShipmentItems = new ArrayList<TOShipmentItem>();
    boolean didAddTOShipmentItems = setTOShipmentItems(allTOShipmentItems);
    if (!didAddTOShipmentItems)
    {
      throw new RuntimeException("Unable to create TOShipment, must not have duplicate tOShipmentItems. See https://manual.umple.org?RE001ViolationofImmutability.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getShipmentNumber()
  {
    return shipmentNumber;
  }

  public Date getDateOrdered()
  {
    return dateOrdered;
  }
  /* Code from template association_GetMany */
  public TOShipmentItem getTOShipmentItem(int index)
  {
    TOShipmentItem aTOShipmentItem = tOShipmentItems.get(index);
    return aTOShipmentItem;
  }

  public List<TOShipmentItem> getTOShipmentItems()
  {
    List<TOShipmentItem> newTOShipmentItems = Collections.unmodifiableList(tOShipmentItems);
    return newTOShipmentItems;
  }

  public int numberOfTOShipmentItems()
  {
    int number = tOShipmentItems.size();
    return number;
  }

  public boolean hasTOShipmentItems()
  {
    boolean has = tOShipmentItems.size() > 0;
    return has;
  }

  public int indexOfTOShipmentItem(TOShipmentItem aTOShipmentItem)
  {
    int index = tOShipmentItems.indexOf(aTOShipmentItem);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTOShipmentItems()
  {
    return 0;
  }
  /* Code from template association_SetUnidirectionalMany */
  private boolean setTOShipmentItems(TOShipmentItem... newTOShipmentItems)
  {
    boolean wasSet = false;
    if (!canSetTOShipmentItems) { return false; }
    canSetTOShipmentItems = false;
    ArrayList<TOShipmentItem> verifiedTOShipmentItems = new ArrayList<TOShipmentItem>();
    for (TOShipmentItem aTOShipmentItem : newTOShipmentItems)
    {
      if (verifiedTOShipmentItems.contains(aTOShipmentItem))
      {
        continue;
      }
      verifiedTOShipmentItems.add(aTOShipmentItem);
    }

    if (verifiedTOShipmentItems.size() != newTOShipmentItems.length)
    {
      return wasSet;
    }

    tOShipmentItems.clear();
    tOShipmentItems.addAll(verifiedTOShipmentItems);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "shipmentNumber" + ":" + getShipmentNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dateOrdered" + "=" + (getDateOrdered() != null ? !getDateOrdered().equals(this)  ? getDateOrdered().toString().replaceAll("  ","    ") : "this" : "null");
  }
}