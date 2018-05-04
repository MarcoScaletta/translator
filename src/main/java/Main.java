import SentenceReorder.SentenceReorder;
import com.poSTagger.Viterbi;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.TreeBankReader;
import SentenceReorder.Dictionary;
import com.treeBankReader.WordConverter;
import com.utilities.Log;

import java.util.Arrays;


public class Main {

    private static final String TREE_BANK_TRAINING = "../viterbi/training.txt";

    private static final String[] SENTENCES = {
            "È la spada-laser di tuo padre",
            "Ha fatto una mossa leale",
            "Gli ultimi avanzi della vecchia Repubblica sono stati spazzati via"};

    public static void main(String []args){
        translationTest();
    }

    private static void translationTest(){

        Sentence sentence;

        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING);
        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB};

        Viterbi viterbi = new Viterbi(treeBankReader,nounAdjVerbPropn);

        Translator translator = new Translator(new Dictionary("dictionary.txt"));

        for(String sentenceString : SENTENCES){
            System.out.println();
            System.out.println("Traduco la frase \""+sentenceString+"\"");
            sentence = viterbi.poSTagging(WordConverter.formatStringSentence(sentenceString.split(" ")));
            System.out.print("Il PoSTagging e': ");
            for(PoSTag poSTag : sentence.getPoSTags()){
                System.out.print("["+poSTag.getWord()+" "+poSTag.getTag()+"]");
            }
            System.out.println();
            System.out.println("La traduzione e'");
            SentenceReorder.rearrange(sentence);
            System.out.print("\"");
            for(String string : translator.translate(sentence))
                System.out.print(string + " ");
            System.out.println("\"");
        }
    }
}
