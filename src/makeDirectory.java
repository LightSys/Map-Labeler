import java.io.File;
/*
Created by Will Kercher 2/29/18
Purpose: Takes in a string and creates a directory named after that string, if directory exists doesn't do anything SWAG!
 */
public class makeDirectory {
    public makeDirectory(){}
    public static void makeNewDir(String dirName) {
        File theDir = new File(dirName);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;
            try{
                result = theDir.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }
}
