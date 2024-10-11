package pirates.impl;

import pirates.JackSparrowHelper;
import pirates.Purchase;
import pirates.Purchases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JackSparrowHelperImpl implements JackSparrowHelper {

    @Override
    public Purchases helpJackSparrow(String pathToPrices, int numberOfGallons) {
        List<String[]> sources = loadSources(pathToPrices);
        Purchases purchases = new Purchases(numberOfGallons);
        Set<Purchase> purchaseSet = new HashSet<>();

        sources.sort(Comparator.comparingDouble(source -> Double.parseDouble(source[2])));
        for (String[] source : sources) {
            String sourceName = source[0];
            int size = Integer.parseInt(source[1]);
            double pricePerGallon = Double.parseDouble(source[2]);
            int minSize = Integer.parseInt(source[3]);
            int stepSize = Integer.parseInt(source[4]);
            int thisTotaltoBuy = Math.min(numberOfGallons, size);

            while (thisTotaltoBuy >= minSize && thisTotaltoBuy <= size && (thisTotaltoBuy - minSize) % stepSize == 0 && numberOfGallons > 0) {
                Purchase purchase = new Purchase();
                purchase.setSourceName(sourceName);
                purchase.setNumberOfGallons(thisTotaltoBuy);
                purchase.setPriceOfGallon(pricePerGallon);

                purchaseSet.add(purchase);
                numberOfGallons -= thisTotaltoBuy;
                thisTotaltoBuy -= stepSize;

            }
        }

        if (numberOfGallons > 0) {
            System.out.println("Не удалось закупить все необходимое количество рома.");
        }

        purchases.setPurchases(purchaseSet);
        return purchases;
    }

    private List<String[]> loadSources(String pathToPrices) {
        List<String[]> sources = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToPrices))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                sources.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sources;
    }

    public static void main(String[] args) {
        JackSparrowHelperImpl helper = new JackSparrowHelperImpl();

        Scanner scanner = new Scanner(System.in);
        int numberofGalon = scanner.nextInt();
        Purchases purchases = helper.helpJackSparrow("src/main/resources/sources.csv", numberofGalon);
        System.out.println("Средняя стоимость : " + purchases.calculateAveragePrice());
    }
}