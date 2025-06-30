import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Booking {
    String name, roomType;
    Booking(String name, String roomType) {
        this.name = name;
        this.roomType = roomType;
    }
    public String toString() {
        return name + "," + roomType;
    }
}

public class HotelApp {
    static File file = new File("bookings.txt");

    public static void main(String[] args) {
        JFrame f = new JFrame("Hotel Booking");
        JTextField nameField = new JTextField(); nameField.setBounds(100, 30, 150, 20);
        JComboBox<String> roomBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});
        roomBox.setBounds(100, 60, 150, 20);
        JTextArea output = new JTextArea(); output.setBounds(20, 150, 250, 100);
        JButton bookBtn = new JButton("Book"), cancelBtn = new JButton("Cancel"), viewBtn = new JButton("View");
        bookBtn.setBounds(20, 100, 70, 20); cancelBtn.setBounds(100, 100, 80, 20); viewBtn.setBounds(190, 100, 70, 20);
        f.add(new JLabel("Name:")).setBounds(20, 30, 80, 20);
        f.add(new JLabel("Room:")).setBounds(20, 60, 80, 20);
        f.add(nameField); f.add(roomBox); f.add(output); f.add(bookBtn); f.add(cancelBtn); f.add(viewBtn);
        f.setSize(300, 300); f.setLayout(null); f.setVisible(true); f.setDefaultCloseOperation(3);

        bookBtn.addActionListener(e -> {
            try (FileWriter fw = new FileWriter(file, true)) {
                Booking b = new Booking(nameField.getText(), roomBox.getSelectedItem().toString());
                fw.write(b + "\n");
                output.setText("Payment done. Room booked.");
            } catch (Exception ex) {
                output.setText("Error booking.");
            }
        });

        cancelBtn.addActionListener(e -> {
            String entry = nameField.getText() + "," + roomBox.getSelectedItem();
            List<String> lines = new ArrayList<>();
            boolean found = false;
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (!found && line.equals(entry)) { found = true; continue; }
                    lines.add(line);
                }
            } catch (Exception ex) {}
            try (FileWriter fw = new FileWriter(file)) {
                for (String l : lines) fw.write(l + "\n");
                output.setText(found ? "Booking cancelled." : "Not found.");
            } catch (Exception ex) {
                output.setText("Error.");
            }
        });

        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Bookings:\n");
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) sb.append(sc.nextLine()).append("\n");
            } catch (Exception ex) {}
            output.setText(sb.toString().equals("Bookings:\n") ? "No bookings." : sb.toString());
        });
    }
}
