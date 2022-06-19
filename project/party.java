package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class party implements template{
    String partyId;
    String partyName;
    String partySymbol;

    ArrayList<party> party_list = new ArrayList<>();

    public void add_attributes(String[] att)
    {
        int i = party_list.size();
        party c = party_list.get(i-1);
        c.partyId = att[0];
        c.partyName = att[1];
        c.partySymbol = att[2];
    }

    public void add_party(party c)
    {
        party_list.add(c);
    }

    @Override
    public void select(String[] args) {

        // -a saikiran123 -select party -w partyId -e "SBKP"

        String[] merges = operations.merge_attributes(args);
        String query = "SELECT " + merges[0] + " FROM party " +merges[1]+ merges[2] + " LIMIT 10;";

        connector.query_execute(query);
        
    }

    @Override
    public void delete(String[] args) {

        // -a saikiran123 -delete party "SBKP"

        int flag = 0;
        String query = "DELETE from party where partyId = ? ;" ;
        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        flag = connector.prepared_query(query, values);

        if(flag == 0)
            System.out.println("Delete Operation unsuccessful or Nothing is deleted.");
        else
            System.out.println("Deleted successfully");
        
    }

    @Override
    public void updatecsv(String[] args)
    {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(args[4]));
            String data;
            while ((data=bufferedReader.readLine())!=null){
                Lineupdate(data.split(","));
            }
            System.out.println("CSV data imported to database successfully");
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("From UpdateCSV: Problem while accesing file.");
        }
    }

    public void Lineupdate(String[] values)
    {
        String query = "UPDATE party SET partyName = ?, partySymbol = ? WHERE partyID = ?;";
        connector.prepared_query(query, values);
    }

    @Override
    public void update(String[] args) {

        // -a password -update party  partyId -set x,x
        // -a saikiran123 -update party "SBKP" -set "Sabka vikas party","Horse with king"

        String merge = "";
        for (int i = 6; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        merge = merge.concat(","+args[4]);

        String[] values = merge.split(",");

        String query = "UPDATE party SET partyName = ?, partySymbol = ? WHERE partyID = ?;";

        connector.prepared_query(query, values);
        System.out.println("\nUpdated successfully");
        
    }

    @Override
    public void insertCSV(String[] args) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(args[4]));
            String data;
            while ((data=bufferedReader.readLine())!=null){
                insert(data.split(","));
            }
            System.out.println("CSV data imported to database successfully");
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("From insertCSV: Problem while accesing file.");
        }
    }

    public void insert(String[] args) {

        // -a password -insert party party_id,party_name,party_symbol

        String query = "INSERT INTO party(partyId, partyName, partySymbol) VALUES (?,?,?);";
        connector.prepared_query(query, args);
    }

    @Override
    public void insertCommand(String[] args)
    {
        // -a password -insert party party_id,party_name,party_symbol
        // -a saikiran123 -insert party "SBKP","Sabka party","Wrist"

        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        String query = "INSERT INTO party(partyId, partyName, partySymbol) VALUES (?,?,?);";
        connector.prepared_query(query, values);
    }

}
