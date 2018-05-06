package com.xhcoding.helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码工具类
 * Created by Max on 2017/3/30.
 */
public class QRCodeHelper {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 300;
    public static final String GIF = "jpg";
    public static final String UTF8 = "utf-8";
    public static final int margin = 1;

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of appendByTail " + format + " to " + file);
        }
    }

    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        writeToStream(matrix, format, null, 0, 0, stream);
    }

    /**
     * 写入待logo的二维码
     *
     * @param matrix
     * @param format
     * @param logoStream
     * @param stream
     * @throws IOException
     */
    private static void writeToStream(BitMatrix matrix, String format, InputStream logoStream, int logoWidth, int logoHeight, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);

        if (!ObjectUtils.isEmpty(logoStream)) { //载入logo
            int qrCodeWidth = image.getWidth();//二维码宽度
            int qrCodeHeight = image.getHeight();//二维码高度

            Graphics2D gs = image.createGraphics();
            Image img = ImageIO.read(logoStream);

            gs.drawImage(img, (qrCodeWidth - logoWidth) / 2, (qrCodeHeight - logoHeight) / 2, logoWidth, logoHeight, null);
            gs.dispose();
            img.flush();
        }
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of appendByTail " + format);
        }
    }

    /**
     * 输出流的方式创建二维码 默认内容字符集 utf8 宽高300 图片格式gif
     *
     * @param content      二维码内容
     * @param outputStream 输出流
     * @throws Exception
     */
    public static void createQRCodeTStream(String content, OutputStream outputStream) throws Exception {
        createQRCodeTStream(content, null, 0, 0, outputStream);
    }

    /**
     * 输出流的方式创建待logo二维码 默认内容字符集 utf8 宽高300 图片格式gif
     *
     * @param content      二维码内容
     * @param outputStream 输出流
     * @throws Exception
     */
    public static void createQRCodeTStream(String content, InputStream logoStream, int logoWidth, int logoHeight, OutputStream outputStream) throws Exception {
        createQRCodeTStream(content, UTF8, ErrorCorrectionLevel.H, outputStream, logoStream, logoWidth, logoHeight, DEFAULT_WIDTH, DEFAULT_HEIGHT, GIF);
    }

    /**
     * 输出流的方式创建二维码
     *
     * @param content              二维码内容
     * @param conentCharacterSet   内容字符集
     * @param errorCorrectionLevel 错误修正级别
     * @param outputStream         输出流
     * @param width                宽
     * @param height               高
     * @param format               图片格式
     * @throws Exception
     */
    public static void createQRCodeTStream(String content, String conentCharacterSet, ErrorCorrectionLevel errorCorrectionLevel, OutputStream outputStream,
                                           InputStream logoStream, int logoWidth, int logoHeight, int width, int height, String format) throws Exception {

        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, conentCharacterSet);    // 内容所使用字符集编码
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);   //错误修正级别
        hints.put(EncodeHintType.MARGIN, margin);//设置白边大小
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 生成二维码
        bitMatrix = setMargin(bitMatrix, 5);
        writeToStream(bitMatrix, format, logoStream, logoWidth, logoHeight, outputStream);
    }

    private static BitMatrix setMargin(BitMatrix matrix, int margin) {

        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {   //循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

}
