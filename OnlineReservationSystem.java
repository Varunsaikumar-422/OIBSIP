import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class User {
    String username;
    String password;
    String name;
    String mobileNumber;

    User(String username, String password, String name, String mobileNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }
}

class Reservation {
    static int counter = 1000;
    int pnr;
    String name;
    String trainNumber;
    String trainName;
    String classType;
    String from;
    String to;
    String dateOfJourney;
    double price;
    boolean cancelled = false;

    Reservation(String name, String trainNumber, String trainName, String classType, String from, String to, String dateOfJourney, double price) {
        this.pnr = counter++;
        this.name = name;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.from = from;
        this.to = to;
        this.dateOfJourney = dateOfJourney;
        this.price = price;
    }

    void display() {
        if (!cancelled) {
            JOptionPane.showMessageDialog(null,
                    "PNR: " + pnr + "\n" +
                            "Name: " + name + "\n" +
                            "Train Number: " + trainNumber + "\n" +
                            "Train Name: " + trainName + "\n" +
                            "Class Type: " + classType + "\n" +
                            "From: " + from + "\n" +
                            "To: " + to + "\n" +
                            "Date of Journey: " + dateOfJourney + "\n" +
                            "Price: ₹" + price);
        } else {
            JOptionPane.showMessageDialog(null, "This reservation has been cancelled.");
        }
    }

    void cancel() {
        cancelled = true;
        JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");
    }
}

public class OnlineReservationSystem {
    static JFrame frame;
    static JPanel panel;
    static JTextField usernameField, nameField, trainNumberField, classTypeField, fromField, toField, dateField, pnrField, mobileField, discountCodeField;
    static JPasswordField passwordField;
    static JButton loginButton, reserveButton, cancelButton, cancelReservationButton, goBackButton, registerButton, searchButton, viewHistoryButton, applyDiscountButton, feedbackButton;
    static Map<String, User> users = new HashMap<>();
    static Map<Integer, Reservation> reservations = new HashMap<>();
    static java.util.List<Reservation> userReservations = new ArrayList<>();
    static boolean isLoggedIn = false;
    static User loggedInUser = null;

    public static void main(String[] args) {
        users.put("admin", new User("admin", "admin123", "Admin", "9999999999"));

        frame = new JFrame("Online Reservation System");
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 255)); 

        showLoginForm();
        frame.add(panel);
        frame.setVisible(true);
    }

    static void showLoginForm() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBackground(new Color(245, 245, 245)); 

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(50, 50, 50));

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(220, 220, 220));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(50, 50, 50));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(220, 220, 220));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        loginButton = new JButton("Login");
        styleButton(loginButton);

        registerButton = new JButton("Register");
        styleButton(registerButton);

        // Handling registration
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegistrationForm();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (users.containsKey(username) && users.get(username).password.equals(password)) {
                    loggedInUser = users.get(username);
                    isLoggedIn = true;
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);
    }

    static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(50, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 170, 255)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 255)); // 
            }
        });
    }

    static void showRegistrationForm() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBackground(new Color(245, 245, 245)); 
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        JLabel mobileLabel = new JLabel("Mobile Number:");
        mobileField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String mobile = mobileField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!name.isEmpty() && !mobile.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    users.put(username, new User(username, password, name, mobile));
                    JOptionPane.showMessageDialog(null, "Registration Successful!");
                    showLoginForm(); // Go back to login form
                } else {
                    JOptionPane.showMessageDialog(null, "All fields are required.");
                }
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(mobileLabel);
        panel.add(mobileField);
        panel.add(submitButton);
    }

    static void showMainMenu() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBackground(new Color(245, 245, 245));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.name);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        viewHistoryButton = new JButton("View Reservations");
        styleButton(viewHistoryButton);

        reserveButton = new JButton("Make Reservation");
        styleButton(reserveButton);

        cancelReservationButton = new JButton("Cancel Reservation");
        styleButton(cancelReservationButton);

        goBackButton = new JButton("Log Out");
        styleButton(goBackButton);
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showReservationForm();
            }
        });

        viewHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showReservationHistory();
            }
        });

        cancelReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCancelReservationForm();
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isLoggedIn = false;
                loggedInUser = null;
                showLoginForm();
            }
        });

        panel.add(welcomeLabel);
        panel.add(reserveButton);
        panel.add(viewHistoryButton);
        panel.add(cancelReservationButton);
        panel.add(goBackButton);
    }

    static void showReservationForm() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBackground(new Color(245, 245, 245)); 

        JLabel trainNumberLabel = new JLabel("Train Number:");
        trainNumberField = new JTextField(20);
        JLabel classTypeLabel = new JLabel("Class Type (AC, Sleeper, etc.):");
        classTypeField = new JTextField(20);
        JLabel fromLabel = new JLabel("From:");
        fromField = new JTextField(20);
        JLabel toLabel = new JLabel("To:");
        toField = new JTextField(20);
        JLabel dateLabel = new JLabel("Date of Journey (YYYY-MM-DD):");
        dateField = new JTextField(20);

        JButton submitButton = new JButton("Submit Reservation");
        styleButton(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String trainNumber = trainNumberField.getText();
                String classType = classTypeField.getText();
                String from = fromField.getText();
                String to = toField.getText();
                String dateOfJourney = dateField.getText();
                double price = calculatePrice(from, to, classType);
                String trainName = getTrainName(trainNumber);

                Reservation newReservation = new Reservation(loggedInUser.name, trainNumber, trainName, classType, from, to, dateOfJourney, price);
                reservations.put(newReservation.pnr, newReservation);
                userReservations.add(newReservation);
                JOptionPane.showMessageDialog(null, "Reservation Successful! PNR: " + newReservation.pnr);
                showMainMenu(); 
            }
        });

        panel.add(trainNumberLabel);
        panel.add(trainNumberField);
        panel.add(classTypeLabel);
        panel.add(classTypeField);
        panel.add(fromLabel);
        panel.add(fromField);
        panel.add(toLabel);
        panel.add(toField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(submitButton);
    }

    static void showReservationHistory() {
        StringBuilder history = new StringBuilder("Your Reservations:\n");
        for (Reservation res : userReservations) {
            history.append("PNR: ").append(res.pnr).append("\n");
            history.append("Train: ").append(res.trainName).append("\n");
            history.append("From: ").append(res.from).append(" To: ").append(res.to).append("\n");
            history.append("Date: ").append(res.dateOfJourney).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, history.toString());
    }

    static void showCancelReservationForm() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.setBackground(new Color(245, 245, 245));
    
        JLabel pnrLabel = new JLabel("Enter PNR to Cancel:");
        pnrField = new JTextField(20);
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton backButton = new JButton("Back");

        styleButton(cancelButton);
        styleButton(backButton);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int pnr = Integer.parseInt(pnrField.getText());
                    if (reservations.containsKey(pnr)) {
                        reservations.get(pnr).cancel();
                        JOptionPane.showMessageDialog(null, "Reservation cancelled successfully!");
                        showMainMenu();  
                    } else {
                        JOptionPane.showMessageDialog(null, "Reservation not found!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid PNR format!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();  
            }
        });

        panel.add(pnrLabel);
        panel.add(pnrField);
        panel.add(cancelButton);
        panel.add(backButton);  
    }

    static double calculatePrice(String from, String to, String classType) {
        double basePrice = 500; // Default price
        if ("AC".equalsIgnoreCase(classType)) {
            basePrice += 200;
        } else if ("Sleeper".equalsIgnoreCase(classType)) {
            basePrice -= 100;
        }
        return basePrice;
    }

    static String getTrainName(String trainNumber) {
        switch (trainNumber) {
            case "101": return "Shatabdi Express";
            case "102": return "Rajdhani Express";
            case "103": return "Duronto Express";
            default: return "Unknown Train";
        }
    }
}
