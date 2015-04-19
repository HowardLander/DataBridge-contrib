/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.renci.databridge.contrib.similarity.ncat;

/**
 *
 * @author Nerketur
 */
public class Eskin extends Measure {

    @Override
    protected double in(Object dummy) {
        return 1;
    }
    @Override
    protected double notIn(Object dummy) {
        double valuesSqr = values.size() * values.size();
        return valuesSqr / (valuesSqr + 2.0);
    }

}
