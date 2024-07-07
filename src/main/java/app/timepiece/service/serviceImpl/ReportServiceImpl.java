package app.timepiece.service.serviceImpl;

import app.timepiece.dto.ReportDTO;
import app.timepiece.dto.ReportResponseDTO;
import app.timepiece.dto.SearchReportDTO;
import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.Report;
import app.timepiece.entity.ReportImage;
import app.timepiece.entity.User;
import app.timepiece.repository.AppraisalRequestRepository;
import app.timepiece.repository.ReportImageRepository;
import app.timepiece.repository.ReportRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportImageRepository reportImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private AppraisalRequestRepository appraisalRequestRepository;

    @Override
    public Report createReport(ReportDTO reportDTO) {
        User user = userRepository.findById(reportDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AppraisalRequest appraisalRequest = appraisalRequestRepository.findById(reportDTO.getAppraisalRequestId())
                .orElseThrow(() -> new RuntimeException("AppraisalRequest not found"));

        Report report = new Report();
        report.setUser(user);
        report.setBrand(reportDTO.getBrand());
        report.setModel(reportDTO.getModel());
        report.setReferenceCode(reportDTO.getReferenceCode());
        report.setWatchType(reportDTO.getWatchType());
        report.setMaterial(reportDTO.getMaterial());
        report.setWatchStrap(reportDTO.getWatchStrap());
        report.setYearProduced(reportDTO.getYearProduced());
        report.setWatchStatus(reportDTO.getWatchStatus());
        report.setReportStatus("true");
        report.setAccessories(reportDTO.getAccessories());
        report.setOrigin(reportDTO.getOrigin());
        report.setSize(reportDTO.getSize());
        report.setCreateDate(new Date());
        report.setCommentValue(reportDTO.getCommentValue());
        report.setAppraisalRequest(appraisalRequest);

        Report savedReport = reportRepository.save(report);

        for (MultipartFile imageFile : reportDTO.getImageFiles()) {
            try {
                Map uploadResult = cloudinaryService.uploadFile(imageFile);
                String uploadedImageUrl = uploadResult.get("url").toString();
                ReportImage reportImage = new ReportImage();
                reportImage.setReport(savedReport);
                reportImage.setImageUrl(uploadedImageUrl);
                reportImageRepository.save(reportImage);
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        return savedReport;
    }


    @Override
    public ReportResponseDTO getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));

        List<ReportImage> images = reportImageRepository.findByReportId(id);

        List<String> imageUrls = images.stream()
                .map(ReportImage::getImageUrl)
                .collect(Collectors.toList());

        return ReportResponseDTO.builder()
                .brand(report.getBrand())
                .model(report.getModel())
                .referenceCode(report.getReferenceCode())
                .watchType(report.getWatchType())
                .material(report.getMaterial())
                .watchStrap(report.getWatchStrap())
                .userName(report.getUser().getName())
                .yearProduced(report.getYearProduced())
                .watchStatus(report.getWatchStatus())
                .accessories(report.getAccessories())
                .origin(report.getOrigin())
                .size(report.getSize())
                .createDate(report.getCreateDate())
                .commentValue(report.getCommentValue())
                .reportStatus(report.getReportStatus())
                .imageUrls(imageUrls)
                .build();
    }

    @Override
    public Page<SearchReportDTO> searchReports(Long id, String brand, String reportStatus, Date createDate, Pageable pageable) {
        Page<Report> reportPage = reportRepository.searchReport(
                id, brand, reportStatus, createDate, pageable);


        return new PageImpl<>(
                reportPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()),
                pageable,
                reportPage.getTotalElements()
        );
    }

    private SearchReportDTO convertToDTO(Report report) {
        SearchReportDTO dto = new SearchReportDTO();
        dto.setId(report.getId());
        dto.setBrand(report.getBrand());
        dto.setReportStatus(report.getReportStatus());
        dto.setCreateDate(report.getCreateDate());
        return dto;
    }

    @Override
    public Page<SearchReportDTO> searchReportsByUserId(Long userId, Pageable pageable) {
        Page<Report> reports = reportRepository.searchReportByUserId(userId, pageable);
        return reports.map(this::convertToDTO);
    }
}
