package org.avaje.doclet.util;

import com.sun.javadoc.Doc;

/**
 * Interface used to render a Javadoc Doc
 *
 * @author John Ericksen
 */
public interface DocletRenderer {

  void renderDoc(Doc doc);
}
