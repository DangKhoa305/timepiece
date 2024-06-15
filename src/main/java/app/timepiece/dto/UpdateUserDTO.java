package app.timepiece.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private String name;
    private String address;
    private MultipartFile avatar;
    private String phoneNumber;
    private String status;
    private String gender;
    private String birthday;
    private String citizenID;
}
