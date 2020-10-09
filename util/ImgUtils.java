package com.zzsim.gz.airport.wma.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author qinjp
 * @date 2020/10/9
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImgUtils {

    //base64 图片大小计算 base64.length() - base64.length() / 8 * 2

    public static String resizeImageTo40K(String base64Img) {
        try {
            BufferedImage src = base64String2BufferedImage(base64Img);
            // 指定尺寸压缩
            BufferedImage output = Thumbnails.of(src).size(src.getWidth() / 3, src.getHeight() / 3).asBufferedImage();
            String base64 = imageToBase64(output);
            if (base64.length() - base64.length() / 8 * 2 > 40000) {
                // 进行指定比例的压缩
                output = Thumbnails.of(output).scale(1 / (base64.length() / 40000)).asBufferedImage();
                base64 = imageToBase64(output);
            }
            return base64;
        } catch (Exception e) {
            return base64Img;
        }

    }


    private static String imageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new String(Base64.getEncoder().encode((baos.toByteArray())));
    }


    private static BufferedImage base64String2BufferedImage(String base64string) {
        BufferedImage image = null;
        try {
            InputStream stream = baseToInputStream(base64string);
            image = ImageIO.read(stream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return image;
    }

    private static InputStream baseToInputStream(String base64string){
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return stream;
    }


}
