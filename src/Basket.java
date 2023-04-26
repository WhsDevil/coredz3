import java.io.*;
import java.util.Arrays;

public class Basket {
    private int[] basket, prices;
    private String[] goods;

    public Basket(int[] prices, String[] goods){
        this.goods = goods;
        this.prices = prices;
        Arrays.fill(this.basket = new int[goods.length], 0);
    }

    public Basket(int[] basket, int[] prices, String[] goods) {
        this.goods = goods;
        this.prices = prices;
        this.basket = basket;
    }

    public void addToCart(int productNum, int amount) {
        basket[productNum - 1] += amount;
    }

    public void printCart() {
        System.out.println("Ваша Корзина: ");
        int sum = 0;
        for (int i = 0; i < basket.length; i++) {
            if (basket[i] != 0) {
                System.out.println(goods[i] + " " + basket[i] + " шт " + prices[i] + " руб/шт " + (basket[i] * prices[i]) + " в сумме");
                sum += basket[i] * prices[i];
            }
        }
        System.out.println("Итого: " + sum + " руб");
    }

    public void printGoods() {
        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < goods.length; i++) {
            System.out.println((i + 1) + ". " + goods[i] + " " + prices[i] + " руб/шт");
        }
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile);) {
            for (int element : basket) out.print(element + " ");
            out.print("\n");
            for (int element : prices) out.print(element + " ");
            out.print("\n");
            for (String element : goods) out.print(element + " ");
            out.print("\n");
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException{
        try (FileReader input = new FileReader(textFile)) {
            StringBuilder result = new StringBuilder();
            Character c;
            while ((c = (char)input.read()) != '\n') result.append(c);
            int[] basketLoad = Arrays.stream(result.toString().split(" ")).mapToInt(Integer::parseInt).toArray();
            result.setLength(0);
            while ((c = (char)input.read()) != '\n') result.append(c);
            int[] pricesLoad = Arrays.stream(result.toString().split(" ")).mapToInt(Integer::parseInt).toArray();
            result.setLength(0);
            while ((c = (char)input.read()) != '\n') result.append(c);
            String[] goodsLoad = result.toString().split(" ");
            result.setLength(0);
            return new Basket(basketLoad,pricesLoad,goodsLoad);
        }
    }
}

