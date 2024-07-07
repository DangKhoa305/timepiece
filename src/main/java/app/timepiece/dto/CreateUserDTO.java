package app.timepiece.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Birthday is required")
    private String birthday;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Avatar is required")
    private String avatar;


}

