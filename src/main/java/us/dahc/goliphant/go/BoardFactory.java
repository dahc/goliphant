package us.dahc.goliphant.go;

public interface BoardFactory {

    public Board create(int rows, int columns);

    public Board copy(Board board);

}
