package com.github.fbascheper.quarkus.zerocode.extension.step;

/**
 * Marker interface for [ZeroCode](https://www.zerocode.io) test step executors.
 *
 * @param <I> input type
 * @param <O> output type
 * @author Erik-Berndt Scheper
 * @since 01-12-2020
 */
public interface ZeroCodeTestStepExecutor<I, O> {

    /**
     * Execute this step
     *
     * @param input input for the step to execute.
     * @return output of the step, eligible for validation
     */
    O execute(I input);

}
