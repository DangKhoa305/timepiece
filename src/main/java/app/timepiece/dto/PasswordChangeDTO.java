package app.timepiece.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
