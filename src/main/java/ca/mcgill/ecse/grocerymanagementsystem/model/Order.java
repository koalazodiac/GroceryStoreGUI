/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.grocerymanagementsystem.model;
import java.sql.Date;
import java.util.*;

// line 39 "../../../../../../resources/GroceryManagementSystem.ump"
// line 3 "../../../../../../resources/GroceryState.ump"
public class Order
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DeliveryDeadline { SameDay, InOneDay, InTwoDays, InThreeDays }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextOrderNumber = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private Date datePlaced;
  private DeliveryDeadline deadline;
  private int totalCost;
  private int pricePaid;

  //Autounique Attributes
  private int orderNumber;

  //Order State Machines
  public enum StateOrder { UnderConstruction, Pending, Placed, InPreparation, ReadyForDelivery, Delivered, Cancelled }
  private StateOrder stateOrder;

  //Order Associations
  private GroceryManagementSystem groceryManagementSystem;
  private List<OrderItem> orderItems;
  private Customer orderPlacer;
  private Employee orderAssignee;

  //Helper Variables
  private boolean canSetTotalCost;
  private boolean canSetPricePaid;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aDatePlaced, DeliveryDeadline aDeadline, GroceryManagementSystem aGroceryManagementSystem, Customer aOrderPlacer)
  {
    datePlaced = aDatePlaced;
    deadline = aDeadline;
    canSetTotalCost = true;
    canSetPricePaid = true;
    orderNumber = nextOrderNumber++;
    boolean didAddGroceryManagementSystem = setGroceryManagementSystem(aGroceryManagementSystem);
    if (!didAddGroceryManagementSystem)
    {
      throw new RuntimeException("Unable to create order due to groceryManagementSystem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderItems = new ArrayList<OrderItem>();
    boolean didAddOrderPlacer = setOrderPlacer(aOrderPlacer);
    if (!didAddOrderPlacer)
    {
      throw new RuntimeException("Unable to create ordersPlaced due to orderPlacer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStateOrder(StateOrder.UnderConstruction);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDatePlaced(Date aDatePlaced)
  {
    boolean wasSet = false;
    datePlaced = aDatePlaced;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeadline(DeliveryDeadline aDeadline)
  {
    boolean wasSet = false;
    deadline = aDeadline;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetImmutable */
  public boolean setTotalCost(int aTotalCost)
  {
    boolean wasSet = false;
    if (!canSetTotalCost) { return false; }
    canSetTotalCost = false;
    totalCost = aTotalCost;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetImmutable */
  public boolean setPricePaid(int aPricePaid)
  {
    boolean wasSet = false;
    if (!canSetPricePaid) { return false; }
    canSetPricePaid = false;
    pricePaid = aPricePaid;
    wasSet = true;
    return wasSet;
  }

  public Date getDatePlaced()
  {
    return datePlaced;
  }

  public DeliveryDeadline getDeadline()
  {
    return deadline;
  }

  /**
   * Total cost of the order, without considering points.
   */
  public int getTotalCost()
  {
    return totalCost;
  }

  /**
   * Amount that the customer actually had to pay for the order.
   * This depends on both the total cost and whether or not the customer decided to use their points.
   */
  public int getPricePaid()
  {
    return pricePaid;
  }

  public int getOrderNumber()
  {
    return orderNumber;
  }

  public String getStateOrderFullName()
  {
    String answer = stateOrder.toString();
    return answer;
  }

  public StateOrder getStateOrder()
  {
    return stateOrder;
  }

  public boolean checkout()
  {
    boolean wasEventProcessed = false;
    
    StateOrder aStateOrder = stateOrder;
    switch (aStateOrder)
    {
      case UnderConstruction:
        if (hasAtLeastOneItem())
        {
        // line 7 "../../../../../../resources/GroceryState.ump"
          displayCost();
          setStateOrder(StateOrder.Pending);
          wasEventProcessed = true;
          break;
        }
        break;
      case Pending:
        if (hasAtLeastOneItem())
        {
        // line 25 "../../../../../../resources/GroceryState.ump"
          displayCost();
          setStateOrder(StateOrder.Pending);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancel()
  {
    boolean wasEventProcessed = false;
    
    StateOrder aStateOrder = stateOrder;
    switch (aStateOrder)
    {
      case UnderConstruction:
        // line 10 "../../../../../../resources/GroceryState.ump"
        cancelOrder();
        setStateOrder(StateOrder.Cancelled);
        wasEventProcessed = true;
        break;
      case Pending:
        // line 29 "../../../../../../resources/GroceryState.ump"
        cancelOrder();
        setStateOrder(StateOrder.Cancelled);
        wasEventProcessed = true;
        break;
      case Placed:
        // line 39 "../../../../../../resources/GroceryState.ump"
        returnItems(); cancelOrder();
        setStateOrder(StateOrder.Cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pay()
  {
    boolean wasEventProcessed = false;
    
    StateOrder aStateOrder = stateOrder;
    switch (aStateOrder)
    {
      case Pending:
        if (orderBeenPaid())
        {
        // line 21 "../../../../../../resources/GroceryState.ump"
          updatePoints(); setDatePlaced(); takeItems();
          setStateOrder(StateOrder.Placed);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean assignEmployee(Employee e)
  {
    boolean wasEventProcessed = false;
    
    StateOrder aStateOrder = stateOrder;
    switch (aStateOrder)
    {
      case Placed:
        // line 35 "../../../../../../resources/GroceryState.ump"
        setOrderAssignee(e); updateInventory();
        setStateOrder(StateOrder.InPreparation);
        wasEventProcessed = true;
        break;
      case InPreparation:
        // line 48 "../../../../../../resources/GroceryState.ump"
        setOrderAssignee(e); updateInventory();
        setStateOrder(StateOrder.InPreparation);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finishAssembly()
  {
    boolean wasEventProcessed = false;
    
    StateOrder aStateOrder = stateOrder;
    switch (aStateOrder)
    {
      case InPreparation:
        if (canFinishAssembly())
        {
          setStateOrder(StateOrder.ReadyForDelivery);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean deliver()
  {
    boolean wasEventProcessed = false;
    
    StateOrder aStateOrder = stateOrder;
    switch (aStateOrder)
    {
      case ReadyForDelivery:
        if (pastDeadline())
        {
          setStateOrder(StateOrder.Delivered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStateOrder(StateOrder aStateOrder)
  {
    stateOrder = aStateOrder;
  }
  /* Code from template association_GetOne */
  public GroceryManagementSystem getGroceryManagementSystem()
  {
    return groceryManagementSystem;
  }
  /* Code from template association_GetMany */
  public OrderItem getOrderItem(int index)
  {
    OrderItem aOrderItem = orderItems.get(index);
    return aOrderItem;
  }

  public List<OrderItem> getOrderItems()
  {
    List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
    return newOrderItems;
  }

  public int numberOfOrderItems()
  {
    int number = orderItems.size();
    return number;
  }

  public boolean hasOrderItems()
  {
    boolean has = orderItems.size() > 0;
    return has;
  }

  public int indexOfOrderItem(OrderItem aOrderItem)
  {
    int index = orderItems.indexOf(aOrderItem);
    return index;
  }
  /* Code from template association_GetOne */
  public Customer getOrderPlacer()
  {
    return orderPlacer;
  }
  /* Code from template association_GetOne */
  public Employee getOrderAssignee()
  {
    return orderAssignee;
  }

  public boolean hasOrderAssignee()
  {
    boolean has = orderAssignee != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGroceryManagementSystem(GroceryManagementSystem aGroceryManagementSystem)
  {
    boolean wasSet = false;
    if (aGroceryManagementSystem == null)
    {
      return wasSet;
    }

    GroceryManagementSystem existingGroceryManagementSystem = groceryManagementSystem;
    groceryManagementSystem = aGroceryManagementSystem;
    if (existingGroceryManagementSystem != null && !existingGroceryManagementSystem.equals(aGroceryManagementSystem))
    {
      existingGroceryManagementSystem.removeOrder(this);
    }
    groceryManagementSystem.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public OrderItem addOrderItem(int aQuantity, GroceryManagementSystem aGroceryManagementSystem, Item aItem)
  {
    return new OrderItem(aQuantity, aGroceryManagementSystem, this, aItem);
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    Order existingOrder = aOrderItem.getOrder();
    boolean isNewOrder = existingOrder != null && !this.equals(existingOrder);
    if (isNewOrder)
    {
      aOrderItem.setOrder(this);
    }
    else
    {
      orderItems.add(aOrderItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderItem(OrderItem aOrderItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderItem, as it must always have a order
    if (!this.equals(aOrderItem.getOrder()))
    {
      orderItems.remove(aOrderItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderItemAt(OrderItem aOrderItem, int index)
  {  
    boolean wasAdded = false;
    if(addOrderItem(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderItemAt(OrderItem aOrderItem, int index)
  {
    boolean wasAdded = false;
    if(orderItems.contains(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderItemAt(aOrderItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOrderPlacer(Customer aOrderPlacer)
  {
    boolean wasSet = false;
    if (aOrderPlacer == null)
    {
      return wasSet;
    }

    Customer existingOrderPlacer = orderPlacer;
    orderPlacer = aOrderPlacer;
    if (existingOrderPlacer != null && !existingOrderPlacer.equals(aOrderPlacer))
    {
      existingOrderPlacer.removeOrdersPlaced(this);
    }
    orderPlacer.addOrdersPlaced(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrderAssignee(Employee aOrderAssignee)
  {
    boolean wasSet = false;
    Employee existingOrderAssignee = orderAssignee;
    orderAssignee = aOrderAssignee;
    if (existingOrderAssignee != null && !existingOrderAssignee.equals(aOrderAssignee))
    {
      existingOrderAssignee.removeOrdersAssigned(this);
    }
    if (aOrderAssignee != null)
    {
      aOrderAssignee.addOrdersAssigned(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    GroceryManagementSystem placeholderGroceryManagementSystem = groceryManagementSystem;
    this.groceryManagementSystem = null;
    if(placeholderGroceryManagementSystem != null)
    {
      placeholderGroceryManagementSystem.removeOrder(this);
    }
    for(int i=orderItems.size(); i > 0; i--)
    {
      OrderItem aOrderItem = orderItems.get(i - 1);
      aOrderItem.delete();
    }
    Customer placeholderOrderPlacer = orderPlacer;
    this.orderPlacer = null;
    if(placeholderOrderPlacer != null)
    {
      placeholderOrderPlacer.removeOrdersPlaced(this);
    }
    if (orderAssignee != null)
    {
      Employee placeholderOrderAssignee = orderAssignee;
      this.orderAssignee = null;
      placeholderOrderAssignee.removeOrdersAssigned(this);
    }
  }


  /**
   * === GUARDS ===
   */
  // line 65 "../../../../../../resources/GroceryState.ump"
   public boolean hasAtLeastOneItem(){
    return orderItems.size() > 0;
  }

  // line 69 "../../../../../../resources/GroceryState.ump"
   public boolean isFullCardPayment(double amount){
    return Math.round(amount * 100) == totalCost;
  }

  // line 73 "../../../../../../resources/GroceryState.ump"
   public boolean hasEnoughPoints(int pts){
    return orderPlacer != null && orderPlacer.getNumberOfPoints() >= pts;
  }

  // line 77 "../../../../../../resources/GroceryState.ump"
   public boolean pastDeadline(){
    long daysUntilDelivery = deadline.ordinal();  // now explicit
    long millisInADay = 24L * 60 * 60 * 1000;
    java.sql.Date expectedDeliveryDate = new java.sql.Date(datePlaced.getTime() + daysUntilDelivery * millisInADay);
    java.sql.Date today = java.sql.Date.valueOf(java.time.LocalDate.now());

    return !expectedDeliveryDate.toLocalDate().isAfter(today.toLocalDate());
  }

  // line 87 "../../../../../../resources/GroceryState.ump"
   public boolean canFinishAssembly(){
    return !containsPerishables() || pastDeadline();
  }

  // line 91 "../../../../../../resources/GroceryState.ump"
   public boolean containsPerishables(){
    for (OrderItem oi : orderItems) {
      if (oi.getItem().getIsPerishable()) {
        return true;
      }
    }
    return false;
  }


  /**
   * === ACTIONS ===
   */
  // line 101 "../../../../../../resources/GroceryState.ump"
   private void displayCost(){
    int cost = 0;
    for (OrderItem oi : orderItems) {
      int qty = oi.getQuantity();
      double discount = 1 - Math.min((qty - 1) * 0.05, 0.45);
      double itemPrice = oi.getItem().getPrice() * discount;
      cost += Math.round(itemPrice * qty);
    }
    if (deadline == DeliveryDeadline.SameDay) {
      cost += 500;
    }
    setTotalCost(cost);
  }

  // line 115 "../../../../../../resources/GroceryState.ump"
   public void setStatusOnlyForTesting(StateOrder orderStatus){
    this.stateOrder = orderStatus;
  }

  // line 119 "../../../../../../resources/GroceryState.ump"
   public void payWithPoints(Customer customer){
    int customerPoints = customer.getNumberOfPoints();
  int pointsUsed = Math.min(customerPoints, totalCost);
  customer.setNumberOfPoints(customerPoints - pointsUsed);
  this.totalCost -= pointsUsed;
  }

  // line 127 "../../../../../../resources/GroceryState.ump"
   public void updatePoints(){
    int earned = 0;
    for (OrderItem item: orderItems){
      Item i = item.getItem();
      earned += i.getNumberOfPoints() * item.getQuantity();
    }
    orderPlacer.setNumberOfPoints(orderPlacer.getNumberOfPoints() + earned);
  }

  // line 136 "../../../../../../resources/GroceryState.ump"
   public void cancelOrder(){
    for (OrderItem oi : new ArrayList<>(orderItems)) {
      oi.delete();
    }
  }

  // line 142 "../../../../../../resources/GroceryState.ump"
   public void setDatePlaced(){
    this.datePlaced = new java.sql.Date(System.currentTimeMillis());
  }

  // line 146 "../../../../../../resources/GroceryState.ump"
   public void assignEmployeeInternally(Employee e){
    setOrderAssignee(e);
  }

  // line 150 "../../../../../../resources/GroceryState.ump"
   public void updateInventory(){
    for (OrderItem oi : orderItems) {
      Item item = oi.getItem();
      item.setQuantityInInventory(item.getQuantityInInventory() - oi.getQuantity());
    }
  }

  // line 157 "../../../../../../resources/GroceryState.ump"
   public boolean orderBeenPaid(){
    return pricePaid == totalCost;
  }

  // line 161 "../../../../../../resources/GroceryState.ump"
   public boolean hasEmployee(){
    return getOrderAssignee() != null;
  }

  // line 165 "../../../../../../resources/GroceryState.ump"
   public void returnItems(){
    for (OrderItem orderItem : this.orderItems){
      int quantity = orderItem.getQuantity();
      Item item = orderItem.getItem();
      item.setQuantityInInventory(item.getQuantityInInventory()+quantity);
    }
  }

  // line 172 "../../../../../../resources/GroceryState.ump"
   public void takeItems(){
    for (OrderItem orderItem : this.orderItems){
      int quantity = orderItem.getQuantity();
      Item item = orderItem.getItem();
      int newQuantity = item.getQuantityInInventory()-quantity;
      item.setQuantityInInventory(newQuantity);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "orderNumber" + ":" + getOrderNumber()+ "," +
            "totalCost" + ":" + getTotalCost()+ "," +
            "pricePaid" + ":" + getPricePaid()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "datePlaced" + "=" + (getDatePlaced() != null ? !getDatePlaced().equals(this)  ? getDatePlaced().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deadline" + "=" + (getDeadline() != null ? !getDeadline().equals(this)  ? getDeadline().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "groceryManagementSystem = "+(getGroceryManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getGroceryManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderPlacer = "+(getOrderPlacer()!=null?Integer.toHexString(System.identityHashCode(getOrderPlacer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderAssignee = "+(getOrderAssignee()!=null?Integer.toHexString(System.identityHashCode(getOrderAssignee())):"null");
  }
}