public abstract class AstralObject {
    private String candidateId;
    private int row;
    private int column;

    public AstralObject(String candidateId, int row, int column){
        this.candidateId = candidateId;
        this.row = row;
        this.column = column;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
