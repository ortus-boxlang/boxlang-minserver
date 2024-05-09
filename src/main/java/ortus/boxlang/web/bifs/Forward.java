/**
 * [BoxLang]
 *
 * Copyright [2024] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package ortus.boxlang.web.bifs;

import io.undertow.server.HttpServerExchange;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;
import ortus.boxlang.web.context.WebRequestBoxContext;

@BoxBIF
public class Forward extends BIF {

	/**
	 * Constructor
	 */
	public Forward() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, Key.template )
		};
	}

	/**
	 *
	 * Leads the request to a different page.
	 * This function acts like the location functionality except that the relocation is done directly on the server and not the browser
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.template The logical path to which the request should be forwarded to.
	 *
	 */
	@SuppressWarnings( "deprecation" )
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		String					templatePath	= arguments.getAsString( Key.template );
		WebRequestBoxContext	requestContext	= context.getParentOfType( WebRequestBoxContext.class );
		HttpServerExchange		exchange		= requestContext.getExchange();

		exchange.setRequestPath( templatePath );
		exchange.setRelativePath( templatePath );
		exchange.dispatch();

		return null;
	}

}
