package com.tp.tradexcelsior.startup;

import com.tp.tradexcelsior.entity.User;
import com.tp.tradexcelsior.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;

@Component
public class UserCommandLineRunner implements CommandLineRunner {

  @Autowired
  private UserRepo userRepo; // Assuming you have a repository for User

  @Override
  public void run(String... args) throws Exception {
    // Check if the admin user already exists
    String adminEmail = "admin@tradexcelsior.com"; // Admin's email (or any unique field)

    try {
      User existingAdmin = userRepo.findByEmail(adminEmail).orElse(null);

      if (existingAdmin == null) {
        // If no admin user found, create a new admin
        createAdminUser();
      } else {
        System.out.println("Admin already exists, skipping creation.");
      }
    } catch (DataAccessException ex) {
      // This will catch any database-related exceptions (e.g., connection issues)
      System.err.println("Database error while checking admin user: " + ex.getMessage());
    } catch (Exception ex) {
      // Catch any other unexpected exceptions
      System.err.println("An unexpected error occurred: " + ex.getMessage());
    }
  }

  private void createAdminUser() {
    try {
      // Create an admin user
      User admin = new User();
      admin.setFirstName("Admin");
      admin.setLastName("Admin");
      admin.setEmail("admin@tradexcelsior.com");
      admin.setPassword("admin123"); // Set the password
      admin.setRole("ADMIN"); // Assign the role as ADMIN
      admin.setMobileNumber("1234567890");
      admin.setOccupation("Administrator");
      admin.setAddress("Admin address");
      admin.setSubscriptionStartDate(LocalDate.now());
      admin.setSubscriptionEndDate(LocalDate.now().plusYears(1)); // 1 year subscription
      admin.setSubscriptionDuration(1); // 1 year

      // Save the admin to the database
      userRepo.save(admin);
      System.out.println("Admin user added to the database.");
    } catch (DataAccessException ex) {
      // This will catch any database-related exceptions (e.g., connection issues)
      System.err.println("Database error while creating admin user: " + ex.getMessage());
    } catch (Exception ex) {
      // Catch any other unexpected exceptions
      System.err.println("An unexpected error occurred while creating admin: " + ex.getMessage());
    }
  }
}
