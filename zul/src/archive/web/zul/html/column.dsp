<%--
column.dsp

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Jan  9 14:50:13     2006, Created by tomyeh
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%><%@ taglib uri="/WEB-INF/tld/web/core.dsp.tld" prefix="c" %>
<c:set var="self" value="${requestScope.arg.self}"/>
<th id="${self.uuid}" z:type="Col"${self.outerAttrs}${self.innerAttrs}>${self.imgTag}<c:out value="${self.label}"/></th>
