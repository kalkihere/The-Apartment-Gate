import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Visitor Management System =====");
            System.out.println("1. Log Visitor Entry");
            System.out.println("2. Log Visitor Exit");
            System.out.println("3. Flat Visit History");
            System.out.println("4. Visitors Inside");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> logEntry();
                case 2 -> logExit();
                case 3 -> flatHistory();
                case 4 -> visitorsInside();
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void logEntry() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Flat No: ");
            String flat = sc.nextLine();

            System.out.print("Enter Purpose: ");
            String purpose = sc.nextLine();

            String sql = "INSERT INTO visitors(name, flat_no, purpose) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, flat);
            ps.setString(3, purpose);

            ps.executeUpdate();
            System.out.println("Visitor Entry Added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void logExit() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter Visitor ID: ");
            int id = sc.nextInt();

            String sql = "UPDATE visitors SET exit_time = NOW() WHERE vis_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Exit Updated!");
            else
                System.out.println("Visitor Not Found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void flatHistory() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Enter Flat No: ");
            String flat = sc.nextLine();

            String sql = "SELECT * FROM visitors WHERE flat_no=? AND entry_time >= NOW() - INTERVAL 7 DAY";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, flat);

            ResultSet rs = ps.executeQuery();
            System.out.println("\nVisitors in last 7 days:");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("vis_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getTimestamp("entry_time")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void visitorsInside() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM visitors WHERE exit_time IS NULL";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            System.out.println("\nCurrently Inside:");

            while (rs.next()) {
                System.out.println(
                    rs.getString("name") + " | Flat: " +
                    rs.getString("flat_no") +
                    " | Entry: " + rs.getTimestamp("entry_time")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
