package app.timepiece.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDTO {
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private String name;
    private String birthday;
    private String gender;
}
