package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class state implements template{
    String stateId;
    String stateName;

    ArrayList<state> state_list = new ArrayList<>();

    public void add_attributes(String[] att)
    {
        int i = state_list.size();
        state c = state_list.get(i-1);
        c.stateId = att[0];
        c.stateName = att[1];
    }

    public void add_state(state c)
    {
        state_list.add(c);
    }

    @Override
    public void select(String[] args) {

        // -a saikiran123 -select state -w stateId -e "NS"
        // -a saikiran123 -select state

        String[] merges = operations.merge_attributes(args);
        if (merges[0].contains("*"))
            merges[0] = "stateName, NumberOfConstituency, TotalVoterReg, TotalVotescasted ";
        String query = "SELECT " + merges[0] +
                " FROM state , (SELECT stateId, COUNT(constituencyName) as NumberOfConstituency, SUM(voterReg) as TotalVoterReg, SUM(votescasted) as TotalVotescasted FROM constituency " +
                " GROUP BY stateId) constituency_alias " +
                " WHERE state.stateId = constituency_alias.stateId " + merges[1].replace("where", "AND ") + merges[2] + " ;";

        connector.query_execute(query);
        
    }

    @Override
    public void delete(String[] args) {

        // -a saikiran123 -delete state "NS"

        int flag = 0;
        String query = "DELETE from state where stateId = ?";
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
    public void update(String[] args) {

        // -a password -update state stateId -set x
        // -a saikiran123 -update state "NS" -set "Our New state"

        String merge = "";
        for (int i = 6; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        merge = merge.concat(","+args[4]);

        String[] values = merge.split(",");

        String query = "UPDATE state SET stateName = ? WHERE stateId = ? ;";

        connector.prepared_query(query, values);
        System.out.println("Updated successfully");
        
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
        String query = "UPDATE state SET stateName = ? WHERE stateId = ? ;";
        connector.prepared_query(query, values);
    }

    @Override
    public void insertCSV(String[] args) {
        // -a saikiran123 -insertcsv state src\project\state_data.csv

        try {
            FileReader f = new FileReader(args[4]);
            BufferedReader bufferedReader = new BufferedReader(f);
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

        // -a password -insert state state_id, state_name

        String query = "INSERT INTO state(stateId, stateName) values(?,?);";
        connector.prepared_query(query, args);
    }

    @Override
    public void insertCommand(String[] args)
    {
        // -a password -insert state state_id, state_name
        // -a saikiran123 -insert state "NS","New state"

        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        String query = "INSERT INTO state(stateId, stateName) values(?,?);";
        connector.prepared_query(query, values);
    }

}