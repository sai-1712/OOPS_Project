package project;
import java.util.Arrays;

// Interface that contain CRUD operations methods
interface template{
    void select(String[] args /*String attributes */);
    void delete(String[] args /*String attributes */);
    void update(String[] args /*String valuesAndAttributes*/);
    void insertCSV(String[] args /*String valuesAndAttributes*/);
    void insertCommand(String[] args /*String valuesAndAttributes*/);
    void updatecsv(String[] args /*String valuesAndAttributes*/);

    // Printing pattern of Search Features commands
    private static void searchFeatures(){
        System.out.println("Enter valid search commmands from--\n");
        System.out.println("Type '-u -search candidate -cmplt 'candidateName' pageLimit ' to search candidates with full names");
        System.out.println("Type '-u -search candidate -prtl 'candidateName' pageLimit ' to search candidates with partial names");
        System.out.println("Type '-u -search candidate -constit 'constituencyName' 'stateId' pageLimit' to search all candidates in a Constituency");
        System.out.println("Type '-u -search constituency -cmplt 'constituencyName' pageLimit' to search a Constituency with full name");
        System.out.println("Type '-u -search constituency -prtl 'constituencyName' pageLimit' to search a Constituency with partial name");
    }

    // Search features for
    static void search(String[] args /*String valuesAndAttributes*/){
        String Query2="CALL getAllCandidateDetails(";
        String Query1="CALL getCandidateDetails(";
        String Query3="CALL getAllCandidateOfAConstituencyDetails(";
        String Query5="CALL getAllConstituencyDetails(";
        String Query4="CALL getConstituencyDetails(";
        int limit=1000;
        int p=2;
        if(args[0].equals("-a"))
            p=3;
        if(args[p].equals("candidate")){
            switch (args[p+1]) {
                case "-cmplt":  if(args.length==p+3){
                    Query1+="'"+args[p+2]+"'"+", "+limit+");";
                    connector.query_call(Query1);
                }
                else
                if(args.length==p+4){
                    Query1+="'"+args[p+2]+"'"+", "+args[p+3]+");";
                    connector.query_call(Query1);
                }
                else{System.out.println("Enter valid cand cmplt commmands from--\n");
                    searchFeatures();}
                    break;
                case "-prtl":   if(args.length==p+3){
                    Query2+="'"+args[p+2]+"'"+", "+limit+");";
                    connector.query_call(Query2);
                }
                else
                if(args.length==p+4){
                    Query2+="'"+args[p+2]+"'"+", "+args[p+3]+");";
                    connector.query_call(Query2);
                }
                else{System.out.println("Enter valid cand prtl commmands from--\n");
                    searchFeatures();}
                    break;
                case "-constit":    if(args.length==p+4){
                    Query3+="'"+args[p+2]+"'"+", "+"'"+args[p+3]+"'"+", "+limit+");";
                    connector.query_call(Query3);
                }
                else
                if(args.length==p+5){
                    Query3+="'"+args[p+2]+"'"+", "+"'"+args[p+3]+"'"+", "+args[p+4]+");";
                    connector.query_call(Query3);
                }
                else{System.out.println("Enter valid cand consit commmands from--\n");
                    searchFeatures();}
                    break;
                default:    System.out.println("Enter valid cand def commmands from--\n");
                    searchFeatures();
                    break;
            }
        }
        else
        if(args[p].equals("constituency")){
            switch (args[p+1]) {
                case "-cmplt":  if(args.length==p+3){
                    Query4+="'"+args[p+2]+"'"+", "+limit+");";
                    connector.query_call(Query4);
                }
                else
                if(args.length==p+4){
                    Query4+="'"+args[p+2]+"'"+", "+args[p+3]+");";
                    connector.query_call(Query4);
                }
                else
                    searchFeatures();
                    break;
                case "-prtl":   if(args.length==p+3){
                    Query5+="'"+args[p+2]+"'"+", "+limit+");";
                    connector.query_call(Query5);
                }
                else
                if(args.length==p+4){
                    Query5+="'"+args[p+2]+"'"+", "+args[p+3]+");";
                    connector.query_call(Query5);
                }
                else
                    searchFeatures();
                    break;
                default:    searchFeatures();
                    break;
            }

        }
        else
            searchFeatures();
    }
}

public class table{

    public static String[] names = {"candidate","winnerCandidate","constituency","state","party"};

    public static template call_table(String[] args) {

        String tname = args[2];
        if(args[0].equals("-a"))
            tname = args[3];

        template tble;
        switch(tname){
            case "candidate" -> {
                tble =new candidate();
                return tble;
            }
            case "constituency" -> {
                tble =new constituency();
                return tble;
            }
            case "winnerCandidate" -> {
                tble =new winnerCandidate();
                return tble;
            }
            case "state" -> {
                tble =new state();
                return tble;
            }
            case "party" -> {
                tble =new party();
                return tble;
            }
            default -> {
                System.err.println("Incorrect table name. Enter valid name from below tables.\n");
                System.err.println("Tables : " + Arrays.toString(names));
                return null;
            }
        }
    }

}