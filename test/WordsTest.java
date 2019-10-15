import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import org.junit.Test;
import utils.string.StringUtil;

public class WordsTest {

    @Test
    public void testPy() throws PinyinException {
        String text = "测试";
        String separator = ",";
        PinyinFormat format = PinyinFormat.WITH_TONE_MARK;
        System.out.println(StringUtil.getPy(text, separator, format));
    }
}
