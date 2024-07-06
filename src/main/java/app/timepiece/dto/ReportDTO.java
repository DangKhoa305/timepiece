package app.timepiece.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {

    private String brand;
    private String model;
    private String referenceCode;
    private String watchType;
    private String material;
    private String watchStrap;
    private int yearProduced;
    private String watchStatus;
    private String accessories;
    private String origin;
    private String size;
    private Long userId;
    private String commentValue;
    private List<MultipartFile> imageFiles;
    private Long appraisalRequestId;

}