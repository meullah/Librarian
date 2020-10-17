package com.meullah;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

/**
 * Represents Book objects within the system.
 */
public class Book {
    private final String Author;
    private final String Genre;
    private final String serialNumber;
    private final String Title;

    public Book(String author, String genre, String serialNumber, String title){
        Author = author;
        Genre = genre;
        this.serialNumber = serialNumber;

        Title = title;
    }

    /**
     * Reads in the collection of books from the given file.
     * If the file doesn't exist, return null.
     * If the file exists, traverse through the csv and create a new book object for each line. The method should then return the list of books in the same order they appear in the csv file.
     * Parameters:
     * filename - The csv file to read.
     * Returns:
     * The collection of books stored in the csv file.
     *
     * @param filename Name of csv to extract Book Objects
     * @return List of Books
     */
    public static List<Book> readBookCollection(String filename){
        try {
            List<Book> allBooks = new ArrayList<>();
            File fileToRead = new File(filename);
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                Book temp = new Book(dataList.get(2).trim().toLowerCase(),dataList.get(3).trim().toLowerCase(),dataList.get(0).trim(),dataList.get(1).trim().toLowerCase());
                allBooks.add(temp);
            }
            myReader.close();
            return allBooks;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Creates a new list containing books by the specified author.
     * If the list or author does not exist, return null.
     * If they do exist, create a new list with all the books written by the given author, sort by serial number, and return the result.
     * Parameters:
     * books - The list of books to filter.
     * author - The author to filter by.
     * Returns:
     * The filtered list of books.
     *
     * @param books List of Book Objects
     * @param author Name of Author to filter
     * @return list of Books filtered by author
     */
    public static List<Book> filterAuthor(List<Book> books, String author){
        List<Book> filteredBooks= new ArrayList<>();
        author = author.trim().toLowerCase();
        for(Book _book :books){
            if(_book.getAuthor().equals(author)){
                filteredBooks.add(_book);
            }
        }
        if(filteredBooks.size()!=0)
            return filteredBooks;
        else
            return null;
    }

    /**
     *Creates a new list containing books by the specified genre.
     * If the list or genre does not exist, return null.
     * If they do exist, create a new list with all the books in the specified genre, sort by serial number, and return the result.
     * Parameters:
     * books - The list of books to filter.
     * genre - The genre to filter by.
     * Returns:
     * The filtered list of books.
     * @param books List of Book Objects
     * @param genre Genre of Book to filter
     * @return list of Books filtered by genre
     */
    public static List<Book> filterGenre(List<Book> books, String genre){
        List<Book> filteredBooks= new ArrayList<>();
        genre = genre.trim().toLowerCase();
        for(Book _book :books){
            if(_book.getGenre().equals(genre)){
                filteredBooks.add(_book);
            }
        }
        if(filteredBooks.size()!=0)
            return filteredBooks;
        else
            return null;
    }

    /**
     * Retrieves the book from the given file based on its serial number.
     * * If the file or given book doesn't exist, return null.
     * If the file does exist, retrieve the information for the book with the given serial number, and return the newly created book.
     * Parameters:
     * filename - The csv file containing a book collection.
     * serialNumber - The serial number for the book.
     * Returns:
     * The Book object created based on the file.
     * @param filename Name of File
     * @param serialNumber Book Serial Number
     * @return Books object
     */
    public static Book readBook(String filename, String serialNumber) {
        if(hasBook(filename,serialNumber)!=null){
            return hasBook(filename,serialNumber);
        }
        else {
            Book newBook  = getBookData(serialNumber);
            writeDataToFile(newBook,filename);
            return newBook;
        }
    }

    public static Book hasBook(String filename, String serialNumber){
        try {
            File fileToRead = new File(filename);
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(0).trim().equals(serialNumber.trim())) {
                    return new Book(dataList.get(2).trim().toLowerCase(), dataList.get(3).trim().toLowerCase(), dataList.get(0).trim(), dataList.get(1).trim().toLowerCase());
                }
            }
            myReader.close();
            return null;
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    /**
     * Save the collection of books to the given file.
     * If the file or collection doesn't exist, do nothing.
     * Otherwise, write the collection to file, ensuring that the file maintains the csv format.
     * Parameters:
     * filename - The csv file to write to.
     * books - The collection of books to write to file.
     * @param filename name of file
     * @param books Collection of Book Objects
     */
    public static void saveBookCollection(String filename, Collection<Book> books){
        try {
            FileWriter myWriter = new FileWriter(filename,true);
            for(Book b:books){
                myWriter.write(b.getSerialNumber()+","+b.getTitle()+","+b.getAuthor()+","+b.getGenre()+"\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    /**
     * Gets data to create a new book Object
     * @param serialNumber Book serial number
     * @return Book Object
     */
    private static Book getBookData(String serialNumber){
        System.out.println("Please enter details of new book: \n");
        String Title = Console.read("Please enter Book Title");
        String Author = Console.read("Please enter Name of the Author");
        String Genre = Console.read("Enter Genre");

        return new Book(Author,Genre,serialNumber,Title);
    }

    /**
     * writes New Book data to csv file
     * @param b  object of Book
     * @param fileName File name
     */
    public static boolean writeDataToFile(Book b, String fileName){
        try {
            FileWriter myWriter = new FileWriter(fileName,true);
            myWriter.write(b.getSerialNumber()+","+b.getTitle()+","+b.getAuthor()+","+b.getGenre()+"\n");
            myWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Format:
     * If the book is rented: [serialNumber]: [title] ([author], [genre])\nRented by [renter number].
     * If the book is available: [serialNumber]: [title] ([author], [genre])\nCurrently available.
     * @return The extended String
     */
    public String longString(){
        if(isRented(serialNumber)){
            return serialNumber+":  "+Title+" ("+Author+", "+Genre+")"+"\nRented by "+rentedPerson(serialNumber);
        }
        else {
            return serialNumber+":  "+Title+" ("+Author+", "+Genre+")"+"\nCurrently available ";
        }
    }

    /**
     * Formats the Book object to create the short form of its i.e [title] ([author])*
     * @return : The shortened String.
     */
    public String shortString(){
        return Title+ " ("+Author+")";
    }

    /**
     * Returns the book to the library.
     * If the member does not exist or isn't the current renter, do nothing and return false.
     * If the book is rented by the member, change the current renter and return true.
     * @param member The member returning the book.
     * @return The outcome of the rental transaction.
     */
    public boolean relinquish(Member member){
        if(Member.relinquish(member.getMemberId(), serialNumber)){
            Member.removeLine(serialNumber+","+member.getMemberId(),"rentedBooks.csv");
            return true;
        }
        return false;
    }

    /**
     * Sets the current renter to be the given member.
     * If the member does not exist, or the book is already being rented, do nothing and return false.
     * If the book is able to be rented, return true.
     * @param member The new person renting the book.
     * @return The outcome of the rental transaction. Ture | False
     */
    public boolean rent(Member member){
        if(isRented(serialNumber)){
            return false;
        }
        else {
            String memberId = member.getMemberId();
            if(Member.addLine(serialNumber,"./members/"+memberId+".renting")){
                Member.addLine(serialNumber+","+memberId,"rentedBooks.csv");
                Member.addLine(serialNumber+","+memberId,"booksHistory.csv");
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static boolean rent(String memberId,String bookId){
        if(isRented(bookId)){
            return false;
        }
        else {
            if(Member.addLine(bookId,"./members/"+memberId+".renting")){
                Member.addLine(bookId+","+memberId,"rentedBooks.csv");
                Member.addLine(bookId+","+memberId,"booksHistory.csv");
                return true;
            }
        }
        return false;
    }

    public static Set<String>bookRentalHistory(String bookSerialNumber){
        Set<String> temp = new HashSet<>();
        try {
            File fileToRead = new File("booksHistory.csv");
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(0).trim().equals(bookSerialNumber.trim())) {
                    temp.add(dataList.get(1));
                }
            }
            myReader.close();
            return temp;
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    /**
     * tells whether the given book is available or rented
     * @return true if book is rented false otherwise
     */
    public static boolean isRented(String book_serialNumber) {
        try {
            File fileToRead = new File("rentedBooks.csv");
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(0).trim().equals(book_serialNumber)) {
                    myReader.close();
                    return true;
                }
            }
            myReader.close();
            return false;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * @param book_serialNumber serial number of book
     * @return Returns the Id of member who is currently renting the book
     */
    public static String rentedPerson(String book_serialNumber){
        try {
            File fileToRead = new File("rentedBooks.csv");
            Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> dataList = Arrays.asList(data.split(","));
                if (dataList.get(0).trim().equals(book_serialNumber)) {
                    myReader.close();
                    return dataList.get(1);
                }
            }
            myReader.close();
            return null;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public String getTitle() {
        return Title;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getGenre() {
        return Genre;
    }

    public String getAuthor() {
        return Author;
    }


}
