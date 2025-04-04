package com.ecom.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User") // Tên bảng theo diagram
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Các cột theo diagram: name, phone_number, password, role, enable, note...
    // Bổ sung email nếu DB có
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    private String role;

    // Nếu diagram có cột "enable" (hoặc "is_enable"):
    @Column(name = "enable")
    private Boolean enable;

    // Nếu diagram có cột "note" (string)
    private String note;

    // Nếu diagram có cột "email"
    private String email;
    private String profileImage;   // cột profile_image
    private String resetToken;     // cột reset_token
    // + getters, setters
    
}
