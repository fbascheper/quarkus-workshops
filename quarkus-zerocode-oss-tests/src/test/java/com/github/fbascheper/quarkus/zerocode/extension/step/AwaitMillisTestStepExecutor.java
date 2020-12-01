package com.github.fbascheper.quarkus.zerocode.extension.step;

/**
 * A [ZeroCode](https://www.zerocode.io) step executor to await a given time in milliseconds.
 *
 * @author Erik-Berndt Scheper
 * @since 01-12-2020
 */
public class AwaitMillisTestStepExecutor implements ZeroCodeTestStepExecutor<Integer, Boolean> {

    @Override
    public Boolean execute(Integer value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("Interrupted", e);
        }

        return true;
    }
}
