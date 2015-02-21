package us.dahc.goliphant.util.hashing;

import javax.inject.Singleton;

@Singleton
public interface ZobristTableSource {

    public ZobristTable get(int rows, int columns);

}
