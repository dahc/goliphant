package us.dahc.goliphant.go;

public interface ZobristTable {
    public int getRows();
    public int getColumns();
    public long getEntry(Color color, int row, int column);
}
