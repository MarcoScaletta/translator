package SentenceReorder;

import com.taggingTool.PoSTag;
import com.taggingTool.Tag;
import com.utilities.UtilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Dictionary {

    private final HashMap<PoSTag, String> translationMap = new HashMap<>();

    public Dictionary(String filePath) {
        readDictionary(filePath);
    }

    private void readDictionary(String filePath){
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String currentLine;
            while((currentLine = br.readLine()) != null) {
                String[] columns = currentLine.split("/");
                if(columns.length==3)
                    translationMap.put(new PoSTag(columns[0],Tag.valueOf(columns[1])), columns[2]);
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
}
