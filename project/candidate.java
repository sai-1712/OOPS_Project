package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class candidate implements template{
    public String candidateId;
    public String candidateName;
    public String constituencyname;
    public String stateId;
    public String partyId;
    public String candidateCategory;
    public int age;
    public String gender;
    public String education;
    public int netWorth;
    public String criminalRecord;

    ArrayList<candidate> candidate_list = new ArrayList<>();

    public void add_attributes(String[] att)
    {
        int i = candidate_list.size();
        candidate c = candidate_list.get(i-1);
        c.candidateName = att[0];
        c.constituencyname = att[1];
        c.stateId = att[2];
        c.partyId = att[3];
        c.candidateCategory = att[4];
        c.age = Integer.parseInt(att[5]);
        c.gender = att[6];
        c.education = att[7];
        c.netWorth = Integer.parseInt(att[8]);
        c.criminalRecord = att[9];
    }

    public void add_candidate(candidate c)
    {
        candidate_list.add(c);
    }

    @Override
    public void select(String[] args) {

        // -a saikiran123 -select candidate -w candidateName -e "Sai Kiran" -sort age desc

        String[] merges = operations.merge_attributes(args);
        if(merges[0].contains("*"))
            merges[0] = "candidateName, constituencyName, stateName, partyName, candidateCategory, age, gender, education, networth, criminalRecord ";
        String query = "SELECT " + merges[0] +
                "FROM candidate " +
                "INNER JOIN party ON candidate.partyId = party.partyId " +
                "INNER JOIN constituency On candidate.constituencyId = constituency.constituencyId " +
                "INNER JOIN state ON candidate.stateId = state.stateId " + merges[1] + merges[2] + " LIMIT 10;";

        connector.query_execute(query);
    }

    @Override
    public void delete(String[] args) {


        // -a password -delete candidate candidate_name,constituency_name,state_id
        // -a saikiran123 -delete candidate candidateId
        // -a saikiran123 -delete candidate "Sai Kiran","Ongole","AP"

        String query;
        int flag = 0;
        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        if(values.length > 1)
            query = "DELETE FROM candidate WHERE candidateId  = (SELECT getCandidateId(?,?,?))  ;";
        else
            query = "DELETE FROM candidate WHERE candidateId  = ? ;";

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
        // party_id,candidate_category,age,gender,education,net_worth,criminal_record,candidate_name,constituency_name,state_id

        String query = "UPDATE candidate SET partyId = ?, candidateCategory = ?, age =  ?, gender = ?, education = ?, netWorth =  ?, criminalRecord = ? WHERE candidateId = ( SELECT getCandidateId(?,?,?)  ); ";
        connector.prepared_query(query, s);
    }

    @Override
    public void update(String[] args ) {

        // -a password -update candidate candidate_name,constituency_name,state_id -set x,x,x,x,x
        // -a password -update candidate candidate_name,constituency_name,state_id -att candidateCategory,age,education,netWorth,criminalRecord -set x,x,x,x,x
        // -a saikiran123 -update candidate "Chandra karthik","Ongole","AP" -att partyId,candidateCategory,age,gender,education,netWorth,criminalRecord -set "TRS","ST",19,"M","BTECH",100000,"YES"

        int flag = 0;
        String query ;
        String values;
        String[] att2;
        String merge = "";
        String intial = "";

        for (int i = 4; i < args.length; i++) {
            if(args[i].contains("-"))
                break;
            intial = intial.concat(args[i]);
        }

        if(args.length > 7){
            values = args[8].concat(","+intial);
            att2 = args[6].split(",");

            int l = att2.length;
            for (String s : att2) {
                if (s.equals(att2[l-1]))
                    merge = merge.concat(s + " = ? ");
                else
                merge = merge.concat(s + " = ?, ");
            }
        }
        else{
         values = args[6].concat(","+ intial);
         merge = " partyId = ?, candidateCategory = ?, age =  ?, gender = ?, education = ?, netWorth =  ?, criminalRecord = ?";
        }

        if (intial.contains(","))
            query = "UPDATE candidate SET " + merge + " WHERE candidateId = ( SELECT getCandidateId(?,?,?)  ); ";
        else
            query = "UPDATE candidate SET " + merge + " WHERE candidateId = ? ; ";

        flag = connector.prepared_query(query, values.split(","));
        if(flag == 0)
            System.out.println("Update Operation unsuccessful or Nothing is Updated.");
        else
            System.out.println("Updated successfully");

    }

    @Override
    public void insertCSV(String[] args) {

        // -a pass -insertcsv candidate candidate.csv

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
        // -a pass -insert table candidate_name,constituency_name,state_id,party_id,candidate_category,age,gender,education,net_worth,criminal_record
        // -a saikiran123 -insert candidate "Chandra karthik","Ongole","AP","BJP","GEN",30,"M","12",1000,"NO"

        int flag = 0;
        String merge = "";
        for (int i = 4; i < args.length; i++) {
            merge = merge.concat(args[i]);
        }
        String[] values = merge.split(",");

        int age =Integer.parseInt(values[5]);
        int networth=Integer.parseInt(values[8]);
        String gender=values[6];
        String Education=values[7];
        String criminalRecord=values[9];

        if(age>25&&(gender.equals("M")||gender.equals("F"))&&
                (Education.equals("0")||Education.equals("10")||Education.equals("12")||Education.equals("DIPLOMA")||Education.equals("DEGREE"))&&
                networth>0&&
                (criminalRecord.equals("YES")||criminalRecord.equals("NO"))){
            String query = "SELECT insertCandidate(?,?,?,?,?,?,?,?,?,?);";
            connector.prepared_query(query, values);
        }
        else{
            System.out.println("Field Constraints Voilation!\nEnter Valid constraints from-");
            System.out.println("Age > 25\nGender - M,F\nEducation – 0, 10, 12, DIPLOMA, DEGREE\nCriminalrecord – YES/NO\nNetworth > 0");
        }

        System.out.println("\nData inserted to database successfully");
    }

    public void insert(String[] args) {

        // -a pass -insert table candidate_name,constituency_name,state_id,party_id,candidate_category,age,gender,education,net_worth,criminal_record
        // -a saikiran123 -insert candidate "SaiKiran","Ongole","AP","BJP","GEN",25,"M","Inter",1000,"NO"

        String query = "SELECT insertCandidate(?,?,?,?,?,?,?,?,?,?);";
        connector.prepared_query(query, args);

    }

}