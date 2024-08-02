package app.timepiece.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult;
    }

    public Map<String, Object> uploadPdf(InputStream pdfStream, String fileName) throws IOException {
        // Read bytes from input stream
        byte[] pdfBytes = pdfStream.readAllBytes();

        // Upload PDF to Cloudinary as 'image' resource type with format 'pdf'
        return cloudinary.uploader().upload(pdfBytes, ObjectUtils.asMap(
                "resource_type", "image",  // Set resource_type as 'image'
                "public_id", fileName,     // Set the public ID (without .pdf extension)
                "format", "pdf"            // Specify the format as 'pdf'
        ));
    }
}
