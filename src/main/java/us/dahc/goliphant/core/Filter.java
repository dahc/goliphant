package us.dahc.goliphant.core;

public interface Filter {

    public boolean accept(Board board, Move move);

}
