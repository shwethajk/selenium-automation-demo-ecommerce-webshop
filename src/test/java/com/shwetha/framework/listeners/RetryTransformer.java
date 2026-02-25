
// package com.shwetha.framework.listeners;

// import org.testng.IAnnotationTransformer;
// import org.testng.annotations.ITestAnnotation;

// import java.lang.reflect.Constructor;
// import java.lang.reflect.Method;

// public class RetryTransformer implements IAnnotationTransformer {
//     static {
//         // System.out.println("[RetryTransformer] static init: LOADED via ServiceLoader");
//         System.out.println("[RetryTransformer] Applied");
//     }

//     @Override
//     public void transform(ITestAnnotation annotation, Class testClass,
//                           Constructor testConstructor, Method testMethod) {
//         // inject RetryAnalyzer into evry @Test at runtime
//         // if (annotation.getRetryAnalyzerClass() == null) { // will not load the retryAnalyzer
//             annotation.setRetryAnalyzer(RetryAnalyzer.class);       
//         // }
//     }
// }