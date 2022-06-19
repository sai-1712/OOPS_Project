package project;

public class operations extends connector {

    public static void features() {
        System.out.println("\nSQL OPERATIONS :");
        System.out.println("Type '-select <table name>' for select operation");
        System.out.println("Type '-select <table name> <attributes> ...' for select operation with specified attributes");
        System.out.println("Type '-insert <tablename> <id,name,....> (ex: -insert account 'A-134','up',450.00)");
        System.out.println("Type '-insertcsv <tablename> <csvpath>' to insert/update data with csv");
        System.out.println("Type '-delete <tablename> <id><name>..'");
        System.out.println("Type '-update <tablename> <id><name>..   -set <corresponding_values>' ");
        System.out.println("Type '-update <tablename> <id><name>..   -att <attributes> -set <corresponding_values>' to update specified attributes only.");
        System.out.println("Type '-w <attribute> -condition <value>' (ex: -w age -gt 19) at end to add condition for any query");
        System.out.println("Type '-sort <attribute> -condition' (ex: -sort id asc) at end to sort the result");
        System.out.println("Type '-c' to clear/ignore the current input statement.\n");
    }

    public static String condition_pros(String s) {
        String m;
        switch (s) {
            case "-gt" -> m = " >";
            case "-lt" -> m = " <";
            case "-e" -> m = " =";
            case "-gte" -> m = " >=";
            case "-lte" -> m = " <=";
            default -> throw new IllegalStateException("Unexpected value: " + s);
        }

        return m;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }

    public static String condition_merge(String[] s, int c,String t)
    {
        String condition = "where ";
        int i = c+2;
        if(t.equals("state"))
            condition = condition.concat("state."+s[c]);
        else if(t.equals("winnerCandidate"))
            condition = condition.concat("winnercandidate."+s[c]);
        else
            condition = condition.concat(s[c]);

        condition = condition.concat(condition_pros(s[c + 1]));

        if(isInteger(s[c+2]))
            condition = condition.concat(" " + s[c + 2]);
        else{

            int k = s.length;
            String merge = "";
            for (int m = i; m < k; m++) {
                if(s[m].equals("-sort"))
                    break;
                merge = merge.concat(s[m]);
            }
            condition = condition.concat(" \"" + merge + "\"");
        }

        return condition;
    }

    public static String sorting(String[] s, int c)
    {
        String sort = " order by ";
        sort = sort.concat(s[c] + " ");
        sort = sort.concat(s[c+1] + " ");
        return sort;
    }

    public static String[] merge_attributes(String[] s)
    {
        int ind = 1;
        if (s[0].equals("-a"))
            ind++;

        String[] Query = new String[3];
        String merge = "";
        String condition = "where ";
        String sort = " order by ";
        Query[1] = "";
        Query[2] = "";
        int c = 0;
        boolean flag = false;

        if (s.length > (ind + 2)) {
            for (int i = ind + 2; i < s.length; i++) {

                if (s[i].equals("-w")) {
                    if (i == (ind+2))
                        merge = "* ";
                    Query[1] = condition_merge(s,i+1,s[ind+1]);
                    i = i+3;
                    continue;
                }

                if (s[i].equals("-sort")) {
                    if (i == (ind+2))
                        merge = "* ";
                    Query[2] = sorting(s,i+1);
                    break;
                }

                if (i+1 < s.length ){
                    if (s[i + 1].contains("-")) {
                        flag = true;
                    }
                }

                if (i == s.length - 1 || flag)
                    merge = merge.concat(s[i] + " ");

                else
                    merge = merge.concat(s[i] + ", ");
            }
            Query[0] = merge ;
        } else
            Query[0] = "* ";

        return Query;
    }

}