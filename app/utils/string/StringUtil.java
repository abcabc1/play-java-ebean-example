package utils.string;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

    public static String replaceSQL(String sql, String code) {
        sql = "count(id) id";
        code = "id";
        StringBuilder builder = new StringBuilder();
        String[] temps = sql.split(",");
        for (String temp : temps) {
            if (temp.contains(code)) {
                continue;
            }
            builder.append(temp).append(",");
        }
        if (builder.length() == 0) {
            return "1";
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static String rounding(Integer num, int space) {
        return String.format("%0" + space + "d", num);
    }

    public static String shuffle(String s) {
//        string.codePoints().mapToObj(c->(char)c).forEach(w->System.out.print(w));
        if (s.length() < 2) {
            return "";
        }
        List<Character> list = s.chars().mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(list);
        StringBuffer sb = new StringBuffer();
        list.forEach(sb::append);
        if (sb.toString().equalsIgnoreCase(s)) {
            return shuffle(sb.toString());
        }
        return sb.toString();
    }

    public static String shuffleWord(String s) {
        List<String> list = Arrays.asList(s.split(" "));
        Collections.shuffle(list);
        StringBuffer sb = new StringBuffer();
        for (String v : list) {
            sb.append(v).append(" ");
        }
        if (sb.toString().equalsIgnoreCase(s)) {
            return shuffleWord(sb.toString());
        }
        return sb.toString();
    }

    public static String makeRegex(String part, String whole, String blank) {
        String regex = Arrays.asList(part.replaceAll(" \\.\\.\\. ", " ").split(" ")).stream().map(v -> "(\\b(?i:" + v + ")\\b)").reduce((v1, v2) -> v1 + "|" + v2).orElse("");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(whole);
        StringBuffer sb = new StringBuffer(whole.length());
        while (matcher.find()) {
            String text = matcher.group(0);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(blank));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void getCode(double code) {
        double[] codes = {1001, 1002, 1003};
        String[] msgs = {"a", "b", "c"};
        ChoiceFormat choiceFormat = new ChoiceFormat(codes, msgs);
        MessageFormat messageFormat = new MessageFormat("code: {0}, msg: {1}.");
        messageFormat.setFormatByArgumentIndex(1, choiceFormat);
        Double[] args = {1001D, 1001D};
        System.out.println(messageFormat.format(args));
    }

    public static void test() {
        MessageFormat form = new MessageFormat("The disk \"{1}\" contains {0}.");
        double[] filelimits = {0, 1, 2};
        String[] filepart = {"no files", "one file", "{0,number} files"};
        ChoiceFormat fileform = new ChoiceFormat(filelimits, filepart);
        form.setFormatByArgumentIndex(0, fileform);

        int fileCount = 11;
        String diskName = "MyDisk";
        Object[] testArgs = {new Long(fileCount), diskName};

        System.out.println(form.format(testArgs));
    }

    public static void main(String[] args) {
//        Pattern p = Pattern.compile("(\\d{3,5})([a-z]{2})");
//        Matcher m = p.matcher("123bd-3434dc-34333dd-00");
//        Pattern p = Pattern.compile("(\\bagree\\b)|(\\bwith\\b)(?!.*\\bagree\\b)");
//        Matcher m = p.matcher("with a agree sb with a ");
        Pattern p = Pattern.compile("(\\b(?i:a)\\b)|(\\bnumber\\b)|(\\bof\\b)");
        String s = "a large number of people travel abroad every year .";
        Matcher m = p.matcher(s);
        StringBuffer sb = new StringBuffer(s.length());
        while (m.find()) {
//            System.out.println(m.group()); //我们只要捕获组1的数字即可。结果 3434
            System.out.println(m.group(0)); //我们只要捕获组1的数字即可。结果 3434
            System.out.println(m.group(1)); // 0组是整个表达式，看这里，并没有提炼出(?<!c)的字符 。结果 a3434bd
            System.out.println(m.group(2)); // 0组是整个表达式，看这里，并没有提炼出(?<!c)的字符 。结果 a3434bd
            String text = m.group(0);
            m.appendReplacement(sb, Matcher.quoteReplacement("___"));
            System.out.println(m.groupCount());
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
//        getCode(1001);
//        System.out.println(StringUtil.replaceSQL("", ""));
    }

    public static String getPy(String text, String separator, PinyinFormat format) throws PinyinException {
        return PinyinHelper.convertToPinyinString(text, separator, format);
    }
}
