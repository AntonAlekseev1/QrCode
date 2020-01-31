package com.antqr.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

public class QrCodeEncoder {

    public static File createQrFile(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            return new File(filePath);
        } else {
            System.out.println("Error: file path not entered");
            System.exit(0);
            return null;
        }
    }

    public static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix biteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size);
        MatrixToImageWriter.writeToPath(biteMatrix, fileType, qrFile.toPath());
    }
}
