package com.meullah;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Library Operating System.
 */
public class Library {
    static String HELP_STRING;

    /**
     * Adds a book to the system by reading it from a csv file.
     *
     * Invoked by the command "ADD BOOK [file] [serialNumber]", this method reads the given file, searches for the serial number, and then adds the book to the system.
     * If the file does not exist, output "No such file."
     * If the book does not exist within the file, output "No such book in file."
     * If the book's serial number is already present in the system, output "Book already exists in system."
     * If the book is successfully added, output "Successfully added: [short string]."
     * @param bookFile The csv file to read
     * @param serialNumber The serial number of the book
     */
    public void addBook(String bookFile, String serialNumber){
        File file = new File(bookFile);
        if(file.exists()) {
            Book newBook = Book.hasBook(bookFile, serialNumber);
            if (newBook != null) {
                // check if book already present in the system!
                Book _book = Book.hasBook("books.csv",serialNumber);
                // if _book == null then book is not present in the system
                if(_book == null){

                    if(Book.writeDataToFile(newBook,"books.csv")){
                        System.out.println("Successfully added:  "+ newBook.shortString());
                    }
                }
                else {
                    System.out.println("Book already exists in system.");
                }
            }
            else {
                System.out.println("No such book in file.");
            }
        }
        else {
            System.out.println("No such file.");
        }
    }

    /**
     * Adds the collection of books stored in a csv file to the system.
     * Invoked by the command "ADD COLLECTION [filename]", this method reads all the books from a csv file and adds them to the system.
     * If the file does not exist, output "No such collection."
     * If no books are able to be added (for example, because they all have serial numbers already present in the system), output "No books have been added to the system."
     * If at least one book has been added to the system, output "[number] books successfully added."
     * @param filename - The csv file storing the collection of books
     */
    public void addCollection(String filename){
        File file = new File(filename);
        int number = 0;
        List<Book> books = new ArrayList<>();
        if(file.exists()) {
            books = Book.readBookCollection(filename);
            if(books.size()!=0){
                for(Book b:books){
                    // check if book already present in the system!
                    // if Book.hasBook() == null then book is not present in the system
                    if(Book.hasBook("books.csv",b.getSerialNumber())==null){
                        if(Book.writeDataToFile(b,"books.csv")){
                            number+=1;
                        }
                    }
                }
                if(number>0){
                    System.out.println(number+" books successfully added.");
                }else
                    System.out.println("No books have been added to the system.");
            }
            else {
                System.out.println("Given file is empty");
            }
        }
        else {
            System.out.println("No such collection.");
        }
    }

    /**
     * Adds a member to the system.
     *
     * Invoked by the command "ADD MEMBER [name]", this method creates a new member with the specified name and adds them to the system.
     * The first member will always have member number 100000, and subsequent members will increment from there (so the second member has number 100001).
     * Once the member is successfully added to the system, output "Success."
     * @param name The name of the member
     */
    public void addMember(String name){
        if(Member.addMember(name)){
            System.out.println("Success.");
        }else
            System.out.println("Failed to add new member.");
    }

    /**
     * Prints out all the member numbers of members who have previously rented a book.
     *
     * Invoked by the command "BOOK HISTORY [serialNumber]", this method outputs every member that has rented the given book, in the order of rents.
     * If the book does not exist in the system, output "No such book in system."
     * If the book has not been rented, output "No rental history."
     * @param serialNumber  The serial number of the book
     */
    public void bookHistory(String serialNumber){
        if(Book.hasBook("books.csv",serialNumber)==null){
            System.out.println("No such book in system.");
        }
        else {
            Set<String> bookHistory = Book.bookRentalHistory(serialNumber);
            if(bookHistory.size()==0){
                System.out.println("No rental history.");
            }
            else {
                for(String bookRenter:bookHistory)
                    System.out.println(bookRenter);
            }
        }
    }

    /**
     * Prints out all the books that all members provided have previously rented.
     *
     * Invoked by the command "COMMON [member1] [member2] ...", this method lists out the short strings of all the books that have been rented by listed members, ordered by serial number.
     * If there are no members in the system, output "No members in system."
     * If there are no books in the system, output "No books in system."
     * If at least one of the members does not exist, output "No such member in system."
     * If there are duplicate members provided, output "Duplicate members provided."
     * If there are no common books, output "No common books."
     * @param memberNumbers The array of member numbers
     */
    public void common(String[] memberNumbers){
        for(String s:memberNumbers){
            if(Member.hasMember(s)==false){
                System.out.println("No such member in system.");
                return;
            }
        }
        if(haveDuplicates(memberNumbers)){
            System.out.println("Duplicate members provided.");
            return;
        }
        List<Book> commonBooks = Member.commonBooks(memberNumbers);
        if(commonBooks.size()==0){
            System.out.println("No common books.");
        }
        else {
            for(Book b: commonBooks){
                System.out.println(b.getTitle());
            }
        }

    }

    /**
     * Prints out the formatted strings for all books in the system.
     *
     * Invoked by "LIST ALL", this method prints out either the short string or long string of
     * all books (depending on whether "LONG" is included in the command),separated by new lines.
     * If there are no books in the system, output "No books in system."
     * @param fullString  Whether to print short or long strings
     */
    public void getAllBooks(boolean fullString){
        List<Book> books = Book.readBookCollection("books.csv");
        if(books.size()==0){
            System.out.println("No Books in the system.");
            return;
        }
        if(fullString){
            for(Book b:books){
                System.out.println((b.longString()));
            }
        }
        else {
            for(Book b:books){
                System.out.println(b.shortString());
            }
        }
    }

    /**
     * Prints out all the authors in the system.
     *
     * Invoked by "LIST AUTHORS", this method prints out the list of authors in the system. Each genre should only be printed once, and they should be printed in alphabetical order.
     * If there are no books, output "No books in system."
     */
    public void getAuthors(){
        List<Book> books = Book.readBookCollection("books.csv");
        if(books.size()==0){
            System.out.println("No Books in the system.");
            return;
        }
        for(Book b:books){
            System.out.println(b.getAuthor());
        }
    }


    /**
     * Prints out the formatted strings for all available books in the system.
     *
     * Invoked by "LIST AVAILABLE", this method prints out either the short string or long string of all available books (depending on whether "LONG" is included in the command), seperated by new lines. Note that a book is available when it is not currently rented by a member.
     * If there are no books in the system, output "No books in system."
     * If there are no available books, output "No books available."
     * @param fullString  Whether to print short or long strings
     */
    public void getAvailableBooks(boolean fullString){
        List<Book> temp = Book.readBookCollection("books.csv");
        List<Book> books = new ArrayList<>();
        for(Book b: temp){
            if(Book.isRented(b.getSerialNumber()))
                continue;
            books.add(b);
        }
        if(books.size()==0){
            System.out.println("No Books in the system.");
            return;
        }
        if(fullString){
            for(Book b:books){
                System.out.println((b.longString()));
            }
        }
        else {
            for(Book b:books){
                System.out.println(b.shortString());
            }
        }
    }

    /**
     * Prints either the short or long string of the specified book.
     *
     * Invoked by the command "BOOK [serialNumber] [LONG]", this method prints out the details for the specified book.
     * If there are no books, output "No books in system."
     * If the book does not exist, output "No such book in system."
     * If fullString is true, output the long string of the book.
     * If fullString is false, output the short string of the book.
     * @param serialNumber The serial number of the book
     * @param fullString Whether to print short or long string
     */
    public void getBook(String serialNumber, boolean fullString){
        Book b = Book.hasBook("books.csv",serialNumber);
        if(fullString)
            System.out.println(b.longString());
        else
            System.out.println(b.shortString());
    }

    /**
     * Prints all books in the system by the specified author.
     *
     * Invoked by "AUTHOR [author]", this method outputs all the books in the system that were written by the specified author. Each book should have its short string printed on a new line, and books should be ordered by serial number.
     * If there are no books, output "No books in system."
     * If there are no books by the specified author, output "No books by author [author]."
     * @param author The author to filter by
     */
    public void getBooksByAuthor(String author){
        List<Book> books = Book.readBookCollection("books.csv");
        if(books.size()==0)
            System.out.println("No books in system.");
        else {
            List<Book> filteredBooks = Book.filterAuthor(books,author);
            if(filteredBooks.size()==0){
                System.out.println("No books by author "+author);
            }
            else {
                for(Book b:filteredBooks){
                    System.out.println(b.shortString());
                }
            }
        }
    }

    /**
     * Prints all books in the system with the specified genre.
     *
     * Invoked by "GENRE [genre]", this method outputs all the books in the system with the specified genre. Each book should have its short string printed on a new line, and books should be ordered by serial number.
     * If there are no books, output "No books in system."
     * If there are no books of the specified genre, output "No books with genre [genre]."
     * @param genre The genre to filter by
     */
    public void getBooksByGenre(String genre){
        List<Book> books = Book.readBookCollection("books.csv");
        if(books.size()==0)
            System.out.println("No books in system.");
        else {
            List<Book> filteredBooks = Book.filterGenre(books,genre);
            if(filteredBooks.size()==0){
                System.out.println("No books by genre "+genre);
            }
            else {
                for(Book b:filteredBooks){
                    System.out.println(b.shortString());
                }
            }
        }

    }

    /**
     * Prints out all the genres in the system.
     *
     * Invoked by "LIST GENRES", this method prints out the list of genres stored in the system. Each genre should only be printed once, and they should be printed in alphabetical order.
     * If there are no books, output "No books in system."
     */
    public void getGenres(){
        List<Book> books = Book.readBookCollection("books.csv");
        if(books.size()==0){
            System.out.println("No Books in the system.");
            return;
        }
        Set<String> genre = new HashSet<>();
        for(Book b:books){
            genre.add(b.getGenre());
        }
        for (String _genre: genre){
            System.out.println(_genre);
        }

    }

    /**
     * Prints the details of the specified member.
     *
     * Invoked by the command "MEMBER [memberNumber]", this method outputs the details of the specified member.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member exists, output "[memberNumber]: [member name]"
     * @param memberNumber The member's member number
     */
    public void getMember(String memberNumber){
        if(Member.hasMember(memberNumber))
            System.out.println(Member.getName(memberNumber));
        else
            System.out.println("No such member in system.");
    }

    /**
     * Prints a list of all the books a member has previously rented.
     *
     * Invoked by the command "MEMBER HISTORY [memberNumber]",
     * this method prints out all the books the specified member has previously rented, i
     * n the order that they were rented. Each book should have its short string printed on a new line.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member has not rented any books, output "No rental history for member."
     * @param memberNumber The member's member number
     */
    public void memberRentalHistory(String memberNumber){
        if(Member.hasMember(memberNumber)){
            List<Book> rentedBooks = Member.returned(memberNumber);
            if(rentedBooks.size()==0){
                System.out.println("No rental history for member.");
                return;
            }
            for(Book b:rentedBooks){
                System.out.println(b.shortString());
            }
        }
        else
            System.out.println("No such member in system.");
    }

    /**
     * Makes a member return all books they are currently renting.
     *
     * Invoked by the command "RELINQUISH ALL [memberNumber]", this method causes the specified member to return all books they are currently renting.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member does exist, return all books and output "Success."
     * @param memberNumber The member's member number
     */
    public void relinquishAll(String memberNumber) {
        if (Member.hasMember(memberNumber)) {
            if (Member.relinquishAll(memberNumber))
                System.out.println("Success.");
            else {
                System.out.println("Failed.");
            }
        } else
            System.out.println("No members in system.");
    }

    /**
     * Returns a book to the system.
     *
     * Invoked by the command "RELINQUISH [memberNumber] [serialNumber]", this method gets the specified member to return the given book back to the system.
     * If there are no members in the system, output "No members in system."
     * If there are no books in the system, output "No books in system."
     * If the member does not exist, output "No such member in system."
     * If the book does not exist, output "No such book in system."
     * If the book is not being loaned out by the member, output "Unable to return book."
     * If the book is successfully return, output "Success."
     * @param memberNumber The members' member number
     * @param serialNumber The book's serial number
     */
    public void relinquishBook(String memberNumber, String serialNumber){

        if (Member.hasMember(memberNumber)) {
            if(Book.hasBook("books.csv",serialNumber)!=null){
                if(Member.relinquish(memberNumber,serialNumber))
                    System.out.println("Success.");
            }
            else
                System.out.println("No books in system.");

        } else
            System.out.println("No members in system.");
    }

    /**
     * Loans out a book to a member within the system.
     *
     * Invoked by the command "RENT [memberNumber] [serialNumber]", this method loans out the specified book to the given member.
     * If there are no members in the system, output "No members in system."
     * If there are no books in the system, output "No books in system."
     * If the member does not exist, output "No such member in system."
     * If the book does not exist, output "No such book in system."
     * If the book is already being loaned out, output "Book is currently unavailable."
     * If the book is successfully loaned out, output "Success."
     * @param memberNumber The members' member number
     * @param serialNumber The book's serial number
     */
    public void rentBook(String memberNumber, String serialNumber){
        if(Member.hasMember(memberNumber)){
            if(Book.isRented(serialNumber)){
                System.out.println("Book is currently unavailable.");
            }
            else {
                if(Book.rent(memberNumber,serialNumber))
                    System.out.println("success.");
            }
        }else
            System.out.println("No members in system.");
    }

    /**
     * Saves the current collection of books in the system to a csv file.
     *
     * Invoked by the command "SAVE COLLECTION [filename]", this method saves all the books stored in the
     * system to a csv file (in the same format as can be read with "ADD COLLECTION [filename").
     * If there are no books in the system, output "No books in system."
     * If there are books, write them to the file and output "Success."
     * @param filename The csv file to write the collection to
     */
    public void saveCollection(String filename){
        List<Book> books = Book.readBookCollection("books.csv");
        if(books.size()!=0){
            Book.saveBookCollection(filename,books);
            System.out.println("Success.");
        }
        else {
            System.out.println("No books in system.");
        }
    }


    /**
     * checks duplicates in a  String Array
     * @param members list of members ids in string
     * @return true if there are duplicates false otherwise
     */
    public static boolean haveDuplicates(String[] members)
    {
        Set<String> temp = new HashSet<>();
        for (String s : members)
        {
            if (temp.contains(s))
                return true;
            temp.add(s);
        }
        return false;
    }

}
