/**
 * Copyright (C) 2010
 * Swarm Intelligence Team (SIT)
 * Department of Computer and Systems
 * University of Pernambuco
 * Brazil
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package br.upe.ecomp.doss.problem.df1;

import br.upe.ecomp.doss.util.RandomUtils;

/**
 * Extracted from: A test problem generator for non-stationary environments
 * (Morrison, R.W. De Jong, K.a.)
 * 
 * @author George Moraes
 * 
 */
public class Peak {

    private double hi; // altura do pico
    private double ri; // inclinacao do pico
    private double[] xi; // posicao do pico
    private DFParameters dfParameters;
    private LogisticFunction logisticFunction;

    /**
     * Class constructor.
     * 
     * @param dfParameters Parameters set of DF1 function
     */
    public Peak(DFParameters dfParameters) {
        this.xi = new double[dfParameters.getDimensions()];
        this.dfParameters = dfParameters;
        this.logisticFunction = new LogisticFunction(dfParameters.getDimensions());
        initPeak();
    }

    /**
     * Deve ser chamada apenas uma vez para criar a funcao.
     */
    private void initPeak() {
        hi = dfParameters.getHBase() + RandomUtils.generateUnsignedRandom(dfParameters.getHRange());
        ri = dfParameters.getRBase() + RandomUtils.generateUnsignedRandom(dfParameters.getRRange());

        for (int i = 0; i < xi.length; i++) {
            xi[i] = dfParameters.getXBase() + RandomUtils.generateRandom(dfParameters.getXRange());
        }
    }

    /**
     * Modifica a funcao objetivo de acordo com os parametros que foram
     * escolhidos para serem dinamicos.
     * 
     * @param iteration The current iteration
     */
    public void changePeak(int iteration) {
        if (dfParameters.isDynamicH()) {
            hi = logisticFunction.generateDynamicH(hi, dfParameters.getHBase(), dfParameters.getHRange(),
                    dfParameters.getHScale(), iteration, dfParameters.getAH());
            // System.out.println("Hi: " + hi);
        }
        if (dfParameters.isDynamicR()) {
            ri = logisticFunction.generateDynamicR(ri, dfParameters.getRBase(), dfParameters.getRRange(),
                    dfParameters.getRScale(), iteration, dfParameters.getAR());
        }
        // TODO procurar um modo que a alteracao de do valor de cada dimensao
        // possa ser diferente das demais
        if (dfParameters.isDynamicX()) {
            xi = logisticFunction.generateDynamicX(xi, dfParameters.getXBase(), dfParameters.getXRange(),
                    dfParameters.getXScale(), iteration, dfParameters.getAX());
            // System.out.println("x " + "i" + ": " + xi[0]);
        }
    }

    //
    public Double getFunctionValue(double[] x) {
        double sumPow = 0;
        for (int i = 0; i < x.length; i++) {
            sumPow += Math.pow((x[i] - xi[i]), 2);
        }
        return hi - ri * Math.sqrt(sumPow);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(hi + " - " + ri + " * " + " ");
        for (int i = 0; i < xi.length; i++) {
            sb.append("(" + "x-" + xi[i] + ")");
        }
        return sb.toString();
    }

    public double getHi() {
        return hi;
    }

    public void setHi(double hi) {
        this.hi = hi;
    }

    public double getRi() {
        return ri;
    }

    public void setRi(double ri) {
        this.ri = ri;
    }

    public double[] getXi() {
        return xi;
    }

    public void setXi(double[] xi) {
        this.xi = xi;
    }

    public DFParameters getDfParameters() {
        return dfParameters;
    }

    public void setDfParameters(DFParameters dfParameters) {
        this.dfParameters = dfParameters;
    }
}
