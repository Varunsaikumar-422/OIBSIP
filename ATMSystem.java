import java.util.*;

class Transaction {
    String type;
    double amount;
    Date date;
    String note;

    Transaction(String type, double amount, String note) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.date = new Date();
    }

    public String toString() {
        return date + " - " + type + ": $" + amount + " [" + note + "]";
    }
}

class Account {
    private double balance;
    private ArrayList<Transaction> transactions;
    private double dailyWithdrawLimit = 1000;
    private double withdrawnToday = 0;

    Account() {
        balance = 0;
        transactions = new ArrayList<>();
    }

    void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction("Deposit", amount, "Deposited successfully"));
            System.out.println("✅ Deposit successful.");
        } else {
            System.out.println("⚠️ Invalid deposit amount.");
        }
    }

    void withdraw(double amount) {
        if (amount > 0 && amount <= balance && (withdrawnToday + amount) <= dailyWithdrawLimit) {
            balance -= amount;
            withdrawnToday += amount;
            transactions.add(new Transaction("Withdraw", amount, "Withdrawn successfully"));
            System.out.println("✅ Withdrawal successful.");
        } else if (withdrawnToday + amount > dailyWithdrawLimit) {
            System.out.println("⚠️ Exceeded daily withdraw limit of $" + dailyWithdrawLimit);
        } else {
            System.out.println("⚠️ Invalid or insufficient balance.");
        }
    }

    void transfer(Account target, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            target.balance += amount;
            transactions.add(new Transaction("Transfer to " + target.hashCode(), amount, "Transfer made"));
            target.transactions.add(new Transaction("Transfer from " + this.hashCode(), amount, "Transfer received"));
            System.out.println("✅ Transfer successful.");
        } else {
            System.out.println("⚠️ Invalid or insufficient balance.");
        }
    }

    void showTransactionHistory() {
        if (transactions.isEmpty()) {
            System.out.println("⚠️ No transactions yet.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }

    double getBalance() {
        return balance;
    }

    double getWithdrawnToday() {
        return withdrawnToday;
    }

    void resetDailyLimit() {
        withdrawnToday = 0;
    }
}

class User {
    String userId;
    private String pin;
    private int loginAttempts;
    private boolean isLocked;
    Account account;
    String phone;
    String email;

    User(String userId, String pin, String phone, String email) {
        this.userId = userId;
        this.pin = pin;
        this.account = new Account();
        this.loginAttempts = 0;
        this.isLocked = false;
        this.phone = phone;
        this.email = email;
    }

    boolean authenticate(String userId, String pin) {
        if (isLocked) {
            System.out.println("🔒 Account is locked. Contact customer service.");
            return false;
        }
        if (this.userId.equals(userId) && this.pin.equals(pin)) {
            loginAttempts = 0;
            return true;
        } else {
            loginAttempts++;
            if (loginAttempts >= 3) {
                isLocked = true;
                System.out.println("🔒 Account locked after 3 failed attempts.");
            } else {
                System.out.println("⚠️ Incorrect credentials. Attempts left: " + (3 - loginAttempts));
            }
            return false;
        }
    }

    void changePin(String currentPin, String newPin) {
        if (this.pin.equals(currentPin)) {
            this.pin = newPin;
            System.out.println("✅ PIN changed successfully.");
        } else {
            System.out.println("⚠️ Incorrect current PIN.");
        }
    }

    boolean isLocked() {
        return isLocked;
    }

    void unlockAccount() {
        isLocked = false;
        loginAttempts = 0;
    }

    void updateContactInfo(String newPhone, String newEmail) {
        this.phone = newPhone;
        this.email = newEmail;
        System.out.println("✅ Contact information updated.");
    }

    void displayProfile() {
        System.out.println("User ID: " + userId);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
    }
}

public class ATMSystem {
    private static Scanner sc = new Scanner(System.in);
    private static HashMap<String, User> users = new HashMap<>();
    private static User currentUser = null;

    private static void createSampleUsers() {
        users.put("user1", new User("user1", "1234", "123-456-7890", "user1@email.com"));
        users.put("user2", new User("user2", "2345", "987-654-3210", "user2@email.com"));
    }

    private static void mainMenu(User user) {
        int choice;
        do {
            System.out.println("\n========== ATM Main Menu ==========");
            System.out.println("1. View Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Change PIN");
            System.out.println("7. View Profile");
            System.out.println("8. Update Contact Info");
            System.out.println("9. Set Daily Withdrawal Limit");
            System.out.println("10. Logout");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    user.account.showTransactionHistory();
                    break;
                case 2:
                    withdraw(user);
                    break;
                case 3:
                    deposit(user);
                    break;
                case 4:
                    transfer(user);
                    break;
                case 5:
                    System.out.println("💵 Current Balance: $" + user.account.getBalance());
                    break;
                case 6:
                    changePin(user);
                    break;
                case 7:
                    user.displayProfile();
                    break;
                case 8:
                    updateContactInfo(user);
                    break;
                case 9:
                    setDailyLimit(user);
                    break;
                case 10:
                    System.out.println("🔒 Logged out successfully.");
                    return;
                case 0:
                    System.out.println("👋 Thank you for using ATM System. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        } while (true);
    }

    private static void withdraw(User user) {
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();
        user.account.withdraw(amount);
    }

    private static void deposit(User user) {
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();
        user.account.deposit(amount);
    }

    private static void transfer(User user) {
        System.out.print("Enter recipient User ID: ");
        String recipientId = sc.nextLine();
        User recipient = users.get(recipientId);
        if (recipient != null) {
            System.out.print("Enter amount to transfer: ");
            double amount = sc.nextDouble();
            sc.nextLine();
            user.account.transfer(recipient.account, amount);
        } else {
            System.out.println("⚠️ Recipient not found.");
        }
    }

    private static void changePin(User user) {
        System.out.print("Enter current PIN: ");
        String currentPin = sc.nextLine();
        System.out.print("Enter new PIN: ");
        String newPin = sc.nextLine();
        user.changePin(currentPin, newPin);
    }

    private static void updateContactInfo(User user) {
        System.out.print("Enter new phone number: ");
        String newPhone = sc.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = sc.nextLine();
        user.updateContactInfo(newPhone, newEmail);
    }

    private static void setDailyLimit(User user) {
        System.out.print("Set new daily withdrawal limit (default was 1000): ");
        double limit = sc.nextDouble();
        user.account.resetDailyLimit();
        System.out.println("✅ Daily withdrawal limit updated.");
    }

    private static void login() {
        System.out.println("========== Welcome to the ATM System ==========");
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        User user = users.get(userId);
        if (user != null && user.authenticate(userId, pin)) {
            currentUser = user;
            System.out.println("✅ Login successful.");
            mainMenu(user);
        } else {
            System.out.println("⚠️ Login failed.");
        }
    }

    public static void main(String[] args) {
        createSampleUsers();
        while (true) {
            login();
        }
    }
}
