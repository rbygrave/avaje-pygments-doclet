package org.avaje.doclet.util;

import org.python.util.PythonInterpreter;

public class SyntaxHighlighter {

  protected final PythonInterpreter interpreter = new PythonInterpreter();

  protected final String defaultLanguage;

  protected final String startToken = "<pre>{@code";

  protected final String endToken = "}</pre>";

  public SyntaxHighlighter() {
    this("java");
  }

  public SyntaxHighlighter(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  /**
   * Renders the method.
   * <p/>
   * Not sure what goes here
   *
   * <pre>{@code [java]
   * {@literal @}Test
   * public void testCleanInput(){
   *
   *   List<Order> orders = repo.findOrders();
   *    assertEquals("test1\ntest2", PygmentsRenderer.cleanJavadocInput("  test1\n test2\n"));
   *    assertEquals("@", PygmentsRenderer.cleanJavadocInput("{@literal @}"));
   *    assertEquals("test", PygmentsRenderer.cleanJavadocInput("test"));
   *    assertEquals("&#64;", PygmentsRenderer.cleanJavadocInput("{at}"));
   *    assertEquals("/", PygmentsRenderer.cleanJavadocInput("{slash}"));
   * }
   * }</pre>
   */
  public String filter(String content) {

    int start = content.indexOf(startToken);
    if (start == -1) {
      return content;
    }

    int languageStartPos = start + startToken.length();
    int endOfStartLine = content.indexOf('\n', languageStartPos);
    if (endOfStartLine == -1) {
      return content;
    }

    int end = content.indexOf(endToken, endOfStartLine);
    if (end == -1) {
      // didn't find the end token so just skip/ignore
      return content;
    }

    String language = determineLanguage(content, languageStartPos, endOfStartLine);

    StringBuilder buffer = new StringBuilder(content.length() + 1000);

    String rawSource = content.substring(endOfStartLine + 1, end);
    String highlightedSource = formatSource(language, rawSource);

    buffer.append(content.substring(0, start));
    buffer.append(highlightedSource);

    String remainder = content.substring(end + endToken.length());
    buffer.append(filter(remainder));
    return buffer.toString();
  }


  private String determineLanguage(String content, int languageStartPos, int endOfStartLine) {

    String restOfLine = content.substring(languageStartPos, endOfStartLine).trim();
    restOfLine = restOfLine.trim();
    if (restOfLine.startsWith("[") && restOfLine.endsWith("]")) {
      return restOfLine.substring(1, restOfLine.length()-1).toLowerCase();
    }
    return defaultLanguage;
  }


  private String formatSource(String language, String source) {
    if (language.equals("java")) {
      return formatCode(source, "JavaLexer", "pygments.lexers.jvm", "java");

    } else if (language.equals("groovy")) {
      return formatCode(source, "GroovyLexer", "pygments.lexers.jvm", "groovy");

    } else if (language.equals("scala")) {
      return formatCode(source, "ScalaLexer", "pygments.lexers.jvm", "scala");

    } else if (language.equals("sql")) {
      return formatCode(source, "SqlLexer", "pygments.lexers.sql", "sql");

    } else if (language.equals("xml")) {
      return formatCode(source, "XmlLexer", "pygments.lexers.web", "xml");

    } else if (language.equals("properties")) {
      return formatCode(source, "PropertiesLexer", "pygments.lexers.text", "properties");

    } else if (language.equals("sh")) {
      return formatCode(source, "BashLexer", "pygments.lexers.shell", "sh");

    } else if (language.equals("javascript")) {
      return formatCode(source, "JavascriptLexer", "pygments.lexers.web", "javascript");
    }

//    if (language.equals("json")) {
//      return formatCode(source, "JsonLexer", "pygments.lexers.web", "json");
//    }

    return source;
  }

  private String formatCode(String code, String lexer, String lexerPackage, String language) {

    // Set a variable with the content you want to work with
    interpreter.set("code", code);

    String fromClause = "from " + lexerPackage + " import " + lexer + "\n";

    interpreter.exec("from pygments import highlight\n" + fromClause
        + "from pygments.formatters import HtmlFormatter\n"
        + "\nresult = highlight(code, " + lexer + "(), HtmlFormatter())");

    // Get the result that has been set in a variable
    String codeHighlighted = interpreter.get("result", String.class);

    return "<div class=\"syntax " + language + "\">" + codeHighlighted + "</div>";
  }

}
