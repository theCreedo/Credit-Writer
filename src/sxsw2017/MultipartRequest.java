package sxsw2017;

import java.io.File;
import java.io.IOException;
import java.util.List;
public class MultipartRequest {

    public static void main(String[] args) {

    	String FILE_PATH_NAME = "";
    	String JSON_POST_REQUEST = "";
    	String FILE_NAME = "";
        String charset = "UTF-8";
        File uploadFile = new File(FILE_PATH_NAME);
        String requestURL = "https://api.socan.ca/sandbox/SubmitNLMP?apiKey=l7xx50540a4a671342868a65f8a8f4a71d7a";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            System.out.println("RIP");

   //         multipart.addJsonField("nlmp", JSON_POST_REQUEST);
            multipart.addFilePart("file", uploadFile);
            multipart.addFormField("fileName", FILE_NAME);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
