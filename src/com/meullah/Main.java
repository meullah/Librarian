package com.meullah;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args)  {

        Library lib = new Library();
//        lib.getAuthors();
        switch (args[0].toLowerCase()){
            case "add":
                switch (args[1].toLowerCase()){
                    case "collection":
                        if(args.length==3)
                            lib.addCollection(args[2]);
                        else
                            System.out.println("Invalid Command");
                        break;
                    case "book":
                        if(args.length==4)
                            System.out.println(args[2]+args[3]);
                        else
                            System.out.println("Invalid Command");
                        break;
                    case "member":
                        if (args.length==3)
                            System.out.println(args[2]);
                        else
                            System.out.println("Invalid Command");
                        break;
                    default:
                        System.out.println("Invalid Command");

                }
                break;
            case "rent":
                if(args.length==3){
                    lib.rentBook(args[1],args[2]);
                }
                else
                    System.out.println("Invalid Command");

                break;
            case "relinquish":
                if(args.length==3){
                    if(args[1].toLowerCase().equals("all")){
                        lib.relinquishAll(args[2]);
                    }
                    else
                        lib.relinquishBook(args[1],args[2]);
                }
                break;
            case "common":
                List<String> strList = new ArrayList<>();
                for(byte i=1;i<args.length;i++){
                    strList.add(args[i]);
                }
                String [] strArr = new String[strList.size()];
                strArr = strList.toArray(strArr);
                lib.common(strArr);
                break;
            case "exit":
                System.exit(1);
                break;
            case "book":
                if(args.length>=2){
                    if(args[1].toLowerCase().equals("history")){
                        if(args.length==3){
                            lib.bookHistory(args[2]);
                        }
                        else {
                            System.out.println("Invalid Command");
                        }
                        break;
                    }
                    if(args.length==2){
                        lib.getBook(args[1],false);
                        break;
                    }
                    else if (args.length==3 && args[2].toLowerCase().equals("long")){
                        lib.getBook(args[1],true);
                        break;
                    }
                    else {
                        System.out.println("Invalid Command");
                    }

                }
                else
                    System.out.println("Invalid Command");
                break;
            case "list":
                if(args.length>1){
                    switch (args[1].toLowerCase()){
                        case "all":
                            if(args.length==2)
                                lib.getAllBooks(false);
                            else if(args.length==3 && args[2].toLowerCase().equals("long"))
                                lib.getAllBooks(true);
                            else
                                System.out.println("Invalid Command");
                            break;

                        case "available":
                            if(args.length==2)
                                lib.getAvailableBooks(false);
                            else if(args.length==3 && args[2].toLowerCase().equals("long"))
                                lib.getAvailableBooks(true);
                            else
                                System.out.println("Invalid Command");
                            break;
                        case "authors":
                            if(args.length==2)
                                lib.getAuthors();
                            else
                                System.out.println("Invalid Command");
                            break;
                        case "genre":
                            if(args.length==2)
                                lib.getGenres();
                            else
                                System.out.println("Invalid Command");
                            break;

                        default:
                            System.out.println("Invalid Command");
                    }
                }
                else {
                    System.out.println("Invalid Command");
                }
                break;
            case "author":
                if(args.length==2){
                    lib.getBooksByAuthor(args[1]);
                }
                else {
                    System.out.println("Invalid Command");
                }
                break;
            case "genre":
                if(args.length==2){
                    lib.getBooksByGenre(args[1]);
                }
                else {
                    System.out.println("Invalid Command");
                }
                break;
            case "member":
                if(args.length<=3){
                    if(args[1].toLowerCase().equals("history") && args.length==3){
                        lib.memberRentalHistory(args[2]);
                    }
                    else if(args.length==2)
                        lib.getMember(args[1]);
                    else
                        System.out.println("Invalid Command");
                    break;
                }
                else {
                    System.out.println("Invalid Command");
                }
                break;

            case "save":
                if(args.length==3 && args[1].toLowerCase().equals("collection"))
                    lib.saveCollection(args[2]);
                else
                    System.out.println("Invalid Command");

            default:
                System.out.println("Invalid Command");

        }

    }

}
