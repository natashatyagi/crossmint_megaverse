public class Main {
    public static void main(String[] args){
        Main main = new Main();
        main.buildMultiVerse();
    }

    public void buildMultiVerse(){
        APIHandler apiHandler = new APIHandler();
        Utils utils = new Utils();
        String[][] matrix = utils.getGoalMap();
        int row = matrix.length, col = matrix[0].length;
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                String astral = matrix[i][j];
                if(!astral.equals("SPACE")){
                    //check for types and call the api
                    if(astral.equals("POLYANET")){
                        Polyanets polyanetsObj = new Polyanets(ApiConfig.CANDIDATE_ID, i, j);
                        apiHandler.callAPI(polyanetsObj, ApiConfig.POLYANETS, ApiConfig.POST);
                    } else {
                        String[] dirOrColor = astral.split("_");
                        if(dirOrColor[1].equals("COMETH")){
                            //call Comeths api
                            Comeths comethsObj = new Comeths(ApiConfig.CANDIDATE_ID, i, j, dirOrColor[0].toLowerCase());
                            apiHandler.callAPI(comethsObj, ApiConfig.COMETHS, ApiConfig.POST);
                        } else {
                            //call soloons api
                            Soloons soloonsObj = new Soloons(ApiConfig.CANDIDATE_ID, i, j, dirOrColor[0].toLowerCase());
                            apiHandler.callAPI(soloonsObj, ApiConfig.SOLOONS, ApiConfig.POST);
                        }
                    }
                }
            }
        }
    }
}
