package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class constituency implements template{
    String constituencyId;
    String constituencyName;
    String stateId;
    String constituencyCategory;
    int registeredVoters;
    int voteCasted;

    ArrayList<constituency> constituency_list = new ArrayList<>();

    public void add_attributes(String[] att)
    {
        int i = constituency_list.size();
        constituency c = constituency_list.get(i-1);
        c.constituencyName = att[0];
        c.stateId = att[1];
        c.constituencyCategory = att[2];
        c.registeredVoters = Integer.parseInt(att[3]);
        c.voteCasted = Integer.parseInt(att[4]);
    }

    public void add_constituency(constituency c)
    {
        constituency_list.add(c);
    }

    @Override
    public void select(String[] args) {

        String[] merges = operations.merge_attributes(args);
        if(merges[0].contains("*"))
            merges[0] = "constituencyName, stateName, constituencyCategory, voterReg, votesCasted ";
        String query = "SELECT " + merges[0] +
                "FROM constituency " +
                "INNER JOIN state ON constituency.stateId = state.stateId " + merges[1] + merges[2] +" LIMIT 10;";

        connector.query_execute(query);
        
    }

    @Override
    public void delete(String[] args) {

        // -a password -delete -constituency constituency_name,state_id

        String query;
        int flag = 0;
        String[] values = args[4].split(",");
        if(values.length > 1)
            query = "DELETE FROM constituency WHERE constituencyId = (SELECT getConstituencyID(?,?));" ;
        else
            query = "DELETE FROM constituency WHERE constituencyId  = ? ;";

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

    public void Lineupdate(String[] s)
    {
        // constituencyCategory,voterReg,votesCasted,constituency_name,state_id

        String query = "UPDATE constituency SET constituencyCategory = ?, voterReg = ?, votesCasted = ? WHERE constituencyId = (SELECT getConstituencyID(?,?)); ";
        connector.prepared_query(query, s);
    }

    @Override
    public void update(String[] args) {

        // -a password -update constituency constituency_name,state_id -set x,x,x
        // -att constituencyCategory,voterReg,votesCasted -set x,x,x
        // -a saikiran123 -update constituency "Khar","AP" -att voterReg,votesCasted -set 10000000,90000

        String query ;
        String values;
        String[] att2;
        String merge = "";

        if(args.length > 7){
            values = args[8].concat(","+args[4]);
            att2 = args[6].split(",");

            for (String s : att2) {
                if (s.equals(att2[att2.length-1]))
                    merge = merge.concat(s + " = ? ");
                else
                merge = merge.concat(s + " = ?, ");
            }
        }
        else{
            values = args[6].concat(","+args[4]);
            merge = "constituencyCategory = ?, voterReg = ?, votesCasted = ? ";
        }

        if (args[4].contains(","))
            query = "UPDATE constituency SET " + merge +
                    "WHERE constituencyId = (SELECT getConstituencyID(?,?)); ";
        else
            query = "UPDATE constituency SET " + merge + " WHERE constituencyId = ? ; ";

        connector.prepared_query(query, values.split(","));
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

    @Override
    public void insertCommand(String[] args)
    {
        // -a pass -insert table constituency_name,state_id,constituency_category, voter_reg, votes_casted
        // -a saikiran123 -insert constituency "Khar","AP","GEN",120000,100000

        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        String query = "SELECT insertConstituency(?,?,?,?,?);";
        connector.prepared_query(query, values);
    }

    public void insert(String[] args) {
        // -a pass -insert table constituency_name,state_id,constituency_category, voter_reg, votes_casted
        // -a saikiran123 -insert constituency "Khar","AP","GEN",120000,100000

        String query = "SELECT insertConstituency(?,?,?,?,?);";
        connector.prepared_query(query, args);
    }

}