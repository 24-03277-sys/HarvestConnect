SIERRA MADRE

Project Title:

Harvest connect

Description / Overview:
HarvestConnect is a simple food donation management system that helps organizations track and distribute donated food. The system allows users to add different types of food items, store them in an inventory, match donations to recipients, and generate basic reports. It also checks expiration dates so that perishable food can be prioritized. Overall, it helps reduce food waste and makes food distribution easier and more organized.

OOP Concepts Applied:

Encapsulation - The program hides internal data inside classes and exposes only necessary methods.
Attributes such as name, quantity, and expirationDate inside FoodItem are protected, restricting direct modification.
Methods like reduceQuantity(), getName(), and calculateDaysUntilExpiration() safely access and modify these values.
Inheritance - The project uses inheritance to avoid repetitive code:
FoodItem is an abstract superclass containing shared fields and methods.
Subclasses PerishableItem, BakedGoodItem, and PantryItem extend FoodItem, inheriting its behavior while adding specialization.
Polymorphism - The program uses polymorphism to treat different item types as FoodItem objects:
ArrayList<FoodItem> can store any subclass instance.
The getType() method is overridden differently in each subclass.
calculateDaysUntilExpiration() is customized in PantryItem to return 999 days.
Abstraction - The abstract class FoodItem provides a blueprint with abstract method getType() and shared logic such as expiration calculation.
The ITrackable interface abstracts logging functionality, ensuring all food item types implement logInbound() and logOutbound().
Exception Handling (Custom Exception) - A custom exception, InvalidQuantityException, ensures input validation when adding donations.
This improves reliability and prevents invalid states (e.g., negative quantity).
Program Structure - Below is an explanation of the major classes and their roles.

How to Run the Program
Below are the step-by-step instructions.
1st: Save the Source Code
Open a plain text editor (like Notepad, VS Code, Sublime Text, etc.).
Copy and paste the entire code you provided into the editor.
Save the file as HarvestConnect.java.
2nd: Compile the Program
Open your Command Prompt (Windows) or Terminal (macOS/Linux).
Navigate to the directory where you saved HarvestConnect.java using the cd (change directory) command.
Example: cd C:\Users\YourName\Documents\JavaProjects
Execute the following command to compile the source code:
javac HarvestConnect.java
If the compilation is successful a file named HarvestConnect.class will be created in the same directory. This is the bytecode that the Java Virtual Machine (JVM) executes.
If there are errors javac will display them. You'll need to fix the errors in your .java file and re-compile.
3rd: Run the Program
Once compiled you will use the Java runtime to execute the main class.
While still in the same directory in your Command Prompt/Terminal, execute the following command:
Bash
java HarvestConnect
The program will start, and you should see the "HARVESTCONNECT SYSTEM" menu in your terminal.
You can now interact with the program by entering choices (1-5) as prompted.

Sample Output: 
The program output starts: 
══════════════════════════════════════════════ 🍎 HARVESTCONNECT SYSTEM ══════════════════════════════════════════════
1 ➤ Add Food Donation 
2 ➤ View Inventory
3 ➤ Match Donation to Recipient 
4 ➤ Generate Report 
5 ➤ Exit Program
 ══════════════════════════════════════════════ 
Enter choice (1-5): 1 

Select Food Type: 
1. Perishable 
2. Baked Good 
3. Pantry Item 
Choice: 3 
Enter name: Canned Beans 
Enter quantity: 50 
Enter expiration date (YYYY-MM-DD): 2026-10-01 
Inbound: 50 units of Canned Beans 
✔ Donation added!

📌 Main Class: HarvestConnect
Role:
Serves as the central controller of the entire program.
Manages UI menus, user input, inventory list, and system flow.
Key Responsibilities:
Display menu
Add donations
View inventory
Match donations to recipients
Generate reports
Interface: ITrackable
Role:
Ensures all items in the system can log inbound (donated) or outbound (distributed) movements.
Methods:
logInbound()
logOutbound()
Abstract Class: FoodItem
Role:
Parent class for all food types.
Contains:
Shared attributes (name, quantity, expirationDate)
Methods for expiration calculation
Logging method implementations
Abstract method getType() (implemented by subclasses)
Subclasses:
1. PerishableItem
Represents food that spoils quickly.
Inherits expiration calculation from FoodItem.
2. BakedGoodItem
Represents bakery items like bread, cakes, etc.
3. PantryItem
Shelf-stable goods.
Overrides expiration calculation to return a fixed long shelf life (999 days).

Text-Based Class Diagram
               +----------------------+
                |     ITrackable       |
                +----------------------+
                | + logInbound()       |
                | + logOutbound()      |
                +----------+-----------+
                           |
                           |
                +----------v-----------+
                |      FoodItem        |  (abstract)
                +----------------------+
                | - name               |
                | - quantity           |
                | - expirationDate     |
                +----------------------+
                | + getName()          |
                | + getQuantity()       |
                | + reduceQuantity()    |
                | + calculateDaysUntilExpiration() |
                | + getType() (abstract)          |
                +-----------+-----------+
                            |
          -------------------------------------------------
          |                      |                       |
+----------------+   +---------------------+   +--------------------+
| PerishableItem |   |   BakedGoodItem     |   |    PantryItem      |
+----------------+   +---------------------+   +--------------------+
| Overrides getType| | Overrides getType    | | Overrides getType    |
|                  | |                      | | Overrides expiration  |
+------------------+ +----------------------+ +-----------------------+
