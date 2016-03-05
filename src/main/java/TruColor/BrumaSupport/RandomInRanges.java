package TruColor.BrumaSupport;

/**
 * Created by Manuel on 09/12/2015.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInRanges
{
    private final List<Integer> range = new ArrayList<>();

    public RandomInRanges(int min, int max)
    {
        this.addRange(min, max);
    }

    final public void addRange(int min, int max)
    {
        for(int i = min; i <= max; i++)
        {
            this.range.add(i);
        }
    }

    public int getRandom()
    {
        return this.range.get(new Random().nextInt(this.range.size()));
    }

    public static void main(String[] args)
    {
        RandomInRanges rir = new RandomInRanges(1, 10);
        rir.addRange(50, 60);
        System.out.println(rir.getRandom());
    }
}