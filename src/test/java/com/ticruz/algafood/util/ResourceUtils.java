package com.ticruz.algafood.util;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.springframework.util.StreamUtils;

public class ResourceUtils {
	
	public static String getContentFromResource(String resourceName)  {
		InputStream stream = ResourceUtils.class.getResourceAsStream(resourceName);
		try {
			return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

}
