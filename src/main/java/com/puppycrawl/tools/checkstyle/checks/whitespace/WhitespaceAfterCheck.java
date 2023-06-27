///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2023 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.whitespace;

import com.puppycrawl.tools.checkstyle.StatelessCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

/**
 * <p>
 * Checks that a token is followed by whitespace, with the exception that it
 * does not check for whitespace after the semicolon of an empty for iterator.
 * Use Check
 * <a href="https://checkstyle.org/checks/whitespace/emptyforiteratorpad.html">
 * EmptyForIteratorPad</a> to validate empty for iterators.
 * </p>
 * <ul>
 * <li>
 * Property {@code tokens} - tokens to check
 * Type is {@code java.lang.String[]}.
 * Validation type is {@code tokenSet}.
 * Default value is:
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#COMMA">
 * COMMA</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#SEMI">
 * SEMI</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#TYPECAST">
 * TYPECAST</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_IF">
 * LITERAL_IF</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_ELSE">
 * LITERAL_ELSE</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_WHILE">
 * LITERAL_WHILE</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_DO">
 * LITERAL_DO</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_FOR">
 * LITERAL_FOR</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_FINALLY">
 * LITERAL_FINALLY</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_RETURN">
 * LITERAL_RETURN</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_YIELD">
 * LITERAL_YIELD</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_CATCH">
 * LITERAL_CATCH</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_DO">
 * DO_WHILE</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#ELLIPSIS">
 * ELLIPSIS</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_SWITCH">
 * LITERAL_SWITCH</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_SYNCHRONIZED">
 * LITERAL_SYNCHRONIZED</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_TRY">
 * LITERAL_TRY</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LITERAL_CASE">
 * LITERAL_CASE</a>,
 * <a href="https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html#LAMBDA">
 * LAMBDA</a>.
 * </li>
 * </ul>
 * <p>
 * To configure the check:
 * </p>
 * <pre>
 * &lt;module name=&quot;WhitespaceAfter&quot;/&gt;
 * </pre>
 * <p>Example:</p>
 * <pre>
 *  public void myTest() {
 *      if (foo) {              // OK
 *              //...
 *      } else if(bar) {        // violation
 *              //...
 *      }
 *
 *      testMethod(foo, bar);   // OK
 *      testMethod(foo,bar);    // violation
 *
 *      for (;;){}               // OK
 *      for(;;){}                // violation, space after 'for' is required
 *
 *      try (InputStream ignored = System.in) {} // OK
 *      try(InputStream ignored = System.in) {} // violation ''try' is not followed by whitespace'
 *
 *      try {} catch (Exception e){} // OK
 *      try{} catch (Exception e){} // violation ''try' is not followed by whitespace'
 *
 *      try {} finally {} // OK
 *      try {} finally{} // violation ''finally' is not followed by whitespace'
 *
 *      try {} catch (Error e){} finally {} // OK
 *      try {} catch (Error e){} finally{} // violation ''finally' is not followed by whitespace'
 *
 *      try {} catch (Exception e){} // OK
 *      try {} catch(Exception e){} // violation ''catch' is not followed by whitespace'
 *
 *      synchronized (this) { } // OK
 *      synchronized(this) { } // violation ''synchronized' is not followed by whitespace'
 *  }
 *  public String testOne() {
 *      return ("a" + "b"); // OK
 *  }
 *  public String testTwo() {
 *      return("a" + "b"); // violation 'return' is not followed by whitespace'
 *  }
 *  public static void main(String[] args) {
 *    int a = switch (args[0]) {
 *      case "got":
 *        yield (1); // OK
 *      case "my":
 *        yield(3); // violation ''yield' is not followed by whitespace'
 *      default:
 *        yield 2;
 *    };
 *  }
 * </pre>
 * <p>
 * To configure the check for whitespace only after COMMA and SEMI tokens:
 * </p>
 * <pre>
 * &lt;module name=&quot;WhitespaceAfter&quot;&gt;
 *   &lt;property name=&quot;tokens&quot; value=&quot;COMMA, SEMI&quot;/&gt;
 * &lt;/module&gt;
 * </pre>
 * <p>Example:</p>
 * <pre>
 *     public void myTest() {
 *         int a; int b;           // OK
 *         int a;int b;            // violation
 *
 *         testMethod(foo, bar);   // OK
 *         testMethod(foo,bar);    // violation
 *
 *         for(;;) {} // OK
 *     }
 * </pre>
 * <p>
 * Parent is {@code com.puppycrawl.tools.checkstyle.TreeWalker}
 * </p>
 * <p>
 * Violation Message Keys:
 * </p>
 * <ul>
 * <li>
 * {@code ws.notFollowed}
 * </li>
 * <li>
 * {@code ws.typeCast}
 * </li>
 * </ul>
 *
 * @since 3.0
 */
@StatelessCheck
public class WhitespaceAfterCheck
    extends AbstractCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_WS_NOT_FOLLOWED = "ws.notFollowed";

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_WS_TYPECAST = "ws.typeCast";

    @Override
    public int[] getDefaultTokens() {
        return getAcceptableTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] {
            TokenTypes.COMMA,
            TokenTypes.SEMI,
            TokenTypes.TYPECAST,
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_ELSE,
            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_FINALLY,
            TokenTypes.LITERAL_RETURN,
            TokenTypes.LITERAL_YIELD,
            TokenTypes.LITERAL_CATCH,
            TokenTypes.DO_WHILE,
            TokenTypes.ELLIPSIS,
            TokenTypes.LITERAL_SWITCH,
            TokenTypes.LITERAL_SYNCHRONIZED,
            TokenTypes.LITERAL_TRY,
            TokenTypes.LITERAL_CASE,
            TokenTypes.LAMBDA,
        };
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getType() == TokenTypes.TYPECAST) {
            final DetailAST targetAST = ast.findFirstToken(TokenTypes.RPAREN);
            final int[] line = getLineCodePoints(targetAST.getLineNo() - 1);
            if (!isFollowedByWhitespace(targetAST, line)) {
                log(targetAST, MSG_WS_TYPECAST);
            }
        }
        else {
            final int[] line = getLineCodePoints(ast.getLineNo() - 1);
            if (!isFollowedByWhitespace(ast, line)) {
                final Object[] message = {ast.getText()};
                log(ast, MSG_WS_NOT_FOLLOWED, message);
            }
        }
    }

    /**
     * Checks whether token is followed by a whitespace.
     *
     * @param targetAST Ast token.
     * @param line Unicode code points array of line associated with the ast token.
     * @return true if ast token is followed by a whitespace.
     */
    private static boolean isFollowedByWhitespace(DetailAST targetAST, int... line) {
        final int after =
            targetAST.getColumnNo() + targetAST.getText().length();
        boolean followedByWhitespace = true;

        if (after < line.length) {
            final int codePoint = line[after];

            followedByWhitespace = codePoint == ';'
                || codePoint == ')'
                || Character.isWhitespace(codePoint);
        }
        return followedByWhitespace;
    }

}
