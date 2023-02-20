package com.slukovskyi.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.slukovskyi.utils.annotations.Exclude;

public class AnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}