package test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {

    PrintWriter out;
    Scanner in;
    DictionaryManager dm;

    public BookScrabbleHandler(){
        this.dm = DictionaryManager.get();
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        out = new PrintWriter(outToClient);
        in = new Scanner(inFromclient);
        Boolean result = false;
        String text = in.nextLine();

        String[] clientRequests = text.split(",");
        String[] books = new String[clientRequests.length - 1];
        System.arraycopy(clientRequests, 1, books, 0, books.length);

        if(clientRequests[0].equals("Q")){
            result = dm.query(books); 
        } else if (clientRequests[0].equals("C")){
            result = dm.challenge(books); 
        }

        out.println(new StringBuilder(String.valueOf(result)).toString());
        out.flush();
	}

    @Override
    public void close() {
        in.close();
        out.close();
    }
}
