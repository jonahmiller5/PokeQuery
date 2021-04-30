package nets150hw5.businesslogic;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TypeMatchupGraphs {
    private final HashMap<String, HashSet<String>> zeroTimesEffectiveGraph = new HashMap<String, HashSet<String>>();
    private final HashMap<String, HashSet<String>> halfTimesEffectiveGraph = new HashMap<String, HashSet<String>>();
    private final HashMap<String, HashSet<String>> oneTimesEffectiveGraph = new HashMap<String, HashSet<String>>();
    private final HashMap<String, HashSet<String>> twoTimesEffectiveGraph = new HashMap<String, HashSet<String>>();
    
    private final HashMap<String, String> typeURLMap = new HashMap<String, String>();
    
    private final PokeAPICaller caller = new PokeAPICaller();
    private final JsonParser parser = new JsonParser();
    
    public TypeMatchupGraphs() {
        
    }
    
    public void populateTypeURLMap() {
        String typeListJson = "";
        try {
            typeListJson = caller.getJsonResponse("https://pokeapi.co/api/v2/type");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JsonElement wholeElement = parser.parse(typeListJson);
        JsonObject wholeObject = wholeElement.getAsJsonObject();
        JsonArray elementArray = wholeObject.get("results").getAsJsonArray();
        for (JsonElement j : elementArray) {
            JsonObject currentType = j.getAsJsonObject();
            if (currentType.get("name").getAsString().equals("shadow") || currentType.get("name").getAsString().equals("unknown")) continue;
            typeURLMap.put(currentType.get("name").getAsString(), currentType.get("url").getAsString());
        }
    }
    
    public void populateZeroTimes() {
        String typeInfoJson = "";
        for (String type : typeURLMap.keySet()) {
            HashSet<String> currentSet = new HashSet<String>();
            try {
                typeInfoJson = caller.getJsonResponse(typeURLMap.get(type));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JsonArray elementArray = parser.parse(typeInfoJson)
                    .getAsJsonObject()
                    .get("damage_relations")
                    .getAsJsonObject()
                    .get("no_damage_to")
                    .getAsJsonArray();
            
            for (JsonElement elt : elementArray) {
                JsonElement nameElement = elt.getAsJsonObject().get("name");
                currentSet.add(nameElement.getAsString());
            }
            
            zeroTimesEffectiveGraph.put(type, currentSet);
        }
        
        for (Map.Entry<String, HashSet<String>> entry : zeroTimesEffectiveGraph.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (String st : entry.getValue()) {
                System.out.println(st);
            }
        }
    }
    
    public void populateHalfTimes() {
        String typeInfoJson = "";
        for (String type : typeURLMap.keySet()) {
            HashSet<String> currentSet = new HashSet<String>();
            try {
                typeInfoJson = caller.getJsonResponse(typeURLMap.get(type));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JsonArray elementArray = parser.parse(typeInfoJson)
                    .getAsJsonObject()
                    .get("damage_relations")
                    .getAsJsonObject()
                    .get("half_damage_to")
                    .getAsJsonArray();
            
            for (JsonElement elt : elementArray) {
                JsonElement nameElement = elt.getAsJsonObject().get("name");
                currentSet.add(nameElement.getAsString());
            }
            
            halfTimesEffectiveGraph.put(type, currentSet);
        }
        
        for (Map.Entry<String, HashSet<String>> entry : halfTimesEffectiveGraph.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (String st : entry.getValue()) {
                System.out.println(st);
            }
        }
    }
    
    public void populateTwoTimes() {
        String typeInfoJson = "";
        for (String type : typeURLMap.keySet()) {
            HashSet<String> currentSet = new HashSet<String>();
            try {
                typeInfoJson = caller.getJsonResponse(typeURLMap.get(type));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JsonArray elementArray = parser.parse(typeInfoJson)
                    .getAsJsonObject()
                    .get("damage_relations")
                    .getAsJsonObject()
                    .get("double_damage_to")
                    .getAsJsonArray();
            
            for (JsonElement elt : elementArray) {
                JsonElement nameElement = elt.getAsJsonObject().get("name");
                currentSet.add(nameElement.getAsString());
            }
            
            twoTimesEffectiveGraph.put(type, currentSet);
        }
        
        for (Map.Entry<String, HashSet<String>> entry : twoTimesEffectiveGraph.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (String st : entry.getValue()) {
                System.out.println(st);
            }
        }
    }
    
    public void populateOneTimes() {
        for (String type : typeURLMap.keySet()) {
            HashSet<String> currentSet = (HashSet<String>) typeURLMap.keySet();
            for (String zeroS : zeroTimesEffectiveGraph.get(type)) {
                currentSet.remove(zeroS);
            }
            for (String halfS : halfTimesEffectiveGraph.get(type)) {
                currentSet.remove(halfS);
            }
            for (String doubleS : twoTimesEffectiveGraph.get(type)) {
                currentSet.remove(doubleS);
            }
            
        }
    }
    
    public static void main(String[] args) {
        TypeMatchupGraphs matchups = new TypeMatchupGraphs();
        matchups.populateTypeURLMap();
        matchups.populateTwoTimes();
    }
}
