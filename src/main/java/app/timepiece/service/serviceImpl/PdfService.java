package app.timepiece.service.serviceImpl;

import app.timepiece.entity.Report;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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

        // Load Vietnamese font
        PdfFont vietnameseFont;
        try {
            // Ensure the path matches the location where you placed the font
            vietnameseFont = PdfFontFactory.createFont("src/main/resources/fonts/FreeSans.ttf", PdfEncodings.IDENTITY_H);
        } catch (IOException e) {
            e.printStackTrace();
            return new ByteArrayInputStream(out.toByteArray());
        }
        // Add title and subtitle
        Paragraph title = new Paragraph("Timepiece Appraisals")
                .setFont(vietnameseFont)
                .setBold()
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph subtitle = new Paragraph("Giấy thẩm định đồng hồ")
                .setFont(vietnameseFont)
                .setItalic()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);
        document.add(subtitle);

        // Add Report ID
        Paragraph reportId = new Paragraph("Mã: " + report.getId())
                .setFont(vietnameseFont)
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
        table.addCell(new Cell().add(new Paragraph("Thương hiệu:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getBrand()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Mẫu mã:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getModel()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Số tham chiếu:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getReferenceCode()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Chất liệu vỏ:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getMaterial()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Chất liệu dây đeo:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getWatchStrap()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Năm sản xuất:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(report.getYearProduced())).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Trạng thái:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getWatchStatus()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Tình trạng:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getAccessories()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Giới tính:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getWatchType()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Nơi sản xuất:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getOrigin()).setFont(vietnameseFont)));

        table.addCell(new Cell().add(new Paragraph("Kích thước:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getSize()).setFont(vietnameseFont)));

        // Add estimated value
        table.addCell(new Cell().add(new Paragraph("Giá trị ước tính:").setFont(vietnameseFont)));
        table.addCell(new Cell().add(new Paragraph(report.getCommentValue() + " VND").setFont(vietnameseFont)));

        document.add(table);

        // Footer
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        Paragraph footer = new Paragraph()
                .add("Ngày " + dateStr + "\n")
                .add("Người thẩm định\n\n")
                .add(report.getUser().getName())
                .setFont(vietnameseFont)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(50);

        document.add(footer);

        // Close the document
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}