/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.obeonetwork.pim.gen.bootstrap.model.bootstrap.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.obeonetwork.pim.gen.bootstrap.model.bootstrap.util.BootstrapAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BootstrapItemProviderAdapterFactory extends BootstrapAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BootstrapItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Site} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SiteItemProvider siteItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Site}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSiteAdapter() {
		if (siteItemProvider == null) {
			siteItemProvider = new SiteItemProvider(this);
		}

		return siteItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Page} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PageItemProvider pageItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Page}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPageAdapter() {
		if (pageItemProvider == null) {
			pageItemProvider = new PageItemProvider(this);
		}

		return pageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.MainPage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MainPageItemProvider mainPageItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.MainPage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMainPageAdapter() {
		if (mainPageItemProvider == null) {
			mainPageItemProvider = new MainPageItemProvider(this);
		}

		return mainPageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Section} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SectionItemProvider sectionItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Section}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSectionAdapter() {
		if (sectionItemProvider == null) {
			sectionItemProvider = new SectionItemProvider(this);
		}

		return sectionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Form} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FormItemProvider formItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Form}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFormAdapter() {
		if (formItemProvider == null) {
			formItemProvider = new FormItemProvider(this);
		}

		return formItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Table} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TableItemProvider tableItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Table}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTableAdapter() {
		if (tableItemProvider == null) {
			tableItemProvider = new TableItemProvider(this);
		}

		return tableItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Text} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TextItemProvider textItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Text}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTextAdapter() {
		if (textItemProvider == null) {
			textItemProvider = new TextItemProvider(this);
		}

		return textItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Video} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VideoItemProvider videoItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Video}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVideoAdapter() {
		if (videoItemProvider == null) {
			videoItemProvider = new VideoItemProvider(this);
		}

		return videoItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Gallery} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GalleryItemProvider galleryItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Gallery}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGalleryAdapter() {
		if (galleryItemProvider == null) {
			galleryItemProvider = new GalleryItemProvider(this);
		}

		return galleryItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.ImagesBlock} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ImagesBlockItemProvider imagesBlockItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.ImagesBlock}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createImagesBlockAdapter() {
		if (imagesBlockItemProvider == null) {
			imagesBlockItemProvider = new ImagesBlockItemProvider(this);
		}

		return imagesBlockItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.TextArea} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TextAreaItemProvider textAreaItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.TextArea}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTextAreaAdapter() {
		if (textAreaItemProvider == null) {
			textAreaItemProvider = new TextAreaItemProvider(this);
		}

		return textAreaItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Spinner} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpinnerItemProvider spinnerItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.Spinner}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpinnerAdapter() {
		if (spinnerItemProvider == null) {
			spinnerItemProvider = new SpinnerItemProvider(this);
		}

		return spinnerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.CheckBox} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CheckBoxItemProvider checkBoxItemProvider;

	/**
	 * This creates an adapter for a {@link org.obeonetwork.pim.gen.bootstrap.model.bootstrap.CheckBox}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCheckBoxAdapter() {
		if (checkBoxItemProvider == null) {
			checkBoxItemProvider = new CheckBoxItemProvider(this);
		}

		return checkBoxItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (siteItemProvider != null) siteItemProvider.dispose();
		if (pageItemProvider != null) pageItemProvider.dispose();
		if (mainPageItemProvider != null) mainPageItemProvider.dispose();
		if (sectionItemProvider != null) sectionItemProvider.dispose();
		if (formItemProvider != null) formItemProvider.dispose();
		if (tableItemProvider != null) tableItemProvider.dispose();
		if (textItemProvider != null) textItemProvider.dispose();
		if (videoItemProvider != null) videoItemProvider.dispose();
		if (galleryItemProvider != null) galleryItemProvider.dispose();
		if (imagesBlockItemProvider != null) imagesBlockItemProvider.dispose();
		if (textAreaItemProvider != null) textAreaItemProvider.dispose();
		if (spinnerItemProvider != null) spinnerItemProvider.dispose();
		if (checkBoxItemProvider != null) checkBoxItemProvider.dispose();
	}

}