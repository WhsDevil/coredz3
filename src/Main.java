import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException,ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        File textBasket = new File("./basket.bin");
        Basket basket;

        if (textBasket.exists()) basket = Basket.loadFromBinFile(textBasket);
        else {
            basket = new Basket(new int[]{125, 333, 500}, new String[]{"Хлеб", "Молоко", "Печенье"});
            textBasket.createNewFile();
        }
        while (true) {
            System.out.println("Список возможных товаров для покупки");
            basket.printGoods();
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                basket.printCart();
                break;
            }
            basket.addToCart(Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()[0], Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray()[1]);
            basket.saveBin(textBasket);
        }
    }
}