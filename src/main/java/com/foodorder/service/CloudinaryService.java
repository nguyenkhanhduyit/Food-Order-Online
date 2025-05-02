package com.foodorder.service;

import com.cloudinary.Cloudinary;
import com.foodorder.service.iservice.ICloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements ICloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File upload is empty");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("File upload must be an image");
        }
        if (file.getSize() > (5 * 1024 * 1024)) {
            throw new IllegalArgumentException("File too large");
        }
        try {
            Map<String, Object> uploadOptions = Map.of(
                    "folder", "food-order-online/media",
                    "resource_type", "image"
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new IOException("Failed to upload image to Cloudinary", e);
        }
    }


    public void deleteImage(String imageUrl) {
        try {
           if(!imageUrl.isEmpty()) {
               String publicId = extractPublicId(imageUrl);
               System.out.println("Deleting image with publicId: " + publicId); // Thêm log để debug
               Map result = cloudinary.uploader().destroy(publicId, Map.of("resource_type", "image"));
               System.out.println("Delete result: " + result); // Thêm log để debug
               String status = (String) result.get("result");
               if (!"ok".equals(status)) {
                   throw new RuntimeException("Failed to delete image on Cloudinary: " + result.toString());
               }
           }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    private String extractPublicId(String imageUrl) {
        // Ví dụ: https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/food-order-online/media/image-id.jpg?t=123
        String[] parts = imageUrl.split("/");
        // Tìm vị trí của "food-order-online/media"
        StringBuilder publicId = new StringBuilder();
        boolean foundFolder = false;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("food-order-online") && i + 1 < parts.length && parts[i + 1].equals("media")) {
                foundFolder = true;
                publicId.append(parts[i]).append("/").append(parts[i + 1]);
                i += 1; // Bỏ qua phần "media"
                continue;
            }
            if (foundFolder && i == parts.length - 1) {
                String fileName = parts[i];
                // Loại bỏ query parameter nếu có (ví dụ: image-id.jpg?t=123 -> image-id.jpg)
                fileName = fileName.split("\\?")[0];
                // Loại bỏ phần mở rộng (ví dụ: image-id.jpg -> image-id)
                publicId.append("/").append(fileName.substring(0, fileName.lastIndexOf(".")));
            }
        }
        return publicId.toString();
    }
}
