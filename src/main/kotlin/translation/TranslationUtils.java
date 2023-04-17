package translation;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class TranslationUtils {
    /**
     * 给出一个中文翻译的文件路径
     */
    private static final String zh_rcn_path = "D:/Android/pro/S508_aircondition/BwAirCondition/src/test/java/translation/translation.xls";
    /**
     * 自动生成的翻译文件的名字
     */
    private static final String CREATE_STRING_NAME = "strings_bw.xml";
    /**
     * 表格文件目录
     */
    private static final String xlxs_path = "D:/Android/pro/S508_aircondition/BwAirCondition/src/test/java/translation/test.xlsx";
    /**
     * 记录中文翻译每个item中的属性
     */
    private static Map<String, List<Set<Map.Entry<String, String>>>> zhItemMap = new HashMap<>();

    public static void main(String[] args) {
        loadZhFile(zh_rcn_path);
        createMultipleTranslations(xlxs_path);
    }

    private static Boolean val;

    public static void test(Boolean v) {
        System.out.println(val == v);
        val =v;
    }

    /**
     * 加载中文的翻译文件
     *
     * @param filePath
     */
    public static void loadZhFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            String data = read(filePath);
            XmlPullParser xmlPullParser = new XmlPullParser(data);
            List<XmlPullParser.Node> nodes = xmlPullParser.xmlParsing();
            for (XmlPullParser.Node node : nodes) {
                if (node.data != null && node.keyValues != null) {
                    String d = node.data.replaceAll("&#160;", "");
                    d = d.replaceAll("&amp;", "");
                    d = d.replaceAll("&#12288;", "");
                    if (!"resources".equals(node.tagName)) {
                        List<Set<Map.Entry<String, String>>> list = zhItemMap.get(d);
                        if (list == null) {
                            list = new ArrayList<>();
                            zhItemMap.put(d, list);
                        }
                        list.add(node.keyValues.entrySet());
                    }
                }
            }
        } else {
            System.out.println("文件不存在 " + filePath);
        }
    }

    /**
     * 生成多个翻译的文件
     *
     * @param filePath
     */
    private static List<Language> createMultipleTranslations(String filePath) {
        List<Language> languages = new ArrayList<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            int size = wb.getNumberOfSheets();
            if (size > 0) {
                XSSFSheet sheet = wb.getSheetAt(0);
                List<Head> heads = readExcel(sheet, 0);
                int zhIndex = 0;//中文显示的列数
                List<Map<String, Boolean>> mRepeatMap = new ArrayList<>();
                for (int index = 0; index < heads.size(); index++) {
                    Head head = heads.get(index);
                    Language language = new Language(head.name);
                    languages.add(language);
                    if (head.name != null && head.name.contains("values-zh")) {
                        zhIndex = index;
                    }
                    mRepeatMap.add(new HashMap<>());
                }
                Map<String, Boolean> strMap = new HashMap<>();
                int rows = sheet.getPhysicalNumberOfRows();
                for (int i = 1; i < rows; i++) {
                    List<String> values = readExcel(sheet, i, heads);
                    if (heads.size() == values.size()) {
                        for (int j = 0; j < heads.size(); j++) {
                            Language language = languages.get(j);
                            String value = values.get(j);
                            //这里进行字符转义
                            value = value.replaceAll("&", "&#38;");
                            value = value.replaceAll("\"", "&#34;");
                            value = value.replaceAll("'", "&quot;");
                            value = value.replaceAll("<", "&#60;");
                            value = value.replaceAll(">", "&#62;");
                            value = value.replaceAll("\\.\\.\\.", "&#8230;");
                            Language.Item item = new Language.Item(value);
                            language.addEntry(item);
                            strMap.put(values.get(j), true);
                        }
                    } else {
                        System.out.println("数据不对 行数: " + i);
                    }
                }
                for (Map.Entry<String, List<Set<Map.Entry<String, String>>>> entrys : zhItemMap.entrySet()) {
                    if (!strMap.containsKey(entrys.getKey())) {
                        System.out.println("新增的中文翻译:" + entrys.getKey());
                    }
                }
                //中文的翻译
                Language languageZh = languages.get(zhIndex);
                List<Language.Item> items = new ArrayList<>(languageZh.items);
                for (int i = 0; i < items.size(); i++) {
                    Language.Item item = languageZh.items.get(i);
                    List<Set<Map.Entry<String, String>>> entries = zhItemMap.get(item.data);
                    if (entries != null) {
                        for (int j = 0; j < languages.size(); j++) {
                            Language language = languages.get(j);
                            Map<String, Boolean> map = mRepeatMap.get(j);
                            Language.Item childItem = language.items.get(i);
                            for (Set<Map.Entry<String, String>> entry : entries) {
                                for (Map.Entry<String, String> child : entry) {
                                    String childKey = child.getValue();
                                    if (!map.containsKey(childKey)) {
                                        map.put(childKey, true);
                                        childItem.addEntry(new Language.Item.KeyValue(child.getKey(), child.getValue()));
                                    }
                                }
                                break;//这里只取第一个数据
                            }
                            if (entries.size() > 1) {
                                for (int k = 1; k < entries.size(); k++) {
                                    Set<Map.Entry<String, String>> entry = entries.get(k);
                                    Language.Item newItem = new Language.Item(childItem.data);
                                    for (Map.Entry<String, String> child : entry) {
                                        String childKey = child.getValue();
                                        if (!map.containsKey(childKey)) {
                                            map.put(childKey, true);
                                            newItem.addEntry(new Language.Item.KeyValue(child.getKey(), child.getValue()));
                                        }
                                    }
                                    language.items.add(newItem);
                                }
                            }
                        }
                    } else {
                        System.out.println(item.data + " 未从源文件找到此翻译");
                    }
                }
                System.out.println("正在生成翻译");
                for (int j = 0; j < languages.size(); j++) {
                    Language language = languages.get(j);
                    language.save();
                }
                System.out.println("翻译生成完成");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return languages;
    }

    public static List<Head> readExcel(XSSFSheet sheet, int row) {
        List<Head> heads = new ArrayList<>();
        int size = sheet.getPhysicalNumberOfRows();
        if (row >= 0 && row < size) {
            XSSFRow xssfRow = sheet.getRow(row);
            //当读取行为空时
            if (xssfRow != null) {
                for (int i = xssfRow.getFirstCellNum(); i <= xssfRow.getLastCellNum(); i++) {
                    if (i < 0) {
                        continue;
                    }
                    XSSFCell cell = xssfRow.getCell(i);
                    if (cell != null) {
                        String val = cell.getStringCellValue();
                        heads.add(new Head(val, i));
                    }
                }
            }
        }
        return heads;
    }

    public static List<String> readExcel(XSSFSheet sheet, int row, List<Head> heads) {
        List<String> values = new ArrayList<>();
        int size = sheet.getPhysicalNumberOfRows();
        if (row >= 0 && row < size) {
            XSSFRow xssfRow = sheet.getRow(row);
            //当读取行为空时
            if (xssfRow != null && heads != null) {
                for (Head head : heads) {
                    int index = head.index;
                    if (index >= xssfRow.getFirstCellNum() && index <= xssfRow.getLastCellNum()) {
                        XSSFCell cell = xssfRow.getCell(index);
                        if (cell != null) {
                            String val = cell.getStringCellValue();
                            values.add(val);
                        } else {
                            values.add("");
                        }
                    } else {
                        values.add("");
                    }
                }
            }
        }
        return values;
    }

    private static String read(String path) {
        InputStream inputStream = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                BufferedReader bufReader = new BufferedReader(inputReader);
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufReader.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 写入文件
     *
     * @param file
     * @param data
     */
    public static void write(File file, String data) {
        if (!file.exists()) {
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintStream fileWritter = null;
        try {
            fileWritter = new PrintStream(file);
            fileWritter.println(data);
            fileWritter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileWritter != null) {
                fileWritter.close();
            }
        }
    }

    public static class Language {
        String name;
        List<Item> items = new LinkedList<>();

        public Language(String name) {
            this.name = name;
        }

        public void addEntry(Item item) {
            items.add(item);
        }

        public void save() {
            int index = zh_rcn_path.lastIndexOf("/");
            if (index >= 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<resources>\n");
                for (Item item : items) {
                    if (item.entrys.size() > 0 && item.data != null && !item.data.equals("")) {
                        sb.append(" <string");
                        for (int i = 0; i < item.entrys.size(); i++) {
                            Item.KeyValue val = item.entrys.get(i);
                            sb.append(String.format(" %s=\"%s\"", val.key, val.value));
                        }
                        sb.append(">");
                        sb.append(item.data);
                        sb.append("</string>\n");
                    }
                }
                sb.append("</resources>\n");
                String path = zh_rcn_path.substring(0, index);
                File file = new File(path + "/" + name + "/" + CREATE_STRING_NAME);
                write(file, sb.toString());
            }
        }

        public static class Item {
            String data;
            List<KeyValue> entrys;

            public Item(String data) {
                this.data = data;
                entrys = new ArrayList<>();
            }

            public void addEntry(KeyValue value) {
                if (!"translatable".equals(value.key)) {
                    entrys.add(value);
                }
            }

            public static class KeyValue {
                String key;
                String value;

                public KeyValue(String key, String value) {
                    this.key = key;
                    this.value = value;
                }
            }
        }
    }

    public static class Head {
        String name;
        int index;

        public Head(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }
}
