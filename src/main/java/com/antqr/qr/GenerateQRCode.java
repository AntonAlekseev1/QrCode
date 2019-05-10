package com.antqr.qr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

public class GenerateQRCode {

    private static final String DEFAULT_WIDTH_AND_HEIGHT_MESSAGE = "Default width and height = ";
    private static final int DEFAULT_WIDTH = 125;

    public static void main(String[] args) throws WriterException, IOException {
        String qrCodeText = getStringFromConsole("Please enter data to encode: ");
        if (!StringUtils.isEmpty(qrCodeText)) {
            String filePath = getStringFromConsole("Please enter the file path like '/home/user/Desktop/hello.png': ");
            File qrFile = createQrFile(filePath);
            String fileType = filePath.substring(filePath.indexOf(".")).replace(".", "");
            String width = getStringFromConsole("Please enter the image width and height(Default = " + DEFAULT_WIDTH + "): ");
            int size;
            try {
                size = Integer.parseInt(width);
                if (size <= 0) {
                    size = DEFAULT_WIDTH;
                    System.out.println(DEFAULT_WIDTH_AND_HEIGHT_MESSAGE + size);
                }
            } catch (NumberFormatException e) {
                size = DEFAULT_WIDTH;
                System.out.println(DEFAULT_WIDTH_AND_HEIGHT_MESSAGE + size);
            }
            createQRImage(qrFile, qrCodeText, size, fileType);
            System.out.println("DONE");
        } else {
            System.out.println("Error: no data to encode");
        }
    }

    private static String getStringFromConsole(String message) {
        BufferedReader appInput = new BufferedReader(new InputStreamReader(System.in));
        String data = null;
        System.out.print(message);
        while (data == null) {
            try {
                data = appInput.readLine().trim();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
        return data;
    }

    private static File createQrFile(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            return new File(filePath);
        } else {
            System.out.println("Error: file path not entered");
            System.exit(0);
            return null;
        }
    }

    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        createGraphics(image, matrixWidth, byteMatrix);
        ImageIO.write(image, fileType, qrFile);
    }

    private static void createGraphics(BufferedImage image, int matrixWidth, BitMatrix byteMatrix) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }

}