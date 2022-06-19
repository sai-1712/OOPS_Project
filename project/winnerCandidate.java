package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class winnerCandidate implements template{
    String candidateId;
    String constituencyId;
    String stateId;

    ArrayList<winnerCandidate> winnerCandidate_list = new ArrayList<>();

    public void add_attributes(String[] att)
    {
        int i = winnerCandidate_list.size();
        winnerCandidate c = winnerCandidate_list.get(i-1);
        c.candidateId = att[0];
        c.constituencyId = att[1];
        c.stateId = att[2];
    }

    public void add_winnercandidate(winnerCandidate c)
    {
        winnerCandidate_list.add(c);
    }

    @Override
    public void select(String[] args) {

        // -a saikiran123 -select winnerCandidate -w candidateId -e "AP-16-3"

        String[] merges = operations.merge_attributes(args);
        if(merges[0].contains("*"))
            merges[0] = "candidateName, constituencyName, stateName, partyName, candidateCategory, age, gender, education, networth, criminalRecord ";

        String query = "SELECT  " + merges[0] +
                " FROM winnercandidate , (SELECT candidateid,candidateName, constituencyName, stateName, partyName, candidateCategory, age, gender, education, networth, criminalRecord FROM candidate" +
                " INNER JOIN party ON candidate.partyId = party.partyId" +
                " INNER JOIN constituency On candidate.constituencyId = constituency.constituencyId INNER JOIN state ON candidate.stateId = state.stateId) candidate_alias " +
                " WHERE winnercandidate.candidateId = candidate_alias.candidateId " +merges[1].replace("where ", "AND ")+ merges[2] + " LIMIT 10;";

        connector.query_execute(query);
        
    }
    @Override
    public void delete(String[] args) {

        // -a password -delete winnerCandidate candidate_name,constituency_name,state_id

        String query;
        int flag = 0;
        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        if(values.length > 1)
            query = "DELETE FROM winnercandidate WHERE candidateId = " +
                    "(SELECT getCandidateId(?,?,?) );" ;
        else
            query = "DELETE FROM winnercandidate WHERE candidateId = ? ;" ;

        flag = connector.prepared_query(query, values);

        if(flag == 0)
            System.out.println("Delete Operation unsuccessful or Nothing is deleted.");
        else
            System.out.println("Deleted successfully");
        
    }

    @Override
    public void update(String[] args) {

        System.out.println("\nUpdate is not possible in WinnerCandidate table, " +
                "update in candidate table that will automatically reflect in this table.");
        
    }

    @Override
    public void updatecsv(String[] args)
    {
        System.out.println("\nUpdate is not possible in WinnerCandidate table, " +
                "update in candidate table that will automatically reflect in this table.");
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
        // -a saikiran123 -insert winnerCandidate candidateName,constituencyName,stateId
        // -a saikiran123 -insert winnerCandidate "Sai Kiran","Ongole","AP"

        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] temp = merge.split(",");

        ArrayList<String> am = new ArrayList<>(Arrays.asList(temp));
        am.add(temp[1]);
        am.add(temp[2]);
        am.add(temp[2]);

        String[] values = am.toArray(new String[6]);

        String query = "INSERT INTO winnercandidate VALUES((SELECT getCandidateId(?,?,?))," +
                " (SELECT getConstituencyID(?,?)), ?);";

        connector.prepared_query(query, values);
    }

    public void insert(String[] args) {

        // candidateName,constituencyName,stateId
        // "Sai Kiran","Ongole","AP"


        String query = "INSERT INTO winnercandidate VALUES((SELECT getCandidateId(?,?,?))," +
                " (SELECT getConstituencyID(\"" + args[1] +"\",\"" + args[2]+ "\")), \""+ args[2]+ "\");";

        connector.prepared_query(query, args);
    }

}
