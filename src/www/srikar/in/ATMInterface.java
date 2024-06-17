package www.srikar.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class User {
    private String userId;
    private String pin;
    private Account account;

    public User(String userId, String pin, Account account) {
        this.userId = userId;
        this.pin = pin;
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public Account getAccount() {
        return account;
    }
}

class Account {
    private double balance;
    private ArrayList<Transaction> transactionHistory;

    public Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdraw", amount));
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public void transfer(Account toAccount, double amount) {
        if (amount <= balance) {
            balance -= amount;
            toAccount.deposit(amount);
            transactionHistory.add(new Transaction("Transfer to " + toAccount, amount));
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

class Transaction {
    private String type;
    private double amount;
    private java.time.LocalDateTime date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = java.time.LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public java.time.LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + " - " + type + ": $" + amount;
    }
}

class ATM {
    private HashMap<String, User> users;
    private User currentUser;

    public ATM() {
        users = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public boolean authenticateUser(String userId, String pin) {
        User user = users.get(userId);
        if (user != null && user.getPin().equals(pin)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void showTransactionHistory() {
        if (currentUser != null) {
            for (Transaction t : currentUser.getAccount().getTransactionHistory()) {
                System.out.println(t);
            }
        }
    }

    public void deposit(double amount) {
        if (currentUser != null) {
            currentUser.getAccount().deposit(amount);
        }
    }

    public void withdraw(double amount) {
        if (currentUser != null) {
            currentUser.getAccount().withdraw(amount);
        }
    }

    public void transfer(String toUserId, double amount) {
        if (currentUser != null) {
            User toUser = users.get(toUserId);
            if (toUser != null) {
                currentUser.getAccount().transfer(toUser.getAccount(), amount);
            } else {
                System.out.println("Recipient user not found.");
            }
        }
    }

    public void quit() {
        currentUser = null;
    }
}

public class ATMInterface {
    private static Scanner scanner = new Scanner(System.in);
    private static ATM atm = new ATM();

    public static void main(String[] args) {
        setupDummyData();
        while (true) {
            if (authenticate()) {
                showMenu();
            }
        }
    }

    private static void setupDummyData() {
        Account account1 = new Account(1000);
        User user1 = new User("user1", "1234", account1);
        atm.addUser(user1);

        Account account2 = new Account(2000);
        User user2 = new User("user2", "5678", account2);
        atm.addUser(user2);
    }

    private static boolean authenticate() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        if (atm.authenticateUser(userId, pin)) {
            return true;
        } else {
            System.out.println("Invalid User ID or PIN.");
            return false;
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    atm.showTransactionHistory();
                    break;
                case 2:
                    handleWithdraw();
                    break;
                case 3:
                    handleDeposit();
                    break;
                case 4:
                    handleTransfer();
                    break;
                case 5:
                    atm.quit();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleWithdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        atm.withdraw(amount);
    }

    private static void handleDeposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        atm.deposit(amount);
    }

    private static void handleTransfer() {
        System.out.print("Enter recipient User ID: ");
        String toUserId = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        atm.transfer(toUserId, amount);
    }
}

