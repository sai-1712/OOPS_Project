package project;

import java.util.Arrays;

public class User extends operations{

    public static String[] access= {"-select","-search"};

    public static void call_f(String[] args)
    {
        template obj=table.call_table(args);
        if(obj==null)
            System.exit(1);
        try {
            switch (args[1]) {
                case "-select"-> obj.select(args);
                case "-search"-> template.search(args);
                default->{
                    System.err.println("\nUser cannot access " +args[1]+ " operation.\n");
                    System.out.println("Available operations for user : \n" + Arrays.toString(access));
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("\nEnter any one valid command from sql operations \n");
            operations.features();
        }
    }

}