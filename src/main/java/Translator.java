import SentenceReorder.Dictionary;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.utilities.Log;

import java.util.ArrayList;
import java.util.List;

public class Translator {
    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public List<String> translate(Sentence sentence){
        List<String> res = new ArrayList<>();
        String temp;
        if(sentence.getPoSTags().get(0).getTag().equals(Tag.VERB)
                || sentence.getPoSTags().get(0).getTag().equals(Tag.AUX))
            sentence.getPoSTags().add(0, new PoSTag("qualcuno-qualcosa", Tag.valueOf("PRON")));
        for(PoSTag poSTag : sentence.getPoSTags()){
            if(!poSTag.getTag().equals(Tag.PROPN)
                    && !poSTag.getTag().equals(Tag.PUNCT)){
                temp = dictionary.getTranslationMap().get(poSTag);
                if(temp == null)
                    res.add("{NO_TRSL:"+poSTag.getWord()+"}");
                else
                    res.add(temp);
            }else
                res.add(poSTag.getWord());
        }


        return res;
    }

}
