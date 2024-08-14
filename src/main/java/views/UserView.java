package views;

import dao.DataDAO;
import model.Data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;

    public UserView(String email) {
        this.email = email;
    }

    public void home() {
        do {
            System.out.println("Welcome " + this.email);
            System.out.println("Press 1 to show hidden files");
            System.out.println("Press 2 to hide a new file");
            System.out.println("Press 3 to unhide a file");
            System.out.println("Press 0 to exit");
            Scanner sc = new Scanner(System.in);
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> {
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("Id - File Name");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                    } catch (SQLException e) {
                        System.err.println("Error fetching hidden files: " + e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        System.out.println("Enter the file path:");
                        String path = sc.nextLine().trim();  // Trim the input

                        // Print out the path for debugging
                        System.out.println("Read path: " + path);

                        File f = new File(path);
                        if (f.exists()) {
                            Data file = new Data(0, f.getName(), path, this.email);
                            DataDAO.hideFile(file);
                            System.out.println("File hidden successfully.");
                        } else {
                            System.out.println("File does not exist at the specified path.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error hiding file: " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("Error processing file: " + e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("Id - File Name");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                        System.out.println("Enter the id of the file to unhide: ");
                        int id = Integer.parseInt(sc.nextLine());

                        boolean isValidId = false;
                        for (Data file : files) {
                            if (file.getId() == id) {
                                isValidId = true;
                                break;
                            }
                        }

                        if (isValidId) {
                            DataDAO.unhide(id);
                            System.out.println("File unhidden successfully.");
                        } else {
                            System.out.println("Invalid file id.");
                        }
                    } catch (SQLException e) {
                        System.err.println("Error unhiding file: " + e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 0 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }
}
