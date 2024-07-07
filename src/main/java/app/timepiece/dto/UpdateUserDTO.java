package app.timepiece.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Avatar is required")
    private MultipartFile avatar;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Birthday is required")
    private String birthday;

    @NotBlank(message = "Citizen ID is required")
    private String citizenID;
}
