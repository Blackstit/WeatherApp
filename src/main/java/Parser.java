import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jsoup.Jsoup.parse;

public class Parser {
    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        Document page = parse(new URL(url), 300);
        return page;
    }

    // 25.12 Среда погода сегодня
    // 25.15
    // \d{2}\.\d{2}
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString (String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find() ) {
            return matcher.group();
        }
        throw  new Exception("Can't extract date from string!");
    }

    private static int printPartValues(Elements values, int index){
        int iterationCount = 4;
        if (index == 0) {
            Element valueLine = values.get(3);
            boolean isMorning = valueLine.text().contains("Утро");
            if (isMorning) {
                iterationCount = 3;
            }
        }
            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index+i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "   ");
                }
                System.out.println("");
            }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        // css query language
        Element tableWth = getPage().select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");

        int index = 0;
        for(Element name : names){
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "    Явления     Температура     Давл    Влажность   Ветер");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }


    }
}
