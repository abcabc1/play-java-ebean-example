package utils.string;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HtmlUtil {

    public static Map extractFile(File file) {
        Map map = new HashMap();
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
