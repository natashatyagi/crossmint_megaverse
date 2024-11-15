public class Soloons extends AstralObject{
    private String color;
    public Soloons(String candidateId, int row, int column, String color){
        super(candidateId, row, column);
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
