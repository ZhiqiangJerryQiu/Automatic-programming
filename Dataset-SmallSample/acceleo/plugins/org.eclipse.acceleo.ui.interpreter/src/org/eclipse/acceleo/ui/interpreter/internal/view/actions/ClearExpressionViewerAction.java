/*******************************************************************************
 * Copyright (c) 2010, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.acceleo.ui.interpreter.internal.view.actions;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * This action will be used to clear the "result" viewer.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ClearExpressionViewerAction extends AbstractClearViewerAction {
	/**
	 * Instantiates the "clear" action given the viewer it should operate on.
	 * 
	 * @param viewer
	 *            The viewer that should be cleared through this action.
	 */
	public ClearExpressionViewerAction(Viewer viewer) {
		super(viewer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (getViewer() instanceof TextViewer) {
			((TextViewer)getViewer()).getTextWidget().setText(""); //$NON-NLS-1$
		}
	}
}
