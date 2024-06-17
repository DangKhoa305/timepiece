package app.timepiece.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchDTO {
    private long id;
    private Long userId;
    private String name;
    private String watchStatus;
    private String status;
    private String description;
    private double price;
    private String brandName;
    private int yearProduced;
    private String model;
    private String material;
    private String watchStrap;
    private String size;
    private String accessories;
    private String referenceCode;
    private String placeOfProduction;
    private String address;
    private Date createDate;
    private Date updateDate;
    private String watchTypeName;
}

