import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        Scanner scanner = new Scanner(System.in);
        File shopXml = new File("./shop.xml");
        Basket basket;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ClientLog log = new ClientLog();
        HashMap<String, String> shop = new HashMap<>();

        // Чтение shop.xml
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(shopXml);
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element preference = (Element) node_;
                switch (node_.getNodeName()) {
                    case "load" -> {
                        shop.put("loadCheck", preference.getElementsByTagName("enabled").item(0).getTextContent());
                        shop.put("loadFileName", preference.getElementsByTagName("fileName").item(0).getTextContent());
                        shop.put("loadFormat", preference.getElementsByTagName("format").item(0).getTextContent());
                    }
                    case "save" -> {
                        shop.put("saveCheck", preference.getElementsByTagName("enabled").item(0).getTextContent());
                        shop.put("saveFileName", preference.getElementsByTagName("fileName").item(0).getTextContent());
                        shop.put("saveFormat", preference.getElementsByTagName("format").item(0).getTextContent());
                    }
                    case "log" -> {
                        shop.put("logCheck", preference.getElementsByTagName("enabled").item(0).getTextContent());
                        shop.put("logFileName", preference.getElementsByTagName("fileName").item(0).getTextContent());
                    }
                }
            }
        }
        File basketFile = new File("./" + shop.get("loadFileName"));
        File csvLog = new File("./" + shop.get("logFileName"));
        // <--

        if (basketFile.exists() && shop.get("loadCheck").equals("true")) {
            try (Reader reader = new FileReader(basketFile)) {
                basket = (shop.get("loadFormat").equals("json")) ? gson.fromJson(reader,Basket.class) : Basket.loadFromTxtFile(basketFile);
            }
        }
        else {
            basket = new Basket(new int[]{125, 333, 500}, new String[]{"Хлеб", "Молоко", "Печенье"});
            basketFile.createNewFile();
        }
        while (true) {
            System.out.println("Список возможных товаров для покупки");
            basket.printGoods();
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                basket.printCart();
                if (shop.get("saveCheck").equals("true") && shop.get("saveFormat").equals("json")) {
                    try (FileWriter file = new FileWriter(basketFile)) { // Серилиазация класса json
                        file.write(gson.toJson(basket));
                        file.flush();
                    }
                }
                if (shop.get("logCheck").equals("true")) log.exportAsCSV(csvLog); // Вывод логов
                break;
            }
            int productNum = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()[0];
            int amount = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()[1];
            if (shop.get("saveCheck").equals("true") && shop.get("saveFormat").equals("txt")) basket.saveTxt(basketFile); // Сохранение в txt
            basket.addToCart(productNum, amount);
            log.log(productNum,amount); // Сохранение логов
        }
    }
}