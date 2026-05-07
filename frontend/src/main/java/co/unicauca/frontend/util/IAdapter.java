package co.unicauca.frontend.util;

public interface IAdapter<S, T> {
    T adapt(S source);
}