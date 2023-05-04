## Описание программы
В начале программы считывается конфигурация .xml в мапу со значениями параметров, а так же заполняются названия файлов
``` java
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
```
Далее, в зависимости от параметров конфигурации происходит десериализация класса
``` java
if (basketFile.exists() && shop.get("loadCheck").equals("true")) {
    try (Reader reader = new FileReader(basketFile)) {
        basket = (shop.get("loadFormat").equals("json")) ? gson.fromJson(reader,Basket.class) : Basket.loadFromTxtFile(basketFile);
    }
}
else {
    basket = new Basket(new int[]{125, 333, 500}, new String[]{"Хлеб", "Молоко", "Печенье"});
    basketFile.createNewFile();
}
```
В цикле while происходит общение с пользователем, сериализация и сохранение логов
``` java
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
```
Класс Basket не был изменен, а ClientLog добавлен
``` java
public class ClientLog {
    private ArrayList<int[]> choice = new ArrayList<>();

    public void log(int productNum, int amount) {
        this.choice.add(new int[]{productNum, amount});
    }

    public void exportAsCSV(File file) throws IOException {
        if (file.exists()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(file,true))) {
                for (int[] currentChoice : choice) {
                    writer.writeNext(Arrays.stream(currentChoice)
                            .mapToObj(String::valueOf)
                            .toArray(String[]::new));
                }
            }
        }
        else {
            file.createNewFile();
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                writer.writeNext(new String[]{"productNum", "amount"});
                for (int[] currentChoice : choice) {
                    writer.writeNext(Arrays.stream(currentChoice)
                            .mapToObj(String::valueOf)
                            .toArray(String[]::new));
                }
            }
        }
    }
}
```