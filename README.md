# Grocery Management System GUI

A JavaFX-based GUI application for managing a grocery store system. This project is built using Java 21 and Gradle.

## Video Demo

[![Grocery Management System Demo](https://img.youtube.com/vi/t7dUHdZ53YI/0.jpg)](https://youtu.be/t7dUHdZ53YI)

Click the thumbnail above to watch a demonstration of the Grocery Management System in action.

## Prerequisites

- Java Development Kit (JDK) 21 or later
- Gradle 8.12.1 or later (the project includes the Gradle wrapper, so you don't need to install it separately)

## Project Structure

```
.
├── src/
│   ├── main/         # Main application source code
│   └── test/         # Test files
├── build.gradle      # Gradle build configuration
├── settings.gradle   # Gradle settings
└── gradle/          # Gradle wrapper files
```

## Setup Instructions

1. Clone the repository:

   ```bash
   git clone [repository-url]
   cd GroceryApplicationGUI
   ```

2. Build the project:

   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew run
   ```

## Development

- The main application class is located at `ca.mcgill.ecse.grocerymanagementsystem.application.GroceryStoreGUIApplication`
- The project uses JavaFX for the GUI components
- Testing is done using JUnit 5 and Cucumber for BDD testing

## Building and Testing

- To build the project:

  ```bash
  ./gradlew build
  ```

- To run tests:
  ```bash
  ./gradlew test
  ```

## Dependencies

- JavaFX 21.0.6
- JUnit 5.11.4
- Cucumber 7.21.1

## Known Issues and Limitations

### Order Management

- Null pointer exceptions may occur when adding items to an order if no order is selected
- Delivery deadline validation needs improvement for better date handling
- Order status updates may not reflect immediately in the UI

### Shipment Management

- Issues with tracking shipment status updates
- Delivery deadline calculations may not be accurate in some cases
- Shipment assignment to employees needs better validation

### User Management

- User creation may fail with certain special characters in usernames
- Password validation needs improvement
- User role assignment may not persist correctly

### General Issues

- Some UI elements may not refresh properly after data updates
- Error messages could be more descriptive
- Performance may degrade with large datasets

## IDE Setup

### IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Import the project as a Gradle project
3. Let IntelliJ sync the Gradle files
4. Run the main class `GroceryStoreGUIApplication`

### Eclipse

1. Import the project as a Gradle project
2. Run `./gradlew eclipse` to generate Eclipse project files
3. Import the generated project into Eclipse
4. Run the main class `GroceryStoreGUIApplication`
