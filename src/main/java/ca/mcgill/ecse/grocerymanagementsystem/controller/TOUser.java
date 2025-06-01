/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.controller;

// line 3 "../../../../../../resources/GMSTransferObjects.ump"
public class TOUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOUser Attributes
  private String username;
  private String name;
  private String phone;
  private String role;
  private String address;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOUser(String aUsername, String aName, String aPhone, String aRole, String aAddress)
  {
    username = aUsername;
    name = aName;
    phone = aPhone;
    role = aRole;
    address = aAddress;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getUsername()
  {
    return username;
  }

  public String getName()
  {
    return name;
  }

  public String getPhone()
  {
    return phone;
  }

  public String getRole()
  {
    return role;
  }

  public String getAddress()
  {
    return address;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "name" + ":" + getName()+ "," +
            "phone" + ":" + getPhone()+ "," +
            "role" + ":" + getRole()+ "," +
            "address" + ":" + getAddress()+ "]";
  }
}