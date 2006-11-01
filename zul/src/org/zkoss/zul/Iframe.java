/* Iframe.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Jul 21 11:11:18     2005, Created by tomyeh
}}IS_NOTE

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
import org.zkoss.xml.HTMLs;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.ext.render.DynamicMedia;

import org.zkoss.zul.impl.XulElement;

/**
 * Includes an inline frame.
 *
 * @author tomyeh
 */
public class Iframe extends XulElement {
	private String _align, _name;
	private String _src;
	/** The media. If not null, _src is generated automatically. */
	private Media _media; 
	/** Count the version of {@link #_media}. */
	private int _medver;

	public Iframe() {
	}
	public Iframe(String src) {
		setSrc(src);
	}

	/** Returns the alignment.
	 * <p>Default: null (use browser default).
	 */
	public String getAlign() {
		return _align;
	}
	/** Sets the alignment: one of top, middle, bottom, left, right and
	 * center.
	 */
	public void setAlign(String align) {
		if (align != null && align.length() == 0)
			align = null;

		if (!Objects.equals(_align, align)) {
			_align = align;
			smartUpdate("align", _align);
		}
	}
	/** Returns the frame name.
	 * <p>Default: null (use browser default).
	 */
	public String getName() {
		return _name;
	}
	/** Sets the frame name.
	 */
	public void setName(String name) {
		if (name != null && name.length() == 0)
			name = null;

		if (!Objects.equals(_name, name)) {
			_name = name;
			smartUpdate("name", _name);
		}
	}

	/** Returns the src.
	 * <p>Default: null.
	 */
	public String getSrc() {
		return _src;
	}
	/** Sets the src.
	 * <p>If src is changed, the whole component is invalidate.
	 * Thus, you want to smart-update, you have to override this method.
	 *
	 * @param src the source URL. If null or empty, nothing is included.
	 */
	public void setSrc(String src) {
		if (src != null && src.length() == 0)
			src = null;

		if (!Objects.equals(_src, src)) {
			_src = src;
			if (_media == null) smartUpdate("src", getEncodedSrc());
				//_src is meaningful only if _media is null
		}
	}
	/** Returns the encoded src ({@link #getSrc}).
	 */
	private String getEncodedSrc() {
		final Desktop dt = getDesktop();
		return _media != null ? getMediaSrc(): //already encoded
			dt != null ? dt.getExecution().encodeURL(
				_src != null ? _src: "~./img/spacer.gif"):  "";
	}

	/** Sets the content directly.
	 * Default: null.
	 *
	 * @param media the media for this inline frame.
	 * If not null, it has higher priority than {@link #getSrc}.
	 */
	public void setContent(Media media) {
		if (media != _media) {
			_media = media;
			if (_media != null) ++_medver; //enforce browser to reload
			smartUpdate("src", getEncodedSrc());
		}
	}
	/** Returns the content set by {@link #setContent}.
	 * <p>Note: it won't fetch what is set thru by {@link #setSrc}.
	 * It simply returns what is passed to {@link #setContent}.
	 */
	public Media getContent() {
		return _media;
	}

	/** Returns the encoded URL for the current media content.
	 * Don't call this method unless _media is not null;
	 */
	private String getMediaSrc() {
		final Desktop desktop = getDesktop();
		if (desktop == null) return ""; //no avail at client

		final StringBuffer sb = new StringBuffer(64).append('/');
		Strings.encode(sb, _medver);
		final String name = _media.getName();
		final String format = _media.getFormat();
		if (name != null || format != null) {
			sb.append('/');
			boolean bExtRequired = true;
			if (name != null && name.length() != 0) {
				sb.append(name);
				bExtRequired = name.lastIndexOf('.') < 0;
			} else {
				sb.append(getId());
			}
			if (bExtRequired && format != null)
				sb.append('.').append(format);
		}
		return desktop.getDynamicMediaURI(this, sb.toString()); //already encoded
	}

	//-- super --//
	public String getOuterAttrs() {
		final StringBuffer sb =
			new StringBuffer(64).append(super.getOuterAttrs());
		HTMLs.appendAttribute(sb, "align",  _align);
		HTMLs.appendAttribute(sb, "name",  _name);
		HTMLs.appendAttribute(sb, "src",  getEncodedSrc());
		return sb.toString();
	}

	//-- Component --//
	/** Default: not childable.
	 */
	public boolean isChildable() {
		return false;
	}

	//-- ComponentCtrl --//
	protected Object newExtraCtrl() {
		return new ExtraCtrl();
	}
	/** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
	protected class ExtraCtrl extends XulElement.ExtraCtrl
	implements DynamicMedia {
		//-- DynamicMedia --//
		public Media getMedia(String pathInfo) {
			return _media;
		}
	}
}
