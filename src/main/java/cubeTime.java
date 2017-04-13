/**
 * Created by sylentbv on 4/12/2017.
 */
public class cubeTime {

    int ID;
    String itemName;
    double solveTime;

    cubeTime(int i, String s, double t) {
        ID = i;
        itemName = s;
        solveTime = t;
    }

    @Override
    public String toString() {
        return itemName + " solved the Rubix Cube in " + solveTime+ " seconds!";
    }

}
