public class Comeths extends AstralObject{
    private String direction;
    public Comeths(String candidateId, int row, int column, String direction){
        super(candidateId, row, column);
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
