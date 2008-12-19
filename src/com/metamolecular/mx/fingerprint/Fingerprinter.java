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

  public BitSet getFingerprint(Molecule molecule)
  {
    BitSet result = new BitSet(length);
    Set<String> paths = getPaths(molecule);
    
    for (String path : paths)
    {
      int position = new Random(path.hashCode()).nextInt(length);
      
      result.set(position);
    }

//
//		for (Iterator e = paths.values().iterator(); e.hasNext(); )
//		{
//			path = (String)e.next();
//			position = new java.util.Random(path.hashCode()).nextInt(size);
//			logger.debug("Setting bit " + position + " for " + path);
//			bs.set(position);
//		}
//		return bs;

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
        pathString.append(atom.getSymbol());
      }
      
      result.add(pathString.toString());
    }
    
    return result;
  }
}
