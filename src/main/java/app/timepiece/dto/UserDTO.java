package app.timepiece.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDTO {
    private Long id;
    private String name;
    private String address;
    private String avatar;
    private String phoneNumber;
    private String status;
    private Date dateCreate;
    private String gender;
    private String birthday;
    private String citizenID;
}