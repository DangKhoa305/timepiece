package app.timepiece.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowWatchDTO {
    private Long id;
    private String name;
    private double price;
    private String status;
    private String imageUrl;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Double userRatingScore;
    private String area;
    private String address;
    private Date createDate;
    private boolean hasAppraisalCertificate;
    private String appraisalCertificateUrl;

}
