package com.automatad.Utilityresources;

public class payload {

    public static String getdata(String email){
        String payload="{\r\n  " +
                "  \"user\": {\r\n    " +
                "    \"email\": \""+email+"\"\r\n   " +
                "  }\r\n}";

        System.out.println(payload);
        return payload;
    }

    public static String getdata(String email,String name,String password){
        String payload="{\r\n  " +
                "  \"user\": {\r\n    " +
                "    \"email\": \""+email+"\",\r\n    " +
                "    \"name\": \""+name+"\",\r\n   " +
                "     \"password\": \""+password+"\"\r\n " +
                "   }\r\n}";

        System.out.println(payload);
        return payload;
    }

    public static String getdata(String email,String password){
        String payload="{\r\n  " +
                "  \"user\": {\r\n    " +
                "    \"email\": \""+email+"\",\r\n   " +
                "     \"password\": \""+password+"\"\r\n  " +
                "  }\r\n}";

        System.out.println(payload);
        return payload;
    }

    public static String resetdata(String token,String newPass){
        String payload="{\r\\n\t" +
                "\"reset_token\": \""+token+"\"," +
                "\r\n\t\"user\": {\r\n\t\t\"password\": \""+newPass+"\"" +
                "\r\n\t}" +
                "\r\n}";

        return payload;
    }
    public static String modifyData(String name){
        String payload="{\r\n   " +
                " \"user\": {\r\n      " +
                "  \"name\": \""+name+"\"\r\n  " +
                "  }\r\n}";

        return payload;
    }

    public static String modifyData(String oldPass,String newPass){
        String payload="{\r\n    \"user\": {\r\n     " +
                "   \"password\": \""+newPass+"\",\r\n      " +
                "  \"old_password\": \""+oldPass+"\"\r\n   " +
                " }\r\n}";

        return payload;
    }
}
