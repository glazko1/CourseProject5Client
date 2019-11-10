package util;

import cooperation.ClientRequest;
import main.Runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

public class ImageSender {

    private static final ImageSender INSTANCE = new ImageSender();

    public static ImageSender getInstance() {
        return INSTANCE;
    }

    private ImageSender() {}

    public void sendImage(File image, String name) throws IOException {
        InputStream imageStream = new FileInputStream(image);
        byte[] imageData = new byte[(int) image.length()];
        imageStream.read(imageData);
        String imageString = Base64.getEncoder().encodeToString(imageData);
        imageStream.close();
        Runner.sendData(new ClientRequest("sendImage", Map.of("image", imageString, "name", name)));
    }
}