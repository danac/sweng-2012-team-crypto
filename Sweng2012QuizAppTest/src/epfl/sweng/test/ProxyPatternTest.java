package epfl.sweng.test;

import java.lang.reflect.Field;

import epfl.sweng.patterns.CheckProxyHelper;

import android.test.AndroidTestCase;

/**
 * 
 * @author cyril
 *
 */
public class ProxyPatternTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testProxyPattern() {
		CheckProxyHelper proxyHelper = new CheckProxyHelper();
		for (Field field : proxyHelper.getProxyClass().getDeclaredFields()) {
			System.out.println(field.getType().toString());
		}
	}
}
