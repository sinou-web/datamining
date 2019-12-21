package KNN;

import java.util.Comparator;

/**
 * Created by masterubunto on 27/11/18.
 */
class Sortbyroll implements Comparator<Element>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Element a, Element b)
    {
        return String.valueOf (a.Distancei).compareTo (String.valueOf (  b.Distancei));
    }
}
