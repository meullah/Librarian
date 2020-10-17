package com.meullah;

import java.io.*;
import java.util.*;

/**
 * Represents Members within the system.
 */
public class Member {
    private final String MemberId;
    private final String Name;

    public Member(String name) {
        MemberId = generateNewId();
        Name = name.trim().toLowerCase();
        try {
            File myObj = new File("./members/"+MemberId+".renting");
            File myObj1 = new File("./members/"+MemberId+".returned");
            if (myObj.createNewFile() && myObj1.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//                System.out.println("File created: " + myObj1.getName());
            } else {
//                System.out.println("File already exists.");
            }
        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("./members/members.csv",true);
            myWriter.write(MemberId+","+Name+"\n");
            myWriter.close();
//            System.out.println("Member Added Successfully ");
        } catch (IOException e) {
//            System.out.println("Failed to add new member!");
            e.printStackTrace();
        }

    }

    /**
     * Adds new memeber to system
     * @param Name name of member
     * @return
     */
    public static boolean addMember(String Name){
        String MemberId = generateNewId();
        try {
            File myObj = new File("./members/"+MemberId+".renting");
            File myObj1 = new File("./members/"+MemberId+".returned");
            if (myObj.createNewFile() && myObj1.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//                System.out.println("File created: " + myObj1.getName());
            } else {
//                System.out.println("File already exists.");
            }

            FileWriter myWriter = new FileWriter("./members/members.csv",true);
            myWriter.write(MemberId+","+Name+"\n");
            myWriter.close();
            return true;

        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the intersection of the members' histories, ordered by serial number.
     * If members is invalid, return null.
     * Otherwise, return the sorted intersection of books.
     *
     * @param members The array of members.
     * @return The sorted intersection of books.
     */

    public static List<Book> commonBooks(String[] members) {
        HashSet<String> setOfBookIds = new HashSet<>();
        if(members.length!=0) {
            for(Book b:renting(members[0])){
                setOfBookIds.add(b.getSerialNumber());
            }
            for(Book b:returned(members[0])){
                setOfBookIds.add(b.getSerialNumber());
            }

        }
        for (String memberId:members){
            HashSet<String> temp = new HashSet<>();
            for(Book b:renting(memberId)){
                temp.add(b.getSerialNumber());
            }
            for(Book b:returned(memberId)){
                temp.add(b.getSerialNumber());
            }
            setOfBookIds.retainAll(temp);
        }

        List<Book> _commonBooks = new ArrayList<>();
        for(String bookId:setOfBookIds){
            _commonBooks.add(Book.readBook("books.csv",bookId));
        }
        return _commonBooks;
    }

    /**
     * Returns the list of books currently being rented, in the order they were rented.
     *
     * @param memberId id of member
     * @return List of books currently being rented
     */
    public static List<Book> renting(String memberId) {
        return getMembersBook(memberId,"renting");
    }

    /**
     * Returns the list of books returned to library
     * @param memberId id of member
     * @return List of books currently being rented
     */
    public static List<Book> returned(String memberId) {
        return getMembersBook(memberId,"returned");
    }

    /**
     * gets all the books from members history rented |  returned
     * @param memberId serial number or id of member
     * @param bookStatus renting or returned
     * @return list of Book of objects
     */
    private static List<Book> getMembersBook(String memberId, String bookStatus){
        String path = "./members/" + memberId + "."+bookStatus;
        List<Book> books = new ArrayList<>();
        File fileToRead = new File(path);
        try {
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                books.add(Book.readBook("books.csv",data.trim()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("member not found");
            return books;
        }
        return books;
    }

    public static boolean hasMember(String memberId){
        try {
            File fileToRead = new File("./members/members.csv");
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(0).trim().equals(memberId)) {
                    return true;
                }
            }
            myReader.close();
            return false;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static List<String> readAllText(String filename) {
        List<String> data = new ArrayList<>();
        try {
            File fileToRead = new File(filename);
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                data.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
        return data;
    }
    public static void removeLine(String line, String filename) {
        List<String> fileData = readAllText(filename);
        if(fileData.size()!=0){
            try {
                FileWriter myWriter = new FileWriter(filename);
                for(String s : fileData)
                {
                    if(s.trim().equals(line.trim()))
                        continue;
                    myWriter.write(s+"\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    }
    public static boolean addLine(String Line, String filename){
        try {
            FileWriter myWriter = new FileWriter(filename,true);
            myWriter.write(Line+"\n");
            myWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static boolean deleteFile(String filename){
        File myObj = new File(filename);
        //            System.out.println("Deleted the file: " + myObj.getName());
        //            System.out.println("Failed to delete the file.");
        return myObj.delete();
    }
    public static boolean createFile(String filename){
        try {
            File myObj = new File(filename);
            //                System.out.println("File created: " + myObj.getName());
            //                System.out.println("File already exists.");
            return myObj.createNewFile();
        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the history of books rented, in the order they were returned.
     * Books currently being rented are not included in this list.
     * @return List of renting books
     */
    public List<Book> history(){
        return returned(getMemberId());
    }

    /**
     * Returns the book to the library.
     * If the book doesn't exist, or the member isn't renting the book, return false.
     * Otherwise, set the renter of the book to null, add it to the rental history, and return true.
     * @param bookId book serial number
     * @param memberId member serial number/id
     * @return The outcome of the rental transaction.
     */
    public static boolean relinquish(String memberId,String bookId) {
        if(Book.hasBook("books.csv",bookId)!=null && hasMember(memberId)){
            if(readAllText("./members/"+memberId+".renting").size()!=0){
                for( String _bookId : readAllText("./members/"+memberId+".renting")){
                    if(_bookId.trim().equals(bookId)) {
                        removeLine(bookId,"./members/"+memberId+".renting");
                        addLine(bookId,"./members/"+memberId+".returned");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean relinquishAll(String memberId) {
        if(hasMember(memberId)){
            if(readAllText("./members/"+memberId+".renting").size()!=0){
                for( String _bookId : readAllText("./members/"+memberId+".renting")){
                    addLine(_bookId,"./members/"+memberId+".returned");
                }
                if(deleteFile("./members/"+memberId+".renting")){
                    return createFile("./members/" + memberId + ".renting");
                }
            }
        }
        return false;
    }

    /**
     * get member name from member id
     *
     * @param memberId id of member
     * @return
     */
    public static String getName(String memberId) {
        try {
            File fileToRead = new File("./members/members.csv");
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(0).trim().equals(memberId)) {
                    return dataList.get(1).trim();
                }
            }
            myReader.close();
            return null;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public String getName() {
        return Name;
    }
    /**
     * get id from member name
     *
     * @param memberName name of member
     * @return
     */
    public static String getMemberId(String memberName) {
        try {
            File fileToRead = new File("./members/members.csv");
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(1).trim().equals(memberName)) {
                    return dataList.get(0).trim();
                }
            }
            myReader.close();
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }
    public String getMemberId() {
        return MemberId;
    }
    public static String generateNewId(){
        File fileToRead = new File("./members/lastId");
        String data = null;
        try {
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            data = String.valueOf(Integer.parseInt(data)+1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            FileWriter myWriter = new FileWriter("./members/lastId");
            myWriter.write(data);
            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
}