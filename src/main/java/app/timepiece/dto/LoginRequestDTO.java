package app.timepiece.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    public String email;
    public String password;

    // Getters và setters
}