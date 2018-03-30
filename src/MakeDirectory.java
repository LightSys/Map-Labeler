import java.io.File;
/*
Created by Will Kercher 2/29/18
Purpose: Takes in a string and creates a directory named after that string, if directory exists doesn't do anything SWAG!
 */
public class MakeDirectory {
    public MakeDirectory(){}
    public static void makeNewDir(String dirName) {
        File theDir = new File(dirName);
        // if the directory does not exist, create it
        if (checkDirExists(dirName)) {
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
            else{
                Logger.addLog("DIR: " + dirName + " was unable to be made");
            }
        }
    }
    public static boolean checkDirExists(String dirName) {
        File theDir = new File(dirName);
        if (theDir.exists() && !theDir.isDirectory()){
            throw new IllegalArgumentException("Cannot create directory. File exists with same name: " + dirName);
        }
        return theDir.exists();
    }
}
