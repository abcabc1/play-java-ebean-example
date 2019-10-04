package utils.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import java.io.IOException;
import java.nio.file.Paths;

public class ImageUtil {

    public static void compress(String filePath) throws IOException {
        System.out.println(Paths.get(filePath));
        Thumbnails.of(Paths.get(filePath).toFile())
                .scale(1.5)
//                .size(640, 480)
                .outputFormat("jpg")
                .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
    }
}
