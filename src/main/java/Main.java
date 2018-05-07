import sentencereorder.SentenceReorder;
import com.poSTagger.Viterbi;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.TreeBankReader;
import tools.Dictionary;
import com.treeBankReader.WordConverter;
import translator.Translator;


public class Main {

    private static final String TREE_BANK_TRAINING = "../viterbi/training.txt";

    private static final String[] SENTENCES = {
            "È la spada laser di tuo padre",
            "Ha fatto una mossa leale",
            "Gli ultimi avanzi della vecchia repubblica sono stati spazzati via"};

    public static void main(String []args){
        translationTest();
    }

    private static void translationTest(){

        Sentence sentence;

        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING);
        treeBankReader.getPoSTagNums().put(new PoSTag("laser", Tag.ADJ), (long)1);
        treeBankReader.getPoSTagPerWordNums().get("laser");
        treeBankReader.getPoSTagPerWordNums()
                .put("laser",treeBankReader.getPoSTagPerWordNums().get("laser")+1);
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
            for(PoSTag poSTag : translator.translate(sentence).getPoSTags())
                System.out.print(poSTag.getWord() + " ");
            System.out.println("\"");
        }
    }
}
