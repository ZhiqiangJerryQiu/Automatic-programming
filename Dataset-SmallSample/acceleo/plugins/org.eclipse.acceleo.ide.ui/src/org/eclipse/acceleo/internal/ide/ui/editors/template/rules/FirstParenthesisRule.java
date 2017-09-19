/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.acceleo.internal.ide.ui.editors.template.rules;

import org.eclipse.acceleo.common.IAcceleoConstants;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * A rule to detect the first parenthesis of a template, query or macro. This one needs specific handling as
 * it will be preceded by the template/query/macro name, an optional visibility, and the block name.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FirstParenthesisRule implements ISequenceRule {
	/** This will match any word. */
	private static final String ANY_WORD = "*"; //$NON-NLS-1$

	/**
	 * Name of the block we're currently scanning. Should be either {@link IAcceleoConstants#TEMPLATE},
	 * {@link IAcceleoConstants#MACRO} or {@link IAcceleoConstants#QUERY}.
	 */
	private String blockType;

	/** A token to be returned by the rule. */
	private final IToken token;

	/**
	 * Constructor.
	 * 
	 * @param blockType
	 *            Name of the block we're currently scanning. Should be either
	 *            {@link IAcceleoConstants#TEMPLATE}, {@link IAcceleoConstants#MACRO} or
	 *            {@link IAcceleoConstants#QUERY}.
	 * @param token
	 *            is the token to use for this rule
	 */
	public FirstParenthesisRule(String blockType, IToken token) {
		this.blockType = blockType;
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	public IToken getSuccessToken() {
		return token;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		if (read(scanner) > 0) {
			return token;
		}
		return Token.UNDEFINED;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.acceleo.ide.ui.editors.template.rules.ISequenceRule#read(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public int read(ICharacterScanner scanner) {
		int c = scanner.read();
		scanner.unread();
		int shift = 1;

		if (c == '(' && validateLocation(scanner)) {
			scanner.read();
		} else {
			shift--;
		}
		return shift;
	}

	/**
	 * Validates that we are currently on a parenthesis, and that this parenthesis is preceded by the sequence
	 * &lt;blockType&gt; &lt;visibility&gt; &lt;*&gt;.
	 * 
	 * @param scanner
	 *            is the scanner
	 * @return true if the previous words are valid
	 */
	private boolean validateLocation(ICharacterScanner scanner) {
		boolean valid = true;
		int shift = 0;

		// The first preceding word is the template/query/macro name
		int n = unreadPreviousWord(scanner, ANY_WORD);
		if (n == 0) {
			valid = false;
		} else {
			shift += n;
		}

		// The second preceding word is the block's visibility if any
		if (valid) {
			n = unreadVisibility(scanner);
			shift += n;

			// Whether we had a visibility or not, the next word is the block type.
			n = unreadPreviousWord(scanner, blockType);
			if (n == 0) {
				valid = false;
			} else {
				shift += n;
			}
		}

		while (shift < 0) {
			scanner.read();
			shift++;
		}
		return valid;
	}

	/**
	 * Rewinds the scanner before the previous word if it correspond to a visibility (
	 * {@link IAcceleoConstants#VISIBILITY_KIND_PUBLIC}, {@link IAcceleoConstants#VISIBILITY_KIND_PROTECTED}
	 * or {@link IAcceleoConstants#VISIBILITY_KIND_PRIVATE}).
	 * 
	 * @param scanner
	 *            The current scanner.
	 * @return The number of examined characters (less than 0 if the word matches).
	 */
	private int unreadVisibility(ICharacterScanner scanner) {
		int shift = 0;

		// Ignore white spaces
		int c;
		do {
			c = unreadChar(scanner);
			shift--;
		} while (Character.isWhitespace(c));

		// Unread the preceding word
		String word = ""; //$NON-NLS-1$
		while (c != ICharacterScanner.EOF && Character.isJavaIdentifierPart(c)) {
			word = Character.toString((char)c) + word;
			c = unreadChar(scanner);
			shift--;
		}
		if (!IAcceleoConstants.VISIBILITY_KIND_PUBLIC.equals(word)
				&& !IAcceleoConstants.VISIBILITY_KIND_PROTECTED.equals(word)
				&& !IAcceleoConstants.VISIBILITY_KIND_PRIVATE.equals(word)) {
			while (shift < 0) {
				scanner.read();
				shift++;
			}
		}

		return shift;
	}

	/**
	 * Rewinds the scanner before the previous word.
	 * 
	 * @param scanner
	 *            is the current scanner
	 * @param previousWord
	 *            is the word to find in the previous characters
	 * @return the number of examined characters ( < 0 if the word matches)
	 */
	private int unreadPreviousWord(ICharacterScanner scanner, String previousWord) {
		assert previousWord.length() > 0;
		boolean valid = false;
		int shift = 0;
		if (scanner.getColumn() >= previousWord.length()) {
			int c;
			do {
				c = unreadChar(scanner);
				shift--;
			} while (Character.isWhitespace(c));
			if ("*".equals(previousWord)) { //$NON-NLS-1$
				while (Character.isJavaIdentifierPart(c) && scanner.getColumn() > 0) {
					valid = true;
					c = unreadChar(scanner);
					shift--;
				}
			} else {
				if (scanner.getColumn() >= previousWord.length() - 1) {
					valid = true;
					for (int i = previousWord.length() - 1; valid && i >= 0; i--) {
						if (previousWord.charAt(i) != c) {
							valid = false;
						} else if (i > 0) {
							c = unreadChar(scanner);
							shift--;
						}
					}
				}
			}
		}
		if (!valid) {
			while (shift < 0) {
				scanner.read();
				shift++;
			}
		}
		return shift;
	}

	/**
	 * Rewinds the scanner before the last read character, and returns this character.
	 * 
	 * @param scanner
	 *            is the current scanner
	 * @return the last read character
	 */
	private int unreadChar(ICharacterScanner scanner) {
		scanner.unread();
		int c = scanner.read();
		scanner.unread();
		return c;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner,
	 *      boolean)
	 */
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return evaluate(scanner);
	}

}
