package translator;

import knowledgebase.KnowledgeBase;
import tools.Dictionary;
import knowledgebase.Entity;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class implements methods for the translation
 */
public class Translator {

    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Method that performs translation
     * @param sentence sentence to be translated
     * @return a translated sentence
     */
    public Sentence translate(Sentence sentence){
        Sentence result = new Sentence();
        List<PoSTag> res = new ArrayList<>();
        String temp;
        PoSTag poSTag;

        composite(sentence);
        List<PoSTag> poSTags = sentence.getPoSTags();

        if(poSTags.get(0).getTag().equals(Tag.VERB)
                || poSTags.get(0).getTag().equals(Tag.AUX))
            poSTags.add(0, new PoSTag("qualcuno-qualcosa", Tag.valueOf("PRON")));
        for (int i = 0; i < poSTags.size(); i++) {
            poSTag = poSTags.get(i);
            if(!poSTag.getTag().equals(Tag.PROPN)
                    && !poSTag.getTag().equals(Tag.PUNCT)){
                if(poSTag.getTag().equals(Tag.AUX)
                        && i < poSTags.size()-1
                        && (!poSTags.get(i+1).getTag().equals(Tag.AUX)&&
                        !poSTags.get(i+1).getTag().equals(Tag.VERB)))
                    temp = dictionary.getTranslationMap().get(new PoSTag(poSTag.getWord(), Tag.VERB));
                else{
                    temp = dictionary.getTranslationMap().get(poSTag);

                }
                if(temp == null)
                    res.add(poSTag);
                else
                    res.add(new PoSTag(temp, poSTag.getTag()));

            }else
                res.add(poSTag);
        }
        result.getPoSTags().addAll(res);
        grammarRule(result);
        possessive(result);
        subject(result);
        upperCase(result);

        return result;
    }

    /**
     * Applies grammar rules to the sentence
     * @param sentence sentence to be modified
     */
    private void grammarRule(Sentence sentence){
        List<PoSTag> poSTags = sentence.getPoSTags();
        for (int i = 0; i < poSTags.size(); i++) {


            if(i < poSTags.size()-1
                    && (poSTags.get(i).equals(new PoSTag("do", Tag.AUX))
                    ||  poSTags.get(i).equals(new PoSTag("did", Tag.AUX)))
                    && dictionary.getIrregularVerb().contains(poSTags.get(i+1))){
                poSTags.remove(i);
            }
        }
    }

    /**
     * Applies possessive rule to the sentence
     * @param sentence sentence to be modified
     */
    private void possessive(Sentence sentence){
        List<PoSTag> poSTags = sentence.getPoSTags();
        List<PoSTag> toShift;
        PoSTag temp;
        boolean stop;
        boolean metNoun;
        int lastDetIndex = -1;
        for (int i = 0; i < poSTags.size(); i++) {
            stop = false;
            metNoun = false;
            toShift = new ArrayList<>();
            if(poSTags.get(i).getTag().equals(Tag.DET))
                lastDetIndex = i;
            if(poSTags.get(i).equals(new PoSTag("of", Tag.ADP))){
                while(!stop && i < poSTags.size()-1){
                    temp = poSTags.get(i+1);
                    if((temp.getTag().equals(Tag.NOUN)
                            ||  temp.getTag().equals(Tag.PROPN))){
                        if(!KnowledgeBase.getEntityMap().containsKey(temp.getWord())
                                || !KnowledgeBase.getEntityMap().get(temp.getWord()).equals(Entity.PERSON)
                                && !KnowledgeBase.getEntityMap().get(temp.getWord()).equals(Entity.ANIMAL)){
                            poSTags.addAll(i+1,toShift);
                            toShift.clear();
                            stop=true;
                        }
                        if(!metNoun)
                            metNoun=true;
                    }else if(metNoun)
                        stop=true;
                    if(!stop){
                        toShift.add(poSTags.remove(i+1));
                    }
                }
                if(!toShift.isEmpty()){
                    poSTags.remove(i);
                    toShift.add(new PoSTag("'s", Tag.PART));
                    poSTags.remove(lastDetIndex);
                    poSTags.addAll(lastDetIndex, toShift);
                }
            }
        }

    }

    /**
     * Insert the implicit subject
     * @param sentence sentence to be modified
     */
    private void subject(Sentence sentence){
        List<PoSTag> poSTags = sentence.getPoSTags();
        for (int i = 0; i < poSTags.size(); i++) {
            if(poSTags.get(i).getWord().equals("someone-something") && i < poSTags.size()-1){
                poSTags.remove(i);
                switch (poSTags.get(i).getWord()){
                    case "is":
                        poSTags.add(i, new PoSTag("It", Tag.PRON));
                        break;
                    case "made":
                        poSTags.add(i, new PoSTag("He", Tag.PRON));
                        break;
                }

            }
        }
    }

    /**
     * Set to uppercase PROPN and the first word of the sentence, or subsentences
     * @param sentence sentence to be modified
     */
    private void upperCase(Sentence sentence){
        List<PoSTag> poSTags = sentence.getPoSTags();
        boolean beginSentence = true;
        StringBuilder stringBuilder;
        String temp;
        for (int i = 0; i < poSTags.size(); i++) {
            if(beginSentence || poSTags.get(i).getTag().equals(Tag.PROPN)) {
                temp = poSTags.get(i).getWord();
                stringBuilder = new StringBuilder(temp);
                stringBuilder.setCharAt(0,Character.toUpperCase(temp.charAt(0)));
                poSTags.add(0, new PoSTag(stringBuilder.toString(), poSTags.get(i).getTag()));
                poSTags.remove(1);
                beginSentence = false;
            }else if(poSTags.get(i).getWord().equals(".")
                    || poSTags.get(i).getWord().equals("?")
                    || poSTags.get(i).getWord().equals("!")) {
                beginSentence = true;
            }
        }
    }

    /**
     * Method that substitutes composite words like "word_1 word_2" with "word_1-word_2"
     * @param sentence sentence o be modified
     */
    private void composite(Sentence sentence){
        int index;
        PoSTag substitute;
        for (List<PoSTag> list :  dictionary.getCompositeWords().keySet()){
            index = Collections.indexOfSubList(sentence.getPoSTags(), list);
            if(index > -1){
                substitute = dictionary.getCompositeWords().get(list);
                for (int i=0; i<list.size();i++){
                    sentence.getPoSTags().remove(index);
                }
                sentence.getPoSTags().add(index,substitute);
            }
        }
    }
}
