package me.braydon.mc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple tuple utility.
 *
 * @author Braydon
 */
@NoArgsConstructor @AllArgsConstructor @Setter @Getter
public class Tuple<L, R> {
    /**
     * The left value.
     */
    private L left;

    /**
     * The right value.
     */
    private R right;
}