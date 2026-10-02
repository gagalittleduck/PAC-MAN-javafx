package interfaces;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
public interface DirectionMap {
    int DIRECTION_UP = 0;
    int DIRECTION_DOWN = 1;
    int DIRECTION_LEFT = 2;
    int DIRECTION_RIGHT = 3;
    int grid_width = 50;
    int SIMPLE = 1;
    int MIDDLE = 2;
    int HARD = 3;
    public static Color UNIFORM_COLOR = Color.BLACK;
    List<Integer> INVERSE_UP = Arrays.asList(2,3,2,3,1);
    List<Integer> INVERSE_DOWN = Arrays.asList(2,3,2,3,0);
    List<Integer> INVERSE_LEFT = Arrays.asList(0,1,0,1,3);
    List<Integer> INVERSE_RIGHT = Arrays.asList(0,1,0,1,2);

    char[][] HARD_MAP=
            {{ 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W' },
            { 'W', 'K', 'W', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'W', 'C', 'W' },
            { 'W', 'O', '.', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', 'W', 'O', 'W' },
            { 'W', 'W', '.', 'W', 'B', 'W', '.', 'O', 'O', 'O', 'W', 'O', 'W', 'K', 'W' },
            { 'W', 'T', '.', 'W', 'O', 'O', 'O', 'W', 'O', 'C', 'O', 'O', 'W', 'F', 'W' },
            { 'W', 'W', 'W', 'W', 'O', 'W', 'O', 'W', 'O', 'W', 'W', 'W', 'W', 'O', 'W' },
            { 'W', 'O', 'C', 'W', 'W', 'W', 'F', 'W', 'G', 'W', '.', '.', '.', '.', 'W' },
            { 'W', 'O', '.', 'O', 'O', 'W', 'C', 'W', 'W', 'W', '.', 'W', 'W', 'W', 'W' },
            { 'W', 'O', 'W', 'W', 'O', 'W', '.', 'W', 'I', 'W', '.', '.', 'W', 'I', 'W' },
            { 'W', 'O', 'W', '.', 'O', 'W', '.', 'W', '.', 'W', 'W', '.', 'W', '.', 'W' },
            { 'W', 'O', 'W', '.', 'W', 'W', 'O', 'O', '.', '.', '.', '.', 'W', '.', 'W' },
            { 'W', 'O', 'O', '.', 'F', 'W', 'W', '.', 'W', 'W', 'W', '.', 'W', '.', 'W' },
            { 'W', 'O', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'P', 'W' },
            { 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W' }};


    char[][] MIDDLE_MAP={
        {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', },
        {'W', 'I', 'W', '.', '.', '.', '.', '.', '.', '.', 'O', 'C', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'W', 'W', 'W', 'W', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'O', 'O', '.', '.', '.', '.', 'W', 'K', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'W', 'W', 'O', 'W', 'W', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'C', 'O', 'O', 'W', '.', 'O', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'O', 'B', 'W', 'W', '.', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'O', 'W', 'G', '.', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'W', '.', 'W', 'O', 'W', '.', 'W', '.', 'W', 'O', 'W', },
        {'W', '.', 'W', 'C', 'T', '.', '.', '.', 'W', '.', 'W', 'O', 'W', },
        {'W', '.', 'W', 'W', 'W', 'W', 'W', 'W', 'W', '.', '.', '.', 'W', },
        {'W', 'C', '.', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'P', 'F', 'W', },
        {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', }};

    char[][] SIMPLE_MAP= {
        {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', },
        {'W', 'I', 'W', '.', '.', '.', '.', '.', '.', '.', 'O', 'C', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'W', 'W', 'W', 'W', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'O', 'O', '.', '.', '.', '.', 'W', 'B', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'W', 'W', 'O', 'W', 'W', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'O', 'O', 'O', 'W', 'O', 'O', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'T', 'G', 'W', 'W', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'O', 'W', 'F', 'O', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'W', 'C', 'W', 'O', 'W', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'O', 'O', 'O', 'O', 'O', 'W', 'O', 'W', 'O', 'W', },
        {'W', 'O', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'K', '.', '.', 'W', },
        {'W', 'O', '.', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'P', '.', 'W', },
        {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', }};

}
