import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public String[][] getGoalMap(){
        APIHandler apiHandler = new APIHandler();
        String response = apiHandler.callAPI(null, ApiConfig.GOAL, ApiConfig.GET);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        // Retrieve the "goal" property as a JsonArray
        JsonArray matrixArray = jsonObject.getAsJsonArray("goal");

        // Convert the JsonArray to a String[][] (2D matrix)
        List<List<String>> goalMatrix = gson.fromJson(matrixArray, List.class);
        int row = goalMatrix.size(); int col = goalMatrix.get(0).size();
        String[][] matrix = new String[row][col];
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                matrix[i][j] = goalMatrix.get(i).get(j);
            }
        }
        return matrix;

    }

    public <T extends AstralObject> Map<String, Object> buildJsonMap(T astralObj, String endpoint){
        Map<String, Object> jsonMap = new HashMap<>();

        // Add common properties for all AstralObject types
        jsonMap.put("candidateId", astralObj.getCandidateId());
        jsonMap.put("row", astralObj.getRow());
        jsonMap.put("column", astralObj.getColumn());

        // Add specific properties based on the endpoint
        switch (endpoint) {
            case ApiConfig.SOLOONS:
                jsonMap.put("color", ((Soloons) astralObj).getColor());
                break;
            case ApiConfig.COMETHS:
                jsonMap.put("direction", ((Comeths) astralObj).getDirection());
                break;
            // No additional properties for Polyanets
            default:
                break;
        }

        return jsonMap;
    }
}
