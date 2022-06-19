package project;

import java.util.Arrays;

public class Admin implements connection_details{

    public static String[] access= {"-select","-search","-insert","-insertCSV","-updateCSV","-delete","-update"};
    public static boolean admin_check(String p)
    {
        return p.equals(password);
    }

    public static void call_f(String[] args)
    {
        template obj=table.call_table(args);

        if(obj==null)
            System.exit(1);

        if (admin_check(args[1])) {
            try {
                switch (args[2]) {
                    case "-select"-> obj.select(args);
                    case "-search"-> template.search(args);
                    case "-insert"-> obj.insertCommand(args);
                    case "-insertCSV"-> obj.insertCSV(args);
                    case "-updateCSV"-> obj.updatecsv(args);
                    case "-delete"-> obj.delete(args);
                    case "-update"-> obj.update(args);
                    default->{
                        System.err.println("\nEnter valid operation!\n");
                        System.out.println("Available operations for Admin : \n" + Arrays.toString(access));
                    }
                }
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                System.out.println("\nFrom Admin: Enter any one valid command from sql operations \n");
                operations.features();
            }
        }
        else{
            System.err.println("\nWrong password !! \nAccess Denied.\n");
        }

    }

}