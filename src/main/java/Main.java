import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        File jsonBasket = new File("./basket.json");
        File csvLog = new File("./log.csv");
        Basket basket;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ClientLog log = new ClientLog();

        if (jsonBasket.exists()) {
            try (Reader reader = new FileReader(jsonBasket)) {
                basket = gson.fromJson(reader,Basket.class);
            }
        }
        else {
            basket = new Basket(new int[]{125, 333, 500}, new String[]{"Хлеб", "Молоко", "Печенье"});
            jsonBasket.createNewFile();
        }
        while (true) {
            System.out.println("Список возможных товаров для покупки");
            basket.printGoods();
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                basket.printCart();
                try (FileWriter file = new FileWriter(jsonBasket)) {
                    file.write(gson.toJson(basket));
                    file.flush();
                }
                log.exportAsCSV(csvLog);
                break;
            }
            int productNum = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()[0];
            int amount = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()[1];
            basket.addToCart(productNum, amount);
            log.log(productNum,amount);

        }
    }
}