/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metamolecular.mx.fingerprint;

import com.metamolecular.mx.model.Atom;
import com.metamolecular.mx.model.Molecule;
import com.metamolecular.mx.path.PathFinder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Richard L. Apodaca <rapodaca at metamolecular.com>
 */
public class Fingerprinter
{
  private int length;
  private PathFinder pathFinder;

  public Fingerprinter()
  {
    this.length = 1024;
    this.pathFinder = new PathFinder();
    
    pathFinder.setMaximumDepth(6);
  }
  
  public void setMaximumPathDepth(int maxDepth)
  {
    pathFinder.setMaximumDepth(maxDepth);
  }
  
  public int getMaximumPathDepth()
  {
    return pathFinder.getMaximumDepth();
  }

  public BitSet getFingerprint(Molecule molecule)
  {
    BitSet result = new BitSet(length);
    Set<String> paths = getPaths(molecule);
    
    for (String path : paths)
    {
      int position = new Random(path.hashCode()).nextInt(length);
      
      result.set(position);
    }

    return result;
  }

  private Set<String> getPaths(Molecule molecule)
  {
    List<List<Atom>> paths = new ArrayList();
    
    for (int i = 0; i < molecule.countAtoms(); i++)
    {
      pathFinder.findAllPaths(molecule.getAtom(i), paths);
    }
    
    return compilePaths(paths);
  }
  
  private Set<String> compilePaths(List<List<Atom>> paths)
  {
    Set<String> result = new HashSet();
    
    for (List<Atom> path : paths)
    {
      StringBuffer pathString = new StringBuffer();
      
      for (Atom atom : path)
      {
        String key = atom.getSymbol();
        int unsaturation = atom.getValence() - atom.countNeighbors();
        
        if (unsaturation > 0)
        {
          key += unsaturation;
        }
        
        pathString.append(key);
      }
      
      result.add(pathString.toString());
    }
    
    return result;
  }
}
