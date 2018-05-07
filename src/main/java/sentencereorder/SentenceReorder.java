package sentencereorder;

import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;

import java.util.Collections;
import java.util.List;

public class SentenceReorder {
    public static void rearrange(Sentence sentence){
        List<PoSTag> poSTagList = sentence.getPoSTags();
        for (int i = 0; i < poSTagList.size()-1; i++) {
            if( poSTagList.get(i).getTag().equals(Tag.NOUN)) {
                if (poSTagList.get(i + 1).getTag().equals(Tag.ADJ))
                    Collections.swap(poSTagList, i, i + 1);
                else if (i < poSTagList.size() - 2
                        && poSTagList.get(i + 2).getTag().equals(Tag.ADJ)
                        && (poSTagList.get(i + 1).getTag().equals(Tag.CCONJ)
                            ||  poSTagList.get(i + 1).getWord().equals(",")))
                    Collections.swap(poSTagList, i, i + 1);
            }
        }
    }
}
