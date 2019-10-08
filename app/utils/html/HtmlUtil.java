package utils.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlUtil {

    public static Map extractFile(String path) {

        File file = new File(path);
        try {
            Document doc = Jsoup.parse(file, "utf-8");
            return extractDictHtml(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, List<String>> extractDictHtml(Document document) {
        Map<String, List<String>> map = new HashMap<>();
        List typeList = document.select("ul.dict-basic-ul").select("li").eachText();
        Elements translationElements = document.select("div.layout").select(".sort").select("ol");
        for (int i = 0; i < typeList.size(); i++) {
            List<String> textList = eachText(translationElements.get(i).select("li"));
            map.put(typeList.get(i).toString(), textList);
        }
        return map;
    }

    public static List<String> eachText(Elements elements) {
        ArrayList<String> texts = new ArrayList<>(elements.size());
        for (Element el: elements) {
            if (el.hasText())
                texts.add(el.html());
        }
        return texts;
    }
}
