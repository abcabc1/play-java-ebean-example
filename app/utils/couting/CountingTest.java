package utils.couting;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CountingTest {

    public CountingTest() {

    }

    public Long stepCalc() {
        Long amount = 1500L;
        System.out.println(amount % 300);
        if (amount >= 300) {
            return amount - 30;
        } else if (amount >= 200) {
            return amount - 20;
        } else if (amount >= 100) {
            return amount - 10;
        } else {
            return amount;
        }
    }

    public void calculate() {
        double d = 1.2345;
        double d1 = (double) Math.round(d * 1000) / 1000;
        System.out.println(d1);
        BigDecimal b = new BigDecimal(d);
        double d2 = b.setScale(3, BigDecimal.ROUND_UP).doubleValue();
        System.out.println(d2);
        DecimalFormat df = new DecimalFormat("#.000");
        String d3 = df.format(d);
        System.out.println(d3);
        String d4 = String.format("%.3f", d);
        System.out.println(d4);
    }

    public static void main(String[] args) {
        CountingTest coutingTest = new CountingTest();
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i + "");
        }
        coutingTest.subLoop(list);
    }

    public void subLoop(List<String> datas) {
        int start = 0, end = 0, batchSize = 3;
        int loop = datas.size() / batchSize;
        for (int n = 0; n < loop; n++) {
            end = start + batchSize;
            List list = datas.subList(start, end);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(start + ":" + end + " " + list.get(i));
            }
            start = end;
        }
//        end = start + 1;
//        System.out.println(datas.subList(start,10));
        int rest = datas.size() - loop * batchSize;
        if (rest > 0) {
            end = start + rest;
            List restList = datas.subList(start, end);
            for (int i = 0; i < restList.size(); i++) {
                System.out.println(start + ":" + end + " " + restList.get(i));
            }
        }
    }
}
