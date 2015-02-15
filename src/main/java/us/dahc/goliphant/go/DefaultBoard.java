package us.dahc.goliphant.go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBoard implements Board {
    private final int rows;
    private final int columns;
    private Location[][] locations;
    private Map<Location, Color> colors;
    private Map<Location, Group> groups;
    private Map<Location, Group> references;

    public DefaultBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        locations = new Location[rows][columns];
        colors = new HashMap<Location, Color>();
        groups = new HashMap<Location, Group>();
        references = new HashMap<Location, Group>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                locations[i][j] = new Location(i, j);
            }
        }
    }

    public DefaultBoard(DefaultBoard board) {
        rows = board.getRows();
        columns = board.getColumns();
        locations = board.locations;
        colors = new HashMap<Location, Color>(board.colors.size());
        groups = new HashMap<Location, Group>(board.groups.size());
        references = new HashMap<Location, Group>(board.references.size());
        for (Group group : board.references.values()) {
            references.put(group.getReference(), new Group(group));
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board.colors.containsKey(locations[i][j])) {
                    colors.put(locations[i][j], board.colors.get(locations[i][j]));
                    groups.put(locations[i][j], references.get(board.groups.get(locations[i][j]).getReference()));
                }
            }
        }
    }

    public void fastPlay(Move move) {
        Location location = locations[move.getRow()][move.getColumn()];
        for (Location neighbor : location.getNeighbors()) {
            if (colors.get(location) == colors.get(neighbor))
                touchFriend(location, neighbor);
            else
                touchEnemy(location, neighbor);
        }
    }

    public void strictPlay(Move move) throws IllegalMoveException {
    	// TODO: check move validity, including superko trials
        fastPlay(move);
    }

    public List<Move> getLegalMoves(Color player) {
    	// TODO: superko tracking...
        return getLegalMovesIgnoringSuperKo(player);
    }

    public List<Move> getLegalMovesIgnoringSuperKo(Color player) {
        List<Move> result = new ArrayList<Move>(rows * columns);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (!colors.containsKey(locations[i][j]))
                    result.add(new Move(player, i, j));
        return result;
    }

    public Color getColorAt(int row, int column) {
        return colors.get(locations[row][column]);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    private void touchFriend(Location move, Location touched) {
    	// TODO: associate groups and such
    }

    private void touchEnemy(Location move, Location touched) {
    	// TODO: handle potential captures
    }

    public class Group {
        private int pseudoLiberties;
        private List<Location> stones;
        private Location reference;

        public Group(Location location) {
            pseudoLiberties = 0;
            stones = new ArrayList<Location>();
            stones.add(location);
            reference = location;
        }

        public Group(Group group) {
            pseudoLiberties = group.pseudoLiberties;
            stones = new ArrayList<Location>(group.stones);
            reference = group.reference;
        }

        public void add(Location location) {
            stones.add(location);
        }

        public int getPseudoLiberties() {
            return pseudoLiberties;
        }

        public Location getReference() {
            return reference;
        }

        public boolean equals(Group group) {
            return reference.equals(group.reference);
        }
    }

    public class Location {
    	private int row;
    	private int column;
        private List<Location> neighbors;
        private List<Location> diagonals;

        public Location(int row, int column) {
        	this.row = row;
        	this.column = column;
            initGeometry();
        }

        public Location(Move move) {
            this(move.getRow(), move.getColumn());
        }

        public List<Location> getNeighbors() {
            return neighbors;
        }

        public List<Location> getDiagonals() {
            return diagonals;
        }

        private void initGeometry() {
            neighbors = new ArrayList<Location>(4);
            if (north() != null)
                neighbors.add(north());
            if (east() != null)
                neighbors.add(east());
            if (south() != null)
                neighbors.add(south());
            if (west() != null)
                neighbors.add(west());
            if (northeast() != null)
                diagonals.add(northeast());
            if (southeast() != null)
                diagonals.add(southeast());
            if (southwest() != null)
                diagonals.add(southwest());
            if (northwest() != null)
                diagonals.add(northwest());
        }

        private Location north() {
            if (row == 0)
                return null;
            else
                return locations[row - 1][column];
        }

        private Location south() {
            if (row + 1 == rows)
                return null;
            else
                return locations[row + 1][column];
        }

        private Location east() {
            if (column + 1 == columns)
                return null;
            else
                return locations[row][column + 1];
        }

        private Location west() {
            if (column == 0)
                return null;
            else
                return locations[row][column - 1];
        }

        private Location northeast() {
            if (column + 1 == columns || row == 0)
                return null;
            else
                return locations[row - 1][column + 1];
        }

        private Location southeast() {
            if (column + 1 == columns || row + 1 == rows)
                return null;
            else
                return locations[row + 1][column + 1];
        }

        private Location southwest() {
            if (column == 0 || row + 1 == rows)
                return null;
            else
                return locations[row + 1][column - 1];
        }

        private Location northwest() {
            if (column == 0 || row == 0)
                return null;
            else
                return locations[row - 1][column - 1];
        }
    }
}
