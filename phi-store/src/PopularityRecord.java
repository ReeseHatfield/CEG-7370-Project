import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.*;

public class PopularityRecord {

    private ArrayList<Item> items;


    private PopularityRecord(){
        this.items = new ArrayList<>();
    }
    
    public static PopularityRecord parse(String filename) {
        PopularityRecord pr = new PopularityRecord();

        File f = new File(filename);

        try (Scanner inFile = new Scanner(f)) {
            while(inFile.hasNextLine()){
                String line = inFile.nextLine();
                String[] parts = line.split("=");

                String name = parts[0];
                Integer amountSold = Integer.parseInt(parts[1]);

                Item i = new Item(name, amountSold);

                pr.items.add(i);

            }
        } catch (NumberFormatException | FileNotFoundException e) {
            e.printStackTrace();
        }
            
        return pr;
        
    }
    

    public Item getMostPopular(){
        Collections.sort(this.items);
        return this.items.get(0);
    }

    public Item getLeastPopular(){
        Collections.sort(this.items);
        return this.items.get(this.items.size() - 1);

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        Collections.sort(this.items);

        // rusty for each
        this.items.forEach((item) -> {
            sb.append(item);
            sb.append("\n");
        });


        return sb.toString();
    }


    public String toPrompt(){
        StringBuilder sb = new StringBuilder();


        sb.append("You are a product recommender agent.");
        sb.append("You are good at your job.");
        sb.append("Next, you will given a list of products and how many product were sold.");
        sb.append("For example: Eggs=23 means twenty three eggs were sold.");
        sb.append(this.toString());
        sb.append("Using that data, you will recommend popular products when people ask for it.");
        sb.append("For example, you might respond with: 'People from this store love to buy potatoes!'");
        sb.append("Make sure any product you recommend was in the provided sample data");
        sb.append("Do not ask any follow up questions.");


        return sb.toString();
    }
}


class Item implements Comparable<Item>{
    public String name = "";
    public Integer amountSold = 0;

    public Item(String name){
        this.name = name;
        this.amountSold = 0;
    }

    public Item(String name, Integer amountSold){
        this.name = name;
        this.amountSold = amountSold;
    }

    @Override
    public int compareTo(Item other) {
        return this.amountSold.compareTo(other.amountSold);
    }


    @Override
    public String toString(){
        return this.name + "=" + this.amountSold;
    }
}
