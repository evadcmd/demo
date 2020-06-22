package com.example.demo.app.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
public class RTSPCamera {
    private Java2DFrameConverter converter = new Java2DFrameConverter();

    @Value("${rtsp.pattern}")
    private String rtspPattern;

    // file path to save pictures
    @Setter
    private String filePath = "/Users/";

    @Autowired
    private SimpMessagingTemplate message;

    public void test(String ip) {
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(
                String.format(this.rtspPattern, ip)
            );
            grabber.setOption("rtsp_transport", "tcp");
            grabber.setFrameRate(10);
            grabber.setImageWidth(960);
            grabber.setImageWidth(540);
            grabber.start();
            // print on canvas
            System.setProperty("java.awt.headless", "false");
            CanvasFrame canvasFrame = new CanvasFrame("Test");
            canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            canvasFrame.setAlwaysOnTop(true);
            // OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

            while (true) {
                Frame frame = grabber.grabImage();
                // Mat mat = converter.convertToMat(frame);
                canvasFrame.showImage(frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTo(String ip, String filePath, int limit) {
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(
                String.format(this.rtspPattern, ip)
            );
            grabber.setOption("rtsp_transport", "tcp");
            grabber.setFrameRate(10);
            grabber.setImageWidth(960);
            grabber.setImageWidth(540);
            grabber.start();
            for (int i = 0; i < limit; i++) {
                BufferedImage image = converter.getBufferedImage(grabber.grabImage());
                ImageIO.write(image, "jpeg", new File(String.format(filePath, System.currentTimeMillis())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void view(String ip) {
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(
                String.format(this.rtspPattern, ip)
            );
            grabber.setOption("rtsp_transport", "tcp");
            grabber.setFrameRate(10);
            grabber.setImageWidth(960);
            grabber.setImageWidth(540);
            grabber.start();
            while (true) {
                BufferedImage image = converter.getBufferedImage(grabber.grabImage());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", os);
                // "/"topic => "/" is necessary
                message.convertAndSend("/topic", Base64.getEncoder().encodeToString(os.toByteArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}