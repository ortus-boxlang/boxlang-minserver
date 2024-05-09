package ortus.boxlang.web.bifs;

import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.exceptions.BoxRuntimeException;

public class ForwardTest {

	static BoxRuntime	runtime;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		runtime = BoxRuntime.getInstance( true );
	}

	@AfterAll
	public static void teardown() {
	}

	@BeforeEach
	public void setupEach() {
		// TODO: Brad you need to simulate the web request context here please.
		context		= new ScriptingRequestBoxContext( runtime.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@Test
	@DisplayName( "It should fail if the template argument is not provided" )
	public void testNoTemplate() {
		// @formatter:off
		assertThrows( BoxRuntimeException.class, () -> {
			runtime.executeSource(
			"""
			forward();
			""",
			context );
		} );
		// @formatter:on
	}

	@Test
	@Disabled( "Until Brad figures out how to do web request context tests" )
	@DisplayName( "Forward to a different page" )
	public void testForward() {
		// @formatter:off
		runtime.executeSource(
		"""
		forward( template: "/some/path" );
		""",
		context );
		// @formatter:on
	}

}
