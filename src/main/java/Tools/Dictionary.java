package tools;

import com.taggingTool.PoSTag;
import com.taggingTool.Tag;
import com.utilities.UtilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Dictionary {

    private final HashMap<PoSTag, String> translationMap = new HashMap<>();
    private final HashMap<PoSTag, HashSet<String>> subject = new HashMap<>();
    private final HashSet<PoSTag> irregularVerb = new HashSet<>();

    public Dictionary(String filePath) {
        readDictionary(filePath);
    }

    private void readDictionary(String filePath){
        FileReader fr = null;
        BufferedReader br = null;
        PoSTag poSTag;

        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String currentLine;
            while((currentLine = br.readLine()) != null) {
                String[] columns = currentLine.split("/");

                if(columns.length==5){
                    poSTag = new PoSTag(columns[0],Tag.valueOf(columns[1]));
                    translationMap.put(poSTag, columns[2]);
                    if(poSTag.getTag().equals(Tag.VERB) && columns[3].equals("IRR")){
                        irregularVerb.add(new PoSTag(columns[2], Tag.valueOf(columns[1])));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            UtilitiesIO.closeFile(fr, br);
        }
    }

    public HashMap<PoSTag, String> getTranslationMap() {
        return translationMap;
    }

    public HashSet<PoSTag> getIrregularVerb() {
        return irregularVerb;
    }
}
