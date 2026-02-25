// src/test/java/com/shwetha/framework/listeners/UnifiedAnnotationTransformer.java
package com.shwetha.framework.listeners;

import com.shwetha.framework.utils.ConfigReader;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class UnifiedAnnotationTransformer implements IAnnotationTransformer {
    static {
        System.out.println("[UnifiedAnnotationTransformer] Active");
    }

    // Parallel Annotation Transformer: Apply to every @DataProvider
    // This will force every @DataProvider to be parallel (or not) based on config—so, can leave your annotations as @DataProvider(name = "login-data-map") without a hardcoded parallel value.
    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {
        boolean master = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false").trim());
        boolean dpPar  = Boolean.parseBoolean(ConfigReader.get("dp.parallel", "false").trim());
        annotation.setParallel(master && dpPar);
    }

    // Retry Transformer: Apply to every @Test
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        // inject RetryAnalyzer into evry @Test at runtime
        // if (annotation.getRetryAnalyzerClass() == null) { // will not load the retryAnalyzer
            annotation.setRetryAnalyzer(com.shwetha.framework.listeners.RetryAnalyzer.class);
        // }
    }
}