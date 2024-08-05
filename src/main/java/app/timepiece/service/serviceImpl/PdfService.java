package app.timepiece.service.serviceImpl;

import app.timepiece.entity.Report;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PdfService {

    public ByteArrayInputStream exportReportToPdf(Report report) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add title and subtitle
        Paragraph title = new Paragraph("Timepiece Appraisals")
                .setBold()
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph subtitle = new Paragraph("Giấy thẩm định đồng hồ")
                .setItalic()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);
        document.add(subtitle);

        // Add Report ID
        Paragraph reportId = new Paragraph("Mã: " + report.getId())
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20);

        document.add(reportId);


        if (report.getReportImages() != null && !report.getReportImages().isEmpty()) {
            try {
                String mainImageUrl = report.getReportImages().get(0).getImageUrl();
                ImageData mainImageData = ImageDataFactory.create(new URL(mainImageUrl));
                Image mainImage = new Image(mainImageData).scaleToFit(150, 150).setMarginBottom(20);
                document.add(mainImage);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi tải ảnh
                Paragraph error = new Paragraph("Error loading image")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(error);
            }
        }

        // Create information table
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100));

        // Add rows to the table
        table.addCell(new Cell().add(new Paragraph("Thương hiệu:")));
        table.addCell(new Cell().add(new Paragraph(report.getBrand())));

        table.addCell(new Cell().add(new Paragraph("Mẫu mã:")));
        table.addCell(new Cell().add(new Paragraph(report.getModel())));

        table.addCell(new Cell().add(new Paragraph("Số tham chiếu:")));
        table.addCell(new Cell().add(new Paragraph(report.getReferenceCode())));

        table.addCell(new Cell().add(new Paragraph("Chất liệu vỏ:")));
        table.addCell(new Cell().add(new Paragraph(report.getMaterial())));

        table.addCell(new Cell().add(new Paragraph("Chất liệu dây đeo:")));
        table.addCell(new Cell().add(new Paragraph(report.getWatchStrap())));

        table.addCell(new Cell().add(new Paragraph("Năm sản xuất:")));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(report.getYearProduced()))));

        table.addCell(new Cell().add(new Paragraph("Trạng thái:")));
        table.addCell(new Cell().add(new Paragraph(report.getWatchStatus())));

        table.addCell(new Cell().add(new Paragraph("Tình trạng:")));
        table.addCell(new Cell().add(new Paragraph(report.getAccessories())));

        table.addCell(new Cell().add(new Paragraph("Giới tính:")));
        table.addCell(new Cell().add(new Paragraph(report.getWatchType())));

        table.addCell(new Cell().add(new Paragraph("Nơi sản xuất:")));
        table.addCell(new Cell().add(new Paragraph(report.getOrigin())));

        table.addCell(new Cell().add(new Paragraph("Kích thước:")));
        table.addCell(new Cell().add(new Paragraph(report.getSize())));

        // Add estimated value
        table.addCell(new Cell().add(new Paragraph("Giá trị ước tính:")));
        table.addCell(new Cell().add(new Paragraph(report.getCommentValue() + " VND")));

        document.add(table);

        // Footer
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        Paragraph footer = new Paragraph()
                .add("Ngày " + dateStr + "\n")
                .add("Người thẩm định\n\n")
                .add(report.getUser().getName())
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(50);

        document.add(footer);

        // Close the document
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}