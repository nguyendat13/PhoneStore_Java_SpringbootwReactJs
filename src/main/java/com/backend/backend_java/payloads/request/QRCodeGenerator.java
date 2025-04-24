package com.backend.backend_java.payloads.request;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class QRCodeGenerator {

    // Update the method to take only one parameter: paymentData
    public static ByteArrayOutputStream generateQRCode(String paymentData) throws WriterException, IOException {
        // Tạo các tham số mã hóa cho QR code
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Tạo QR code dưới dạng BitMatrix
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(paymentData, BarcodeFormat.QR_CODE, 200, 200, hints);

        // Chuyển BitMatrix thành BufferedImage
        BufferedImage bufferedImage = toBufferedImage(bitMatrix);

        // Lưu BufferedImage vào ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);

        // Trả về ByteArrayOutputStream chứa ảnh QR
        return byteArrayOutputStream;
    }

    // Chuyển BitMatrix thành BufferedImage
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(i, j, (matrix.get(j, i) ? 0x000000 : 0xFFFFFF)); // Màu đen và trắng cho QR code
            }
        }
        return image;
    }
}
