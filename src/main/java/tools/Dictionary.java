package tools;

import com.taggingTool.PoSTag;
import com.taggingTool.Tag;
import com.utilities.UtilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * It's the object that contains information to be used for the translation
 */
public class Dictionary {

    private final HashMap<PoSTag, String> translationMap = new HashMap<>();
    private final HashMap<PoSTag, HashSet<String>> subject = new HashMap<>();
    private final HashSet<PoSTag> irregularVerb = new HashSet<>();
    private final HashMap<List<PoSTag>,PoSTag> compositeWords = new HashMap<>();

    public Dictionary(String filePath) {

        readDictionary(filePath);
        PoSTag[] compositePoSTags = new PoSTag[]{
                new PoSTag("spada",Tag.NOUN),
                new PoSTag("laser", Tag.NOUN)};
        compositeWords.put(Arrays.asList(compositePoSTags),
                new PoSTag("spada-laser", Tag.NOUN));
        compositePoSTags = new PoSTag[]{
                new PoSTag("spada",Tag.NOUN),
                new PoSTag("laser", Tag.ADJ)};
        compositeWords.put(Arrays.asList(compositePoSTags),
                new PoSTag("spada-laser", Tag.NOUN));
    }

    /**
     * This method populate the dictionary
     * @param filePath file of dictionary (ITA_WORD/POS_TAG/ENG_WORD/IRR(opt))
     */
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
                if(columns.length==3){
                    poSTag = new PoSTag(columns[0],Tag.valueOf(columns[1]));
                    translationMap.put(poSTag, columns[2]);
                }else if(columns.length==4){
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

    public HashMap<List<PoSTag>, PoSTag> getCompositeWords() {
        return compositeWords;
    }
}
