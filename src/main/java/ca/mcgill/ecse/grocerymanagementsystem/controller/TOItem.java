/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller;

// line 13 "../../../../../../resources/GMSTransferObjects.ump"
public class TOItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOItem Attributes
  private String name;
  private boolean isPerishable;
  private int quantityInInventory;
  private int price;
  private int numberOfPoints;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOItem(String aName, boolean aIsPerishable, int aQuantityInInventory, int aPrice, int aNumberOfPoints)
  {
    name = aName;
    isPerishable = aIsPerishable;
    quantityInInventory = aQuantityInInventory;
    price = aPrice;
    numberOfPoints = aNumberOfPoints;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getName()
  {
    return name;
  }

  public boolean getIsPerishable()
  {
    return isPerishable;
  }

  public int getQuantityInInventory()
  {
    return quantityInInventory;
  }

  public int getPrice()
  {
    return price;
  }

  public int getNumberOfPoints()
  {
    return numberOfPoints;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsPerishable()
  {
    return isPerishable;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "isPerishable" + ":" + getIsPerishable()+ "," +
            "quantityInInventory" + ":" + getQuantityInInventory()+ "," +
            "price" + ":" + getPrice()+ "," +
            "numberOfPoints" + ":" + getNumberOfPoints()+ "]";
  }
}