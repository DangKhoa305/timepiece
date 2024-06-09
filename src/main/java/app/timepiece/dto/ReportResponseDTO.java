package app.timepiece.dto;

import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReportResponseDTO {
    private String brand;
    private String model;
    private String referenceCode;
    private String watchType;
    private String material;
    private String watchStrap;
    private int yearProduced;
    private String watchStatus;
    private String accessories;
    private String watchGender;
    private String origin;
    private String size;
    private String userName;
    private String commentValue;
    private Date createDate;
    private String reportStatus;
    private List<String> imageUrls;
}