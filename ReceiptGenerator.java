import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGenerator {

    // Tax rates and exempt categories
    private static final Map<String, Double> TAX_RATES = new HashMap<>();
    private static final Map<String, List<String>> EXEMPT_CATEGORIES = new HashMap<>();

    static {
        TAX_RATES.put("CA", 0.0975);
        TAX_RATES.put("NY", 0.08875);

        EXEMPT_CATEGORIES.put("CA", List.of("food"));
        EXEMPT_CATEGORIES.put("NY", List.of("food", "clothing"));
    }

    public static void main(String[] args) {
        ReceiptGeneratorTest receiptGeneratorTest = new ReceiptGeneratorTest();
        receiptGeneratorTest.testIsExempt();
        receiptGeneratorTest.testPrintReceipt();
        receiptGeneratorTest.testCalculateTax();
    }

    public static double calculateTax(ShoppingItem item, String location) {
        if (isExempt(item.getCategory(), location)) {
            return 0;
        }
        double taxRate = TAX_RATES.getOrDefault(location, 0.0);
        return roundUpToNearest(item.getPrice() * item.getQuantity() * taxRate, 2);
    }

    public static boolean isExempt(String category, String location) {
        List<String> exemptList = EXEMPT_CATEGORIES.getOrDefault(location, new ArrayList<>());
        return exemptList.contains(category);
    }

    public static double roundUpToNearest(double value, int count) {
        // 计算步长，即 10 的 -count 次幂
        double step = Math.pow(10, -count);

        BigDecimal bdValue = new BigDecimal(Double.toString(value / step));
        bdValue = bdValue.setScale(0, RoundingMode.HALF_UP); // 四舍五入到整数

        double roundedValue = bdValue.doubleValue() * step;

        return roundedValue;
    }

    // ShoppingItem class to represent each item in the cart
    static class ShoppingItem {
        private String name;
        private double price;
        private int quantity;
        private String category;

        public ShoppingItem(String name, double price, int quantity, String category) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.category = category;
        }

        // Getters
        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getCategory() {
            return category;
        }
    }
}