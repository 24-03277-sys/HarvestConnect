import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

// =============================================================
// MAIN APPLICATION (MOVED TO TOP)
// =============================================================
public class HarvestConnect {

    // COLORS FOR CLEANER UI
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    static ArrayList<FoodItem> inventory = new ArrayList<>();
    static int totalDistributed = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            printMenu();
            System.out.print(YELLOW + "Enter choice (1-5): " + RESET);

            while (!sc.hasNextInt()) {
                System.out.print(RED + "Invalid input. Enter a number (1-5): " + RESET);
                sc.next();
            }

            choice = sc.nextInt();
            System.out.println();

            switch (choice) {
                case 1 -> addFoodDonation(sc);
                case 2 -> viewInventory();
                case 3 -> matchDonation(sc);
                case 4 -> generateReport();
                case 5 -> System.out.println(GREEN + "👋 Thank you for supporting Zero Hunger!" + RESET);
                default -> System.out.println(RED + "❌ Invalid choice.\n" + RESET);
            }

        } while (choice != 5);

        sc.close();
    }

    // ----------------------------------------------------------
    private static void printMenu() {
        System.out.println(CYAN +
                "\n══════════════════════════════════════════════");
        System.out.println(" 🍎 HARVESTCONNECT SYSTEM ");
        System.out.println("══════════════════════════════════════════════" + RESET);

        System.out.println(BLUE + " 1 ➤ Add Food Donation");
        System.out.println(" 2 ➤ View Inventory");
        System.out.println(" 3 ➤ Match Donation to Recipient");
        System.out.println(" 4 ➤ Generate Report");
        System.out.println(" 5 ➤ Exit Program" + RESET);

        System.out.println(CYAN + "══════════════════════════════════════════════" + RESET);
    }

    // ----------------------------------------------------------
    private static void addFoodDonation(Scanner sc) {
        try {
            System.out.println(CYAN + "\nSelect Food Type:" + RESET);
            System.out.println(" 1. Perishable");
            System.out.println(" 2. Baked Good");
            System.out.println(" 3. Pantry Item");
            System.out.print("Choice: ");

            int type = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();

            if (quantity < 0)
                throw new InvalidQuantityException("Quantity cannot be negative!");

            System.out.print("Enter expiration date (YYYY-MM-DD): ");
            LocalDate expDate = LocalDate.parse(sc.next());

            FoodItem item = switch (type) {
                case 1 -> new PerishableItem(name, quantity, expDate);
                case 2 -> new BakedGoodItem(name, quantity, expDate);
                case 3 -> new PantryItem(name, quantity, expDate);
                default -> null;
            };

            if (item != null) {
                item.logInbound();
                inventory.add(item);
                System.out.println(GREEN + "✔ Donation added!" + RESET);
            } else {
                System.out.println(RED + "Invalid type selected!" + RESET);
            }

        } catch (InvalidQuantityException e) {
            System.out.println(RED + "Error: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.out.println(RED + "Invalid input. Please try again." + RESET);
        }
    }

    // ----------------------------------------------------------
    private static void viewInventory() {
        System.out.println(CYAN + "\n--- Current Inventory ---" + RESET);

        if (inventory.isEmpty()) {
            System.out.println(YELLOW + "No donations available." + RESET);
            return;
        }

        System.out.println(BLUE + "Type | Name | Qty | Expiration | Days Left" + RESET);
        System.out.println("------------------------------------------------------------------");

        for (FoodItem item : inventory) {
            System.out.printf("%-14s | %-15s | %-3d | %-12s | %d\n",
                    item.getType(),
                    item.getName(),
                    item.getQuantity(),
                    item.getExpirationDate(),
                    item.calculateDaysUntilExpiration());
        }
    }

    // ----------------------------------------------------------
    private static void matchDonation(Scanner sc) {
        if (inventory.isEmpty()) {
            System.out.println(YELLOW + "No donations to match." + RESET);
            return;
        }

        sc.nextLine();
        System.out.print("Recipient name: ");
        String recipient = sc.nextLine();

        System.out.print("Item needed: ");
        String needed = sc.nextLine();

        System.out.print("Quantity needed: ");
        int qty = sc.nextInt();

        for (FoodItem item : inventory) {
            if (item.getName().equalsIgnoreCase(needed) && item.getQuantity() >= qty) {
                item.reduceQuantity(qty);
                totalDistributed += qty;

                item.logOutbound();
                System.out.println(GREEN + "✔ Distributed " + qty + " units of " +
                        item.getName() + " to " + recipient + "!" + RESET);
                return;
            }
        }

        System.out.println(RED + "❌ No match found or insufficient quantity." + RESET);
    }

    // ----------------------------------------------------------
    private static void generateReport() {
        System.out.println(CYAN + "\n=== 📊 Distribution Report ===" + RESET);
        System.out.println("Total distributed: " + GREEN + totalDistributed + RESET);
        System.out.println("Remaining items in inventory: " + YELLOW + inventory.size() + RESET);
    }
}

// =============================================================
// INTERFACE
// =============================================================
interface ITrackable {
    void logInbound();
    void logOutbound();
}

// =============================================================
// ABSTRACT CLASS
// =============================================================
abstract class FoodItem implements ITrackable {
    protected String name;
    protected int quantity;
    protected LocalDate expirationDate;

    public FoodItem(String name, int quantity, LocalDate expirationDate) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpirationDate() { return expirationDate; }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    public long calculateDaysUntilExpiration() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    @Override
    public void logInbound() {
        System.out.println("Inbound: " + quantity + " units of " + name);
    }

    @Override
    public void logOutbound() {
        System.out.println("Outbound: " + name);
    }

    public abstract String getType();
}

// =============================================================
// SUBCLASSES
// =============================================================
class PerishableItem extends FoodItem {
    public PerishableItem(String name, int quantity, LocalDate expirationDate) {
        super(name, quantity, expirationDate);
    }

    @Override
    public String getType() { return "Perishable"; }
}

class BakedGoodItem extends FoodItem {
    public BakedGoodItem(String name, int quantity, LocalDate expirationDate) {
        super(name, quantity, expirationDate);
    }

    @Override
    public String getType() { return "Baked Good"; }
}

class PantryItem extends FoodItem {
    public PantryItem(String name, int quantity, LocalDate expirationDate) {
        super(name, quantity, expirationDate);
    }

    @Override
    public long calculateDaysUntilExpiration() {
        return 999;
    }

    @Override
    public String getType() { return "Pantry Item"; }
}

// =============================================================
// CUSTOM EXCEPTION
// =============================================================
class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
