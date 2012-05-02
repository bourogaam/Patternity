package com.patternity.usecase;

import java.io.IOException;
import java.io.InputStream;

import com.patternity.ast.ClassElement;
import com.patternity.ast.ClassScanner;
import com.patternity.ast.PackageElement;
import com.patternity.ast.asm.AsmScanner;
import com.patternity.ast.asm.SingleClassHandlerCollector;

public class TestUsecases {

	public static PackageElement scanPackage(final Class<?> type) throws IOException {
		final String resourceName = toResourcePath(type.getPackage().getName()) + "/package-info.class";
		final ClassElement clazz = scanClass(resourceName);
		return new PackageElement(clazz);
	}

	public static ClassElement scanClass(final Class<?> type) throws IOException {
		return scanClass(formatResourceName(type));
	}

	private static ClassScanner newScanner() {
		return new AsmScanner();
	}

	private static String formatResourceName(final Class<?> clazz) {
		return toResourcePath(clazz.getName()) + ".class";
	}

	private static String toResourcePath(final String name) {
		return "/" + name.replace('.', '/');
	}

	private static ClassElement scanClass(final String resourceName) throws IOException {
		return scanClass(openStreamOf(resourceName));
	}

	private static ClassElement scanClass(final InputStream stream) throws IOException {
		final SingleClassHandlerCollector handler = new SingleClassHandlerCollector();
		final ClassScanner scanner = newScanner();
		try {
			scanner.scan(stream, handler);
			return handler.getCollected();
		} finally {
			close(stream);
		}
	}

	private static void close(InputStream stream) {
		if (stream == null)
			return;
		try {
			stream.close();
		} catch (IOException e) {
			// ignore...
		}
	}

	private static InputStream openStreamOf(String resourceName) {
		return TestUsecases.class.getResourceAsStream(resourceName);
	}

}
