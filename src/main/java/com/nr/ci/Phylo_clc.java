package com.nr.ci;
import static java.lang.Math.*;
import org.netlib.util.doubleW;

public class Phylo_clc extends Phylagglom {
  public void premin(final double[][] d, final int[] nextp) {}
  public double dminfn(final double[][] d, final int i, final int j) {return d[i][j];}
  public double dbranchfn(final double[][] d, final int i, final int j) {return 0.5*d[i][j];}
  public double dnewfn(final double[][] d, final int k, final int i, final int j, final int ni, final int nj) {
    return max(d[i][k],d[j][k]);}
  public void drootbranchfn(final double[][] d, final int i, final int j, final int ni, final int nj,
    final doubleW bi, final doubleW bj) {bi.val = bj.val = 0.5*d[i][j];}
  public Phylo_clc(final double[][] dist){
    super(dist);
    makethetree(dist);
  }
}
