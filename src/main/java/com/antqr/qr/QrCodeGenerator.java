package com.antqr.qr;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.zxing.WriterException;
import org.apache.commons.lang3.StringUtils;

public class QrCodeGenerator {

    private static final String DEFAULT_WIDTH_AND_HEIGHT_MESSAGE = "Default width and height = ";
    private static final int DEFAULT_WIDTH = 125;

    public static void main(String[] args) throws WriterException, IOException {
        String command = getStringFromConsole("To create Qr code image press 'c' \n To decode Qr code image press 'd'");
        switch (command) {
            case "c":
            case "C":
                generateQrCode();
                break;
            case "d":
            case "D":
                decodeQrCode();
                break;
            default:
                System.out.println("Command not found");
        }
    }

    private static void generateQrCode() throws WriterException, IOException {
        String qrCodeText = getStringFromConsole("Please enter data to encode: ");
        if (!StringUtils.isEmpty(qrCodeText)) {
            String filePath = getStringFromConsole("Please enter the path to save file like '/home/user/Desktop/hello.png': ");
            File qrFile = QrCodeEncoder.createQrFile(filePath);
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
            QrCodeEncoder.createQRImage(qrFile, qrCodeText, size, fileType);
            System.out.println("DONE");
        } else {
            System.out.println("Error: no data to encode");
        }
    }

    private static void decodeQrCode() {
        try {
            String filePath = getStringFromConsole("Please enter the path to QR code image like '/home/user/Desktop/hello.png': ");
            File file = new File(filePath);
            String decodedText = QrCodeDecoder.decodeQRCode(file);
            if (decodedText == null) {
                System.out.println("No QR Code found in the image");
            } else {
                System.out.println("Decoded text = " + decodedText);
            }
        } catch (IOException e) {
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
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
}