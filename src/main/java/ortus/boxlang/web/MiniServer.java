/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.web;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.web.handlers.BLHandler;
import ortus.boxlang.web.handlers.WelcomeFileHandler;

/**
 * The BoxLang MiniServer is a simple web server that serves BoxLang files and static files.
 *
 * The following command line arguments are supported:
 *
 * --port <port> - The port to listen on. Default is 8080.
 * --webroot <path> - The path to the webroot. Default is the current working directory.
 * --debug - Enable debug mode.
 * --host <host> - The host to listen on. Default is localhost.
 *
 * Example:
 *
 * java -jar boxlang-miniserver.jar --webroot /path/to/webroot --debug
 *
 * This will start the BoxLang MiniServer on port 8080, serving files from /path/to/webroot, and enable debug mode.
 */
public class MiniServer {

	private static BoxRuntime runtime;

	public static void main( String[] args ) {
		int		port	= 8080;
		String	webRoot	= "";
		boolean	debug	= false;
		String	host	= "localhost";
		// Grab --port and --webroot from args, if they exist
		// If --debug is set, enable debug mode
		for ( int i = 0; i < args.length; i++ ) {
			if ( args[ i ].equalsIgnoreCase( "--port" ) ) {
				port = Integer.parseInt( args[ ++i ] );
			}
			if ( args[ i ].equalsIgnoreCase( "--webroot" ) ) {
				webRoot = args[ ++i ];
			}
			if ( args[ i ].equalsIgnoreCase( "--debug" ) ) {
				debug = true;
			}
			if ( args[ i ].equalsIgnoreCase( "--host" ) ) {
				host = args[ ++i ];
			}
		}
		Path absWebRoot = Paths.get( webRoot ).normalize();
		if ( !absWebRoot.isAbsolute() ) {
			absWebRoot = Paths.get( "" ).resolve( webRoot ).normalize().toAbsolutePath().normalize();
		}
		System.out.println( "Starting BoxLang Server..." );
		System.out.println( "Web Root: " + absWebRoot.toString() );
		System.out.println( "Host: " + host );
		System.out.println( "Port: " + port );
		System.out.println( "Debug: " + debug );

		// Verify webroot exists on disk
		if ( !absWebRoot.toFile().exists() ) {
			System.out.println( "Web Root does not exist: " + absWebRoot.toString() );
			System.exit( 1 );
		}

		runtime = BoxRuntime.getInstance( debug );

		Undertow.Builder	builder			= Undertow.builder();
		ResourceManager		resourceManager	= new PathResourceManager( absWebRoot );
		Undertow			BLServer		= builder
		    .addHttpListener( port, host )
		    .setHandler( new WelcomeFileHandler(
		        Handlers.predicate(
		            // If this predicate evaluates to true, we process via BoxLang, otherwise, we serve a static file
		            Predicates.parse( "regex( '^(/.+?\\.cfml|/.+?\\.cf[cms]|.+?\\.bx[ms]{0,1})(/.*)?$' )" ),
		            new BLHandler( absWebRoot.toString() ),
		            new ResourceHandler( resourceManager )
		                .setDirectoryListingEnabled( true ) ),
		        resourceManager,
		        List.of( "index.bxm", "index.bxs", "index.cfm", "index.cfs", "index.htm", "index.html" )
		    ) )
		    .build();

		BLServer.start();
	}
}
