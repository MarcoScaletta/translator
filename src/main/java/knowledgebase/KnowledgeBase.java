package knowledgebase;

import java.util.HashMap;

public class KnowledgeBase {

    private static HashMap<String,Entity> entityMap = new HashMap<>();

    static{
        entityMap.put("father", Entity.PERSON);
    }

    public static HashMap<String,Entity> getEntityMap() {
        return entityMap;
    }
}
