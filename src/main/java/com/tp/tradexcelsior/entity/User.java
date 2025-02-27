package com.tp.tradexcelsior.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  private String id;
  private String firstName;
  private String lastName;
  private String occupation;

  @Indexed(unique = true)
  private String mobileNumber;

  @Indexed(unique = true)
  private String email;

  private String password;
  private String role;
  private String licence;
  private String address;
  private LocalDate subscriptionStartDate;
  private LocalDate subscriptionEndDate;
  private int subscriptionDuration;

//  private boolean deleted;
}
