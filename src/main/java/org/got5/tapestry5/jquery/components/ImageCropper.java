//
// Copyright 2012 GOT5 (GO Tapestry 5)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.got5.tapestry5.jquery.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
* @since 3.3.1
* @see <a href="http://deepliquid.com/content/Jcrop.html">jCrop</a>
* 
* @tapestrydoc
*/
@Import(library = {"${assets.path}/components/jcrop/jquery.jcrop.js",
				   "${assets.path}/components/jcrop/imagecropper.js"},
	    stylesheet={"${assets.path}/components/jcrop/jquery.jcrop.css"})

@SupportsInformalParameters
public class ImageCropper implements ClientElement{
	
	@Environmental
    private JavaScriptSupport _support;

	@Inject
    private ComponentResources _resources;

	/**
	 * The image asset to render.
	 */
	@Parameter(required = true, defaultPrefix = "literal")
    private String _src;

	@Parameter(required = false, defaultPrefix = "literal", value = "context")
	private String _domain;

	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;
	
	@Inject
	private AssetSource assetSource;

	
	
	@BeginRender
	void begin(MarkupWriter writer)
	{
	    String clientId = _support.allocateClientId(_resources.getId());

	    Asset image = assetSource.getAsset(null, _domain + ":" + _src, null);
	    writer.element("img", "src", image.toClientURL(), "id", clientId);

	    _resources.renderInformalParameters(writer);
    }

	@BeforeRenderBody
	boolean beforeRenderBody()
	{
		return false;
	}

	@AfterRender
	void after(MarkupWriter writer)
	{
		writer.end();
		JSONObject jso = new JSONObject();
		jso.put("id", clientId);
		//jso.put("params", params);
		_support.addInitializerCall("imageCropper", jso);
	}

	public String getClientId() {
		return this.clientId;
	}

}
