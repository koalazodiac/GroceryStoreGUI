/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller;

// line 28 "../../../../../../resources/GMSTransferObjects.ump"
public class TOShipmentItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOShipmentItem Attributes
  private int quantity;

  //TOShipmentItem Associations
  private TOItem tOItem;

  //Helper Variables
  private boolean canSetTOItem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOShipmentItem(int aQuantity, TOItem aTOItem)
  {
    quantity = aQuantity;
    canSetTOItem = true;
    if (!setTOItem(aTOItem))
    {
      throw new RuntimeException("Unable to create TOShipmentItem due to aTOItem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getQuantity()
  {
    return quantity;
  }
  /* Code from template association_GetOne */
  public TOItem getTOItem()
  {
    return tOItem;
  }
  /* Code from template association_SetUnidirectionalOne */
  private boolean setTOItem(TOItem aNewTOItem)
  {
    boolean wasSet = false;
    if (!canSetTOItem) { return false; }
    canSetTOItem = false;
    if (aNewTOItem != null)
    {
      tOItem = aNewTOItem;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "quantity" + ":" + getQuantity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "tOItem = "+(getTOItem()!=null?Integer.toHexString(System.identityHashCode(getTOItem())):"null");
  }
}