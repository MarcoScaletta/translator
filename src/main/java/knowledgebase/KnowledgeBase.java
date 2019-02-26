package knowledgebase;

import java.util.HashMap;

/**
 * A simple Knowledge Base
 */
public class KnowledgeBase {

    private static HashMap<String, knowledgebase.Entity> entityMap = new HashMap<>();

    static{
        entityMap.put("father", knowledgebase.Entity.PERSON);
    }

    public static HashMap<String, knowledgebase.Entity> getEntityMap() {
        return entityMap;
    }
}
