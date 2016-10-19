package eu.vilaca.remote;

public class MultImpl implements Mult {
    @Override
    public long calc(int i, int j) {
        return i * j;
    }
}