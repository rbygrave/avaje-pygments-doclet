package org.avaje.doclet.util;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;

/**
 * Renders with syntax ....
 * <p/>
 * <pre>{@code [java]
 * <p/>
 *  @Test
 *  public void testCleanInput(){
 * <p/>
 *    assertEquals("test1\ntest2", cleanJavadocInput("  test1\n test2\n"));
 *    assertEquals("@", cleanJavadocInput("{@literal @}"));
 *    assertEquals("test", cleanJavadocInput("test"));
 *    assertEquals("&#64;", cleanJavadocInput("{at}"));
 *    assertEquals("/", cleanJavadocInput("{slash}"));
 *  }
 * <p/>
 * }</pre>
 * <p>
 * Some SQL example below:
 * </p>
 * <pre>{@code [SQL]
 *  -- select all the columns
 *  select *
 *  from m_order o
 *  where o.id = ?
 *  }</pre>
 */
public class PygmentsRenderer implements DocletRenderer {


  private final SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter();

  public PygmentsRenderer() {
  }

  /**
   * Renders a generic document (class, field, method, etc)
   */
  @Override
  public void renderDoc(Doc doc) {
    // hide text that looks like tags (such as annotations in source code) from Javadoc
    String rawCommentText = doc.getRawCommentText().replaceAll("@([A-Z])", "{@literal @}$1");
    doc.setRawCommentText(rawCommentText);

    StringBuilder buffer = new StringBuilder();
    buffer.append(render(doc.commentText()));
    buffer.append('\n');
    for (Tag tag : doc.tags()) {
      renderTag(tag, buffer);
      buffer.append('\n');
    }
    doc.setRawCommentText(buffer.toString());
  }

  /**
   * Renders a document tag in the standard way.
   *
   * @param tag    input
   * @param buffer output buffer
   */
  private void renderTag(Tag tag, StringBuilder buffer) {
    //print out directly
    buffer.append(tag.name());
    buffer.append(" ");
    buffer.append(render(tag.text()));
  }

  private String render(String input) {
    return syntaxHighlighter.filter(cleanJavadocInput(input));
  }

  protected static String cleanJavadocInput(String input) {
    return input.trim()
        .replaceAll("\n ", "\n") // Newline space to accommodate javadoc newlines.
        .replaceAll("\\{at}", "&#64;") // {at} is translated into @.
        .replaceAll("\\{slash}", "/") // {slash} is translated into /.
        .replaceAll("(?m)^( *)\\*\\\\/$", "$1*/") // Multi-line comment end tag is translated into */.
        .replaceAll("\\{@literal (.*?)}", "$1"); // {@literal _} is translated into _ (standard javadoc).
  }
}
