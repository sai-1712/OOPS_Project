package project;

public class Main {

    public static void welcome_message() {
        String w = "WELCOME TO ELECTION COMMISSION RECORDS";
        String s = "------------------------------------------";
        System.out.format("\n\n%82s", s);
        System.out.format("\n%41s %s %s", "|", w, "|");
        System.out.format("\n%82s\n\n", s);
        printHelp();
    }

    public static void printHelp() {
        System.out.println("Help for commands:");
        System.out.println("Type -u before sql commands to perform user operations(DQL) ");
        System.out.println("Type -a <password> before sql commands to perform admin operations(DML) ");
        System.out.println("-h (or any) for help menu");
        System.out.println("-c to clear the current input statement.");
        System.out.println("-e to exit");
        operations.features();
    }

    public static void examples()
    {
        System.out.println("CANDIDATE TABLE EXAMPLE QUERIES : \n");
        System.out.println("Attributes: candidateName, constituencyName, stateName, partyName, candidateCategory, age, gender, education, networth, criminalRecord ");
        System.out.println("-select candidate candidateName -w age -gte 30 -sort age asc");
        System.out.println("-insert candidate \"Sai Kiran\",\"Ongole\",\"AP\",\"BJP\",\"GEN\",25,\"M\",\"Inter\",1000,\"NO\"");
        System.out.println("-update candidate \"SaiKiran\",\"Ongole\",\"AP\" -set \"ST\",19,\"BTECH\",100000,\"YES\"");
        System.out.println("-delete candidate \"Sai Kiran\",\"Ongole\",\"AP\"");

    }

    public static void main(String[] args) {

        welcome_message();

        if (args[args.length-1].contains("-c"))
            System.exit(1);

        try {
            switch (args[0]) {
                case "-h" -> printHelp();
                case "-e" -> System.exit(1);
                case "-u" -> User.call_f(args);
                case "-a" -> Admin.call_f(args);
                case "-example" -> examples();
                default -> {
                    System.out.println("\nEnter valid command from below\n");
                    printHelp();
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("\nMAIN:" + e.getMessage());
            System.err.println("Enter any one command from help menu\n");
        }
    }
}
