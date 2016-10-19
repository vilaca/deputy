package eu.vilaca.remote;

public class SumImpl implements Sum {
    @Override
    public long calc(int i, int j) {
        return i + j;
    }
}