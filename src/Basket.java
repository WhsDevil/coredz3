import java.io.*;
import java.util.Arrays;

public class Basket implements Serializable{
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

    public void saveBin(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        }
    }

    public static Basket loadFromBinFile(File file) throws IOException,ClassNotFoundException{
        try (FileInputStream fos = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fos)) {
            return (Basket)ois.readObject();
        }
    }
}

