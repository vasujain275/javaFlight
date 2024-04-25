package flight;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class App {
    private static boolean[] seats = new boolean[100];
    private static boolean isFlightDelayed = false;
    private static int destinationTemperature;
    private static String selectedDestination;
    private static JSONObject seatReservationData = new JSONObject();

    // Adding user details
    private static final String[] USERS = {"Srivatsan", "Tanish", "Shaifali"};
    private static final String[] PASSWORDS = {"Srivatsan", "Tanish", "Shaifali"};

    public static void main(String[] args) {
        loadSeatReservationData();
        Scanner input = new Scanner(System.in);
        while (true) {
            String username = login(input);
            if (username != null) {
                System.out.println("Login Successful! Welcome, " + username + "!");
                System.out.println("Most Popular Places to Travel are ");
                viewPopularPlaces();
                System.out.print("Enter the number of your desired destination: ");
                int destinationChoice = input.nextInt();
                selectedDestination = getDestinationName(destinationChoice);
                System.out.println("You selected: " + selectedDestination);

                while (true) {
                    System.out.println("1. Reserve a seat for " + selectedDestination);
                    System.out.println("2. View all seats for " + selectedDestination);
                    System.out.println("3. Check Flight Status for " + selectedDestination);
                    System.out.println("4. Check Destination Temperature for " + selectedDestination);
                    System.out.println("5. Check Destination Airport for " + selectedDestination);
                    System.out.println("6. Change Destination");
                    System.out.println("7. Logout");
                    int choice = input.nextInt();
                    switch (choice) {
                        case 1:
                            reserveSeat(selectedDestination);
                            break;
                        case 2:
                            viewSeats(selectedDestination);
                            break;
                        case 3:
                            checkFlightStatus(selectedDestination);
                            break;
                        case 4:
                            checkDestinationTemperature(selectedDestination);
                            break;
                        case 5:
                            checkDestinationAirport(selectedDestination);
                            break;
                        case 6:
                            selectedDestination = null;
                            break;
                        case 7:
                            saveSeatReservationData();
                            System.out.println("Logged out successfully.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    if (choice == 7 || selectedDestination == null) {
                        break;
                    }
                }
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    private static String login(Scanner input) {
        System.out.print("Enter username: ");
        String username = input.next();
        System.out.print("Enter password: ");
        String password = input.next();

        for (int i = 0; i < USERS.length; i++) {
            if (username.equalsIgnoreCase(USERS[i]) && password.equals(PASSWORDS[i])) {
                return USERS[i];
            }
        }
        return null;
    }

    private static void viewPopularPlaces() {
        System.out.println("Popular Places to Travel:");
        System.out.println("1. Paris");
        System.out.println("2. New York City");
        System.out.println("3. Tokyo");
        System.out.println("4. London");
    }

    private static String getDestinationName(int choice) {
        switch (choice) {
            case 1:
                return "Paris";
            case 2:
                return "New York City";
            case 3:
                return "Tokyo";
            case 4:
                return "London";
            default:
                return null;
        }
    }

    private static void reserveSeat(String destination) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter seat number for " + destination + ": ");
        int seatNum = input.nextInt();
        if (seatNum >= 1 && seatNum <= 100) {
            if (!seats[seatNum - 1]) {
                seats[seatNum - 1] = true;
                seatReservationData.put(Integer.toString(seatNum), destination);
                System.out.println("Your seat for " + destination + " has been reserved. Thank you.");
            } else {
                System.out.println("Sorry, this seat for " + destination + " is already reserved.");
            }
        } else {
            System.out.println("Invalid seat number.");
        }
    }

    private static void viewSeats(String destination) {
        System.out.println("Seats for " + destination + ":");
        for (int i = 0; i < seats.length; i++) {
            int seatNumber = i + 1;
            System.out.print("Seat " + seatNumber + ": ");
            if (seats[i]) {
                System.out.println("Reserved");
            } else {
                System.out.println("Available");
            }
        }
    }

    private static void checkFlightStatus(String destination) {
        if (isFlightDelayed) {
            System.out.println("The flight to " + destination + " is delayed.");
        } else {
            System.out.println("The flight to " + destination + " is on time.");
        }
    }

    private static void checkDestinationTemperature(String destination) {
        int temperature;
        switch (destination) {
            case "Paris":
                temperature = 13;
                break;
            case "New York City":
                temperature = 8;
                break;
            case "Tokyo":
                temperature = 21;
                break;
            case "London":
                temperature = 11;
                break;
            default:
                temperature = 0;
                break;
        }
        System.out.println("The temperature at " + destination + " is " + temperature + "°C.");
    }

    private static void checkDestinationAirport(String destination) {
        switch (destination) {
            case "Paris":
                System.out.println(" The Airport of Paris is called Paris Charles de Gaulle Airport");
                break;
            case "New York City":
                System.out.println("The Airport of New York City is called John F. Kennedy International Airport");
                break;
            case "Tokyo":
                System.out.println("The Airport Of Tokyo is Called Haneda Airport");
                break;
            case "London":
                System.out.println("The Airport Of London is called London International Airport");
        }
    }

    private static void loadSeatReservationData() {
        try {
            FileReader reader = new FileReader("seat_reservation_data.json");
            int character;
            StringBuilder data = new StringBuilder();
            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }
            JSONObject jsonObject = new JSONObject(data.toString());
            seatReservationData = jsonObject;
            for (String key : seatReservationData.keySet()) {
                int seatNum = Integer.parseInt(key);
                String destination = seatReservationData.getString(key);
                seats[seatNum - 1] = true;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No previous seat reservation data found.");
        }
    }

    private static void saveSeatReservationData() {
        try {
            FileWriter writer = new FileWriter("seat_reservation_data.json");
            writer.write(seatReservationData.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error occurred while saving seat reservation data.");
        }
    }
}
