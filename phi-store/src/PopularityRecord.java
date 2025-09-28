import java.util.ArrayList;
import java.util.Collections;

public class PopularityRecord {

    private ArrayList<Item> items;


    private PopularityRecord(){


    }
    
    public static PopularityRecord parse(String filename) {
        PopularityRecord pr = new PopularityRecord();




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
}
