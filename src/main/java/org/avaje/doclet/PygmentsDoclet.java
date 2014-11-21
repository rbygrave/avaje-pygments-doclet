package org.avaje.doclet;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import org.avaje.doclet.util.PygmentsRenderer;
import org.avaje.doclet.util.DocletIterator;
import org.avaje.doclet.util.DocletOptions;
import org.avaje.doclet.util.StandardAdapter;
import org.avaje.doclet.util.Stylesheets;

/**
 * PygmentsDoclet
 * <p/>
 * Based on work by John Ericksen.
 */
public class PygmentsDoclet extends Doclet {

  private final RootDoc rootDoc;
  private final DocletOptions docletOptions;
  private final DocletIterator iterator;
  private final Stylesheets stylesheets;

  public PygmentsDoclet(RootDoc rootDoc) {
    this.rootDoc = rootDoc;
    this.docletOptions = new DocletOptions(rootDoc);
    this.iterator = new DocletIterator(docletOptions);
    this.stylesheets = new Stylesheets(docletOptions, rootDoc);
  }

  /**
   * Sets the language version to Java 5.
   * <p/>
   * Javadoc spec requirement.
   *
   * @return language version number
   */
  @SuppressWarnings("UnusedDeclaration")
  public static LanguageVersion languageVersion() {
    return LanguageVersion.JAVA_1_5;
  }

  /**
   * Sets the option length to the standard Javadoc option length.
   * <p/>
   * Javadoc spec requirement.
   *
   * @param option input option
   * @return length of required parameters
   */
  @SuppressWarnings("UnusedDeclaration")
  public static int optionLength(String option) {
    return optionLength(option, new StandardAdapter());
  }

  /**
   * The starting point of Javadoc render.
   * <p/>
   * Javadoc spec requirement.
   *
   * @param rootDoc input class documents
   * @return success
   */
  @SuppressWarnings("UnusedDeclaration")
  public static boolean start(RootDoc rootDoc) {
    return new PygmentsDoclet(rootDoc).start(new StandardAdapter());
  }

  /**
   * Processes the input options by delegating to the standard handler.
   * <p/>
   * Javadoc spec requirement.
   *
   * @param options       input option array
   * @param errorReporter error handling
   * @return success
   */
  @SuppressWarnings("UnusedDeclaration")
  public static boolean validOptions(String[][] options, DocErrorReporter errorReporter) {
    return validOptions(options, errorReporter, new StandardAdapter());
  }

  static int optionLength(String option, StandardAdapter standardDoclet) {
    return DocletOptions.optionLength(option, standardDoclet);
  }

  static boolean validOptions(String[][] options, DocErrorReporter errorReporter, StandardAdapter standardDoclet) {
    return DocletOptions.validOptions(options, errorReporter, standardDoclet);
  }

  boolean start(StandardAdapter standardDoclet) {
    return run(standardDoclet) && postProcess();
  }

  private boolean run(StandardAdapter standardDoclet) {
    PygmentsRenderer renderer = new PygmentsRenderer();
    return iterator.render(rootDoc, renderer) && standardDoclet.start(rootDoc);
  }

  private boolean postProcess() {
    if (docletOptions.stylesheetFile().isPresent()) return true;
    return stylesheets.copy();
  }
}
