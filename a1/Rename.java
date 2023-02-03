import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Rename {
    public static void main(String[] args) {

        // extract options from command line
        java.util.LinkedHashMap<String, ArrayList<String>> options = parse(args);

        //region Help above all
        if (options.containsKey("-h") || options.containsKey("-help")) {
            printHelp();
            return;
        }
        //endregion
        //region Input Error handling
        if (options.containsKey("-fExists")) {
            System.out.println("ERROR filename option specified more than once.");
            return;
        }
        if (options.containsKey("-pExists")) {
            System.out.println("ERROR prefix option specified more than once.");
            return;
        }
        if (options.containsKey("-sExists")) {
            System.out.println("ERROR suffix option specified more than once.");
            return;
        }
        if (options.containsKey("-rExists")) {
            System.out.println("ERROR replace option specified more than once.");
            return;
        }
        if (options.containsKey("-invalidRguments")) {
            System.out.println("ERROR Invalid replace arguments." +
                    "\nValues cannot start with a dash(\"-\"), since that symbol is used to designate options.");
            return;
        }
        if (options.containsKey("-invalidInput")) {
            System.out.println("ERROR Invalid Input: " + options.get("-invalidInput").get(0) +
                    "\nValues cannot start with a dash(\"-\"), since that symbol is used to designate options.");
            return;
        }
        if (options.containsKey("-valSansKey")) {
            System.out.println("ERROR Arguement without Option: " + options.get("-valSansKey").get(0) +
                    "\nValues must have acssociated Option.");
            return;
        }
        if (options.containsKey("-rNotEnough")) {
            System.out.println("ERROR Replace option without enough arguements: " +
                    options.get("-rNotEnough").get(0) + " arguement(s) missing." +
                    "\nReplace requires 2 arguements.");
            return;
        }
        if (!options.containsKey("-filename")) {
            System.out.println("ERROR No filename specified.");
            return;
        }

        ArrayList<String> missingFiles = filesMissing(options.get("-filename"));
        if (missingFiles.size() > 0) {
            System.out.printf("ERROR Missing files:");
            for (String filename : missingFiles) {
                System.out.printf(" " + filename);
            }
            System.out.printf("\n");
            return;
        }

        if (!options.containsKey("-prefix") &&
                !options.containsKey("-suffix") &&
                !options.containsKey("-replace")) {
            System.out.println("ERROR No options specified.");
            return;
        }

        //endregion

        // Option handling
        ArrayList<String> files = options.get("-filename");
        String prefix = "";
        String suffix = "";
        String replace1 = "";
        String replace2 = "";
        //region Populate Prefix
        if (options.containsKey("-prefix")) {
            prefix = prefixConstruct(options.get("-prefix"));
        }
        //endregion
        //region Populate Suffix
        if (options.containsKey("-suffix")) {
            suffix = suffixConstruct(options.get("-suffix"));
        }
        //endregion
        //region Populate Replace
        if (options.containsKey("-replace")) {
            replace1 = options.get("-replace").get(0);
            replace2 = options.get("-replace").get(1);
        }
        //endregion

        // Order of operations matters, if a filename couln't be mutated with replace before p or s and can be after,
        // it will still be mutated given p or s operation occured before replace
        ArrayList<String> changeOrder = new ArrayList<String>();
        for (String option : options.keySet()) {
            if (!option.equals("-filename")){
                changeOrder.add(option);
            }
        }

        rename(files, changeOrder, prefix, suffix, replace1, replace2);

        return;

    }

    // When renaming need to check if new name already exists
    static void rename(ArrayList<String> files, ArrayList<String> changeOrder,
                       String prefix, String suffix, String replace1, String replace2){
        for (String oldfile : files){

            // Generate new file name post options
            int index=oldfile.lastIndexOf('/');
            String path = oldfile.substring(0,index+1);
            String newfile = oldfile.substring(index+1);
            for (String operation : changeOrder) {
                if (operation.equals("-prefix")) {
                    newfile = prefix + newfile;
                } else if (operation.equals("-suffix")) {
                    newfile = newfile + suffix;
                } else if (operation.equals("-replace")) {
                    if (newfile.contains(replace1)) {
                        newfile = newfile.replace(replace1, replace2);
                    } else {
                        System.out.println("ERROR File " + oldfile + " does not contain stubstring: " + replace1);
                        System.out.println("Rename Unsuccessful: " + oldfile);
                        return;
                    }
                }
            }

            // Checks if file already exists
            boolean fileExist = true;
            try {
                // wrap FileReader in a BufferedReader for IO
                FileReader reader = new FileReader(path+newfile);
                BufferedReader bufferedReader = new BufferedReader(reader);

                // close when complete
                bufferedReader.close();
            } catch (Exception ex) {
                fileExist = false;
            }
            if (fileExist) {
                System.out.println("ERROR File already exists: " + newfile);
                System.out.println("Rename Unsuccessful: " + oldfile);
                return;
            }

            // Renames old file to new file
            File oldf = new File(oldfile);
            File newf = new File(path+newfile);
            boolean renamed = oldf.renameTo(newf);
            if (renamed) {
                System.out.println("Rename successful renamed: " + oldfile + " to " + newfile);
            } else {
                System.out.println("Rename unsuccessful: " + oldfile);
                System.out.println("ERROR: Unknown");
            }
        }

        return;
    }

    // Constructs prefix from -prefix block
    static String prefixConstruct(ArrayList<String> prefixes) {
        String retval = "";

        int prefixlen = prefixes.size();
        for (int i = prefixlen -1; i > -1; --i) {
            retval = retval.concat(prefixes.get(i));
        }

        return retval;
    }

    // Constructs suffix from -suffix block
    static String suffixConstruct(ArrayList<String> suffixes) {
        String retval = "";

        int suffixlen = suffixes.size();
        for (int i = 0; i < suffixlen; ++i){
            retval = retval.concat(suffixes.get(i));
        }

        return retval;
    }

    // Build a dictionary of key:value pairs (without the leading "-")
    static LinkedHashMap<String, ArrayList<String>> parse(String[] args) {
        LinkedHashMap<String, ArrayList<String>> arguments = new LinkedHashMap<>();
        String key = null;
        ArrayList<String> value = new ArrayList<String>();
        boolean blockDone = false;
        boolean shouldBreak = false;

        boolean fExists = false;
        boolean pExists = false;
        boolean sExists = false;
        boolean rExists = false;

        int len = args.length;
        // process each argument as either a key or value in the block
        for(int i = 0; i < len; ++i) {

            // Help option
            if (args[i].equals("-h") || args[i].equals("-help")) {

                key = args[i];
                blockDone = true;
                shouldBreak = true;
            // Replace option
            } else if (args[i].equals("-r") || args[i].equals("-replace")) {

                if (rExists){
                    key = "-rExists";
                    blockDone = true;
                } else if (i+2 >= len) {
                    key = "-rNotEnough";
                    int diff = i+3 - len;
                    value.add(Integer.toString(diff));
                    blockDone = true;
                } else if (args[i+1].startsWith("-") || args[i+2].startsWith("-")) {
                    key = "-invalidRguments";
                    blockDone = true;
                } else {
                    key = "-replace";
                    value.add(args[i+1]);
                    value.add(args[i+2]);
                    blockDone = true;
                    i += 2;
                }

            // Filename option
            } else if (args[i].equals("-f") || args[i].equals("-file")) {

                if (fExists){
                    key = "-fExists";
                    blockDone = true;
                } else {
                    key = "-filename";
                    fExists = true;
                }

            // Prefix option
            } else if (args[i].equals("-p") || args[i].equals("-prefix")) {

                if (pExists){
                    key = "-pExists";
                    blockDone = true;
                } else {
                    key = "-prefix";
                    pExists = true;
                }

            // Suffix option
            } else if (args[i].equals("-s") || args[i].equals("-suffix")) {

                if (sExists){
                    key = "-sExists";
                    blockDone = true;
                } else {
                    key = "-suffix";
                    sExists = true;
                }

            //invalid key
            } else if (args[i].startsWith("-")) {

                key = "-invalidInput";
                value.add(args[i]);
                blockDone = true;

            // PFS block values
            } else if (key != null){
                if (args[i].equals("@date")) {
                    value.add(getDate());
                } else if (args[i].equals("@time")) {
                    value.add(getTime());
                } else {
                    value.add(args[i]);
                }
                if ((i+1 == len) || (args[i+1].startsWith("-"))) blockDone = true;

            // arguements passed without option specified
            } else {

                key = "-valSansKey";
                value.add(args[i]);
                blockDone = true;

            }

            if (key != null && blockDone) {
                arguments.put(key, value);
                key = null;
                value = new ArrayList<String>();
                blockDone = false;
            }

            if (shouldBreak == true) {
                break;
            }
        }

        // return arguement blocks
        return arguments;
    }

    // Print help text
    static void printHelp() {
        System.out.println("(c) 2021 Andrew Nguyen. Revised: June 4, 2021.\n" +
                "Usage: rename [-option argument1 argument2 ...]\n" +
                "\n" +
                "Options:\n" +
                "-f|file [filename]          :: file(s) to change.\n" +
                "-p|prefix [string]          :: rename [filename] so that it starts with [string]. \n" +
                "-s|suffix [string]          :: rename [filename] so that it ends with [string]. \n" +
                "-r|replace [str1] [str2]    :: rename [filename] by replacing all instances of [str1] with [str2]. \n" +
                "                            :: recommended to replace after prefix and suffix. \n" +
                "-h|help                     :: print out this help and exit the program.\n");
    }

    // Returns Date in String format
    static String getDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    // Returns Date in String format
    static String getTime() {
        Date time = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
        String strTime = dateFormat.format(time);
        return strTime;
    }

    // Checks if there are any files missing
    static ArrayList<String> filesMissing(ArrayList<String> files){
        ArrayList<String> missingFiles = new ArrayList<String>();
        for (String filename : files) {
            try {
                // wrap FileReader in a BufferedReader for IO
                FileReader reader = new FileReader(filename);
                BufferedReader bufferedReader = new BufferedReader(reader);

                // close when complete
                bufferedReader.close();
            } catch (Exception ex) {
                missingFiles.add(filename);
            }
        }
        return missingFiles;
    }
}